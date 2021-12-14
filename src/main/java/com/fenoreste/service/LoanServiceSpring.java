package com.fenoreste.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.Auxiliares_dRepository;
import com.fenoreste.dto.AccountDetailsDTO;
import com.fenoreste.dto.FeesDueData;
import com.fenoreste.dto.LoanDTO;
import com.fenoreste.dto.LoanFee;
import com.fenoreste.dto.LoanFeeDTO;
import com.fenoreste.dto.LoanPayment;
import com.fenoreste.dto.LoanRate;
import com.fenoreste.dto.opaDTO;
import com.fenoreste.entity.Amortizacion;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.CatalogoEstatus;
import com.fenoreste.entity.Loan_Fee_Status;
import com.fenoreste.util.HerramientasUtil;

@Service
public class LoanServiceSpring {

	@Autowired
	HerramientasUtil util;
	@Autowired
	IAuxiliaresService auxiliaresService;
	@Autowired
	ITiposCuentaBankinglyService cuentasBankinglyService;
	@Autowired
	ICatalogoService catalogoService;
	@Autowired
	IAmortizacionesService amortizacionesService;
	@Autowired
	ISaiFuncionesService funcionesService;
	@Autowired
	IProductosService productosService;
	@Autowired
	ILoanFeeStatusService loanFeestatusService;

	public LoanDTO Loan(String productBankIdentifier) {
		opaDTO opa = util.opa(productBankIdentifier);
		AccountDetailsDTO cuenta = null;
		LoanDTO dto = new LoanDTO();

		try {
			Auxiliares aux = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(), opa.getIdproducto(),opa.getIdauxiliar());
			if (aux.getEstatus() == 2) {
				CatalogoEstatus catalogo_de_estatus = catalogoService.findCatalogoById(aux.getEstatus().intValue());
				Double currentBalance = Double.parseDouble(aux.getSaldo().toString());
				Double currentRate = Double.parseDouble(aux.getTasaio().toString());

				int loanStatusId = catalogo_de_estatus.getProductstatusid();

				// Amortizaciones pagagas
				Integer tot_amortizaciones_pagadas = amortizacionesService.findCountAmortizacionesPagadas(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
				int total_amortizaciones = amortizacionesService.findCountAmortizaciones(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
				
				
				dto.setAccountBankIdentifier(productBankIdentifier);
				dto.setCurrentBalance(currentBalance);
				dto.setCurrentRate(currentRate);	
				dto.setFeesDue(RSFeesDue(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar()));//Total abonos vencidos
				dto.setFeesDueData(RSFeesDueData(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar(),aux.getTipoamortizacion()));//Descripcion saldo vencido
				dto.setLoanStatusId(loanStatusId);				
				dto.setNextFee(nextFee(aux.getIdorigenp(), aux.getIdproducto(), aux.getIdauxiliar()));//informacion proxima cuota
				dto.setOriginalAmount(aux.getMontoprestado().doubleValue());
				dto.setOverdueDays(RSOverdueDays(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar()));
				dto.setPaidFees(tot_amortizaciones_pagadas);
				dto.setPayoffBalance(0.0);//Saldo de cancelacion(se obtiene con el sai auxilier)
				dto.setPrepaymentAmount(0.0);
				dto.setProductBankIdentifier(productBankIdentifier);
				dto.setTerm(total_amortizaciones);
				dto.setShowPrincipalInformation(true);
				dto.setLoanFeesResult(null);//LoanFees(productBankIdentifier,1,10,0,""));
				dto.setLoanRateResult(LoanRates(productBankIdentifier,1,0));
				dto.setLoanPaymentsResult(null);
			}
		} catch (Exception e) {
			System.out.println("Error en GetAccountDetails:" + e.getMessage());
		}

		return dto;// cuenta;

	}

	public LoanFee LoanFee(String productBankIdentifier, int feeNumber) {
		LoanFee loanFee = new LoanFee();
		opaDTO opa = util.opa(productBankIdentifier);
		try {
			Auxiliares aux = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(), opa.getIdproducto(),
					opa.getIdauxiliar());
			// Obtengo informacion con el sai_auxiliar hasta la fecha actual, si hay dudas
			// checar el catalogo o atributos que devuelve la funcion
			String sai_auxiliar = funcionesService.sai_auxiliar(opa.getIdorigenp(), opa.getIdproducto(),
					opa.getIdauxiliar());
			String[] partes_sai_auxiliar = sai_auxiliar.split("\\|");
			List list = Arrays.asList(partes_sai_auxiliar);

			// Obtengo la amortizacion con el idamortizacion
			List<Object[]> objeto_amortizacion_array = amortizacionesService.findAmortizacionByIdamortizacion(
					opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar(), feeNumber);
			Double iovencido = Double.parseDouble(list.get(6).toString()) + Double.parseDouble(list.get(17).toString());
			Double imvencido = Double.parseDouble(list.get(15).toString())+ Double.parseDouble(list.get(18).toString());

			int idamortizacion = 0;
			double abono = 0.0, abono_pag = 0.0;
			boolean todopag = false;
			String vence_str = "";
			for (Object[] obj_amortizacion : objeto_amortizacion_array) {
				vence_str = obj_amortizacion[0].toString();
				idamortizacion = Integer.parseInt(obj_amortizacion[1].toString());
				abono = Double.parseDouble(obj_amortizacion[2].toString());
				abono_pag = Double.parseDouble(obj_amortizacion[3].toString());
				todopag = Boolean.parseBoolean(obj_amortizacion[4].toString());
			}

			int loanfeests = 0;
			if (abono == abono_pag) {
				loanfeests = 3;
			} else if (abono > abono_pag && todopag == false) {
				loanfeests = 1;
			} else if (!list.get(14).toString().equals("C")) {
				loanfeests = 2;
			}

			Loan_Fee_Status loan_Fee = loanFeestatusService.findLoanFeeStatusById(loanfeests);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date vence = sdf.parse(vence_str);
			Double abonoT = abono + iovencido + imvencido;
			String converted = String.valueOf(convertToLocalDateTimeViaInstant(vence) + ":00.000Z");

			loanFee.setCapitalBalance(aux.getSaldo().doubleValue());
			loanFee.setFeeNumber(idamortizacion);
			loanFee.setPrincipalAmount(abonoT);
			loanFee.setDueDate(converted);
			loanFee.setInterestAmount(iovencido);
			loanFee.setOverdueAmount(imvencido);
			loanFee.setFeeStatusId(loan_Fee.getId());
			loanFee.setOthersAmount(0.0);
			loanFee.setTotalAmount(abonoT);

		} catch (Exception e) {
			System.out.println("Error en metodo LoanFee:" + e.getMessage());
		}
		return loanFee;
	}

	// Metodo para devolver abonos vencidos
	public int RSFeesDue(int o, int p, int a) {
		String sai_auxiliar = funcionesService.sai_auxiliar(o, p, a);
		int abonosVencidos = 0;
		try {
			String[] partes_sai = sai_auxiliar.split("\\|");
			List list = Arrays.asList(partes_sai);
			double redondeado = Math.ceil(Double.parseDouble(list.get(2).toString()));            
            abonosVencidos = (int) redondeado; 
		} catch (Exception e) {
			System.out.println("Error en metodo FeesDueData:" + e.getMessage());
		}
		return abonosVencidos;
	}

	// Obetner informacion cuotas vencidas
	public FeesDueData RSFeesDueData(int o, int p, int a, int tipoamortizacion) {
		String sai_auxiliar = funcionesService.sai_auxiliar(o, p, a);
		FeesDueData FeesDueDataRS = null;
		try {
			Auxiliares aux = auxiliaresService.AuxiliarByOpa(o, p, a);
			String[] partes_sai = sai_auxiliar.split("\\|");
			List lista_posiciones_sai = Arrays.asList(partes_sai);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha_sai = sdf.parse(lista_posiciones_sai.get(10).toString());
			//Calculo de lo que hay que pagar al dia de hoy
			String sai_bankingly_prestamo_cuanto = funcionesService.sai_prestamo_cuanto(o, p, a,fecha_sai,aux.getTipoamortizacion().intValue(),sai_auxiliar);
			Double interestAmount = 0.0, OverDueAmount = 0.0, principalAmount = 0.0, othersAmount = 0.0,principalAmountTotal = 0.0, intereses_creciente_adelanto = 0.0;
			System.out.println("Pretamo cuanto:"+sai_bankingly_prestamo_cuanto);
			/*String sai_intereses_creciente_adelanto = funcionesService.sai_prestamo_adelanto_intereses(o,p,a,sai_auxiliar);
			intereses_creciente_adelanto = Double.parseDouble(String.valueOf(sai_intereses_creciente_adelanto));	
			*/	
			String[] partes_cuanto_prestamo = sai_bankingly_prestamo_cuanto.split("\\|");
			List lista_posiciones_cuanto_prestamo = Arrays.asList(partes_cuanto_prestamo);
			for(int i=0;i<lista_posiciones_cuanto_prestamo.size();i++) {
				System.out.println("Pocision"+i+":"+lista_posiciones_cuanto_prestamo.get(i) );
			}
			
			if(!lista_posiciones_sai.get(13).toString().toUpperCase().equals("C")) {
				principalAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(2)));
				OverDueAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(5)))+Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(6)));;
				interestAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(3)));
				othersAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(4)));
				
				principalAmountTotal = interestAmount+OverDueAmount+othersAmount+principalAmount;
			}
			
			
			/*=========================================================================
			  casilla 1: Posicion 3 de funcion      casilla 2: posicion 4
			  casilla 3: Posicion 6 y 7             casilla 4: pocision 5
			  casilla 5: suma de 3,4,5,6,7
			 =========================================================================*/
			

			FeesDueDataRS = new FeesDueData();
			FeesDueDataRS.setFeesDuePrincipalAmount(principalAmount);//Casilla 1 de la app(monto vencido posicion 3 de la funcion)
			FeesDueDataRS.setFeesDueInterestAmount(interestAmount);//Casilla 2 de la app(solo interes ordinario)
			FeesDueDataRS.setFeesDueOverdueAmount(OverDueAmount);//Casilla 3 de la app(IM+ ivaIM)			
			FeesDueDataRS.setFeesDueOthersAmount(othersAmount);//Casilla 4 de la app(Iva de io)						
			FeesDueDataRS.setFeesDueTotalAmount(principalAmountTotal);
		} catch (Exception e) {
			System.out.println("Error en FeesDueData:" + e.getMessage());
		}
		System.out.println("FeesDueData:" + FeesDueDataRS);
		return FeesDueDataRS;
	}

	public List<LoanFee> LoanFees(String productBankIdentifier, int feesStatus, int pageSize, int pageStartIndex,String order) {
		opaDTO opa = util.opa(productBankIdentifier);
		List<LoanFee> listaFees = new ArrayList<>();
		order = order.replace(" ", "").trim();
		try {
			Auxiliares aux = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(), opa.getIdproducto(),opa.getIdauxiliar());
			// Obtengo informacion con el sai_auxiliar hasta la fecha actual, si hay dudas
			// checar el catalogo o atributos que devuelve la funcion
			String sai_auxiliar = funcionesService.sai_auxiliar(opa.getIdorigenp(), opa.getIdproducto(),opa.getIdauxiliar());
			String[] partes_sai = sai_auxiliar.split("\\|");
			List list = Arrays.asList(partes_sai);
			String complemento = "";
			List<Object[]> amortizacion_objetos = null;
			
			System.out.println("feeStatus:"+feesStatus+", order:"+order);
			if (feesStatus == 0 && order.equalsIgnoreCase("feenumber")) {
				amortizacion_objetos = amortizacionesService.findAmortizaciones(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(), PageRequest.of(pageStartIndex, pageSize));
				// complemento = "ORDER BY (idorigenp+idproducto+idauxiliar+idamortizacion)
				// ASC";
			} else if (feesStatus == 1 && order.equalsIgnoreCase("feenumber")) {
				amortizacion_objetos = amortizacionesService.findAmortizacionesPagadasIdamortizacionAsc(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				
				// complemento = "AND todopag=true ORDER BY
				// (idorigenp+idproducto+idauxiliar+idamortizacion) ASC";
			} else if (feesStatus == 1 && order.equals("")) {
				amortizacion_objetos = amortizacionesService.findAmortizacionesPagadas(opa.getIdorigenp(),opa.getIdproducto(), opa.getIdauxiliar(), PageRequest.of(pageStartIndex, pageSize));
				// complemento = "AND todopag=true";
			} else if (feesStatus == 2 && order.equalsIgnoreCase("feenumber")) {
				amortizacion_objetos = amortizacionesService.findAmortizacionesActivasIdamortizacionAsc(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				// complemento = "AND todopag=false ORDER BY
				// (idorigenp+idproducto+idauxiliar+idamortizacion) ASC";
			} else if (feesStatus == 2 && order.equals("")) {
				amortizacion_objetos = amortizacionesService.findAmortizacionesActivas(opa.getIdorigenp(),opa.getIdproducto(), opa.getIdauxiliar(), PageRequest.of(pageStartIndex, pageSize));
				// complemento = "AND todopag=false";
			}else if(feesStatus == 0 && order.equals("")) {
				amortizacion_objetos = amortizacionesService.findAmortizacionesTotal(opa.getIdorigenp(),opa.getIdproducto(), opa.getIdauxiliar(), PageRequest.of(pageStartIndex, pageSize));
			}

			Double iovencido = Double.parseDouble(list.get(6).toString()) + Double.parseDouble(list.get(17).toString());
			Double imvencido = Double.parseDouble(list.get(15).toString())+ Double.parseDouble(list.get(18).toString());

			double abono = 0.0;
			double abono_pag = 0.0;
			boolean todopag = false;

			for (Object[] obj : amortizacion_objetos) {
				LoanFee loanFeeDTO = loanFeeDTO = new LoanFee();
				int loanfeests = 0;
				if (abono == abono_pag) {
					loanfeests = 3;
				} else if (abono > abono_pag && todopag == false) {
					loanfeests = 1;
				} else if (!list.get(14).toString().equals("C")) {
					loanfeests = 2;
				}
				Loan_Fee_Status loanf = loanFeestatusService.findLoanFeeStatusById(loanfeests);
				// LocalDateTime now = LocalDateTime.now();
				Double abonoT = abono + iovencido + imvencido;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date vence = sdf.parse(obj[0].toString());
				String converted = String.valueOf(convertToLocalDateTimeViaInstant(vence) + ":00.000Z");

				loanFeeDTO.setCapitalBalance(aux.getSaldo().doubleValue());
				loanFeeDTO.setFeeNumber(Integer.parseInt(obj[1].toString()));
				loanFeeDTO.setPrincipalAmount(abono);
				loanFeeDTO.setDueDate(converted);
				loanFeeDTO.setInterestAmount(iovencido);
				loanFeeDTO.setOverdueAmount(imvencido);
				loanFeeDTO.setFeeStatusId(loanf.getId());
				loanFeeDTO.setOthersAmount(0.0);
				loanFeeDTO.setTotalAmount(abonoT);
				listaFees.add(loanFeeDTO);
			}
			System.out.println("Lista de cuotas : "+ listaFees);
		} catch (Exception e) {
			System.out.println("Error en LoanFees:" + e.getMessage());
		}
		return listaFees;
	}

	public List<LoanRate> LoanRates(String productBankIdentifier, int pageSize, int pageStartIndex) {
		opaDTO opa = util.opa(productBankIdentifier);
		List<LoanRate> listaRates = new ArrayList<>();
		// Consulto tasas
		
		Auxiliares aux = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(), opa.getIdproducto(),opa.getIdauxiliar());
		List<String> lista_tasas = new ArrayList<>();
		//String tasaim = aux.getTasaim().toString();
		String tasaio = aux.getTasaio().toString();
		//String tasaiod = aux.getTasaiod().toString();		

		String converted = aux.getFechaactivacion().toString().replace("\\/", "-") + "T00:00:00.000Z";
		for (int i = 0; i < lista_tasas.size(); i++) {
			LoanRate loanRate = new LoanRate();
			loanRate.setId(1);
			loanRate.setInitialDate(converted);
			loanRate.setRate(Double.parseDouble(tasaio));
			listaRates.add(loanRate);
		}

		return listaRates;
	}

	public List<LoanPayment> loanPayments(String productBankIdentifier, int pageSize, int startPageIndex) {
		System.out.println("Entrando a loan Payments entity");
	    opaDTO opa = util.opa(productBankIdentifier);
		
		List<LoanPayment> listPayment = new ArrayList<LoanPayment>();
		List<Object[]>Total_amortizaciones = amortizacionesService.findAmortizacionesTotal(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(0,10/*tartPageIndex, pageSize*/));
		int payEstatus = 0;
		String converted = "";
		boolean todopag = false;
		for (Object[] obj : Total_amortizaciones) {			
			LoanPayment loanp = new LoanPayment();
			if ( Boolean.parseBoolean(obj[3].toString())) {
				payEstatus = 1;
			} else {
				payEstatus = 3;
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date vence=null;
			try {
				vence = sdf.parse(obj[0].toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			converted = String.valueOf(convertToLocalDateTimeViaInstant(vence) + ":00.000Z");
			loanp.setCapitalBalance(Double.parseDouble(obj[2].toString()));
			loanp.setFeeNumber(Integer.parseInt(obj[1].toString()));
			loanp.setMovementType(2);
			loanp.setOthersAmount(Double.parseDouble(obj[3].toString()));
			loanp.setOverdueInterestAmount(0.0);
			loanp.setNormalInterestAmount(Double.parseDouble(obj[5].toString()));
			loanp.setPaymentDate(converted);
			loanp.setPrincipalAmount(Double.parseDouble(obj[2].toString()));
			loanp.setTotalAmount(Double.parseDouble(obj[2].toString())+Double.parseDouble(obj[5].toString()));
		
			listPayment.add(loanp);
		}
		System.out.println("Lista Payments:"+listPayment);
	    return listPayment;
	}

	//informacion sobre el proximo pago
	public LoanFee nextFee(int o, int p, int a) {
		// Objeto para cuota
		LoanFee loanFee = new LoanFee();
		try {
			Auxiliares aux = auxiliaresService.AuxiliarByOpa(o, p, a);
			
			String sai_auxiliar = funcionesService.sai_auxiliar(o, p, a);
			String[] partes_sai = sai_auxiliar.split("\\|");
			List list = Arrays.asList(partes_sai);
			Double montoCuota = 0.0, intereses_creciente_adelanto = 0.0;
			Double interestAmount = 0.0, OverDueAmount = 0.0, principalAmount = 0.0, othersAmount = 0.0;
			
			int idamortizacion = 0;
			int estatus_amortizacion = 0;// El estatus de la amortizacion
			Double abono = 0.0, abono_pag = 0.0;
			boolean todopag = false;
			
			   List<Object[]> objeto_amortizacion_array = amortizacionesService.findProximaAmortizacion(o, p, a);
				for (Object[] obj_amortizacion : objeto_amortizacion_array) {
					idamortizacion = Integer.parseInt(obj_amortizacion[1].toString());
					abono = Double.parseDouble(obj_amortizacion[2].toString());
					abono_pag = Double.parseDouble(obj_amortizacion[3].toString());
					todopag = Boolean.parseBoolean(obj_amortizacion[4].toString());
				}

				if (abono == abono_pag) {
					estatus_amortizacion = 3;
				} else if (abono > abono_pag && todopag == false) {
					estatus_amortizacion = 1;
				} else if (!list.get(13).toString().equals("C")) {// Si esta vencido
					estatus_amortizacion = 2;
				}
			
				// Para obtener el total de la proxima cuota total corro la funcion de pago
				// completo
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fecha_sai = sdf.parse(list.get(10).toString()); 
				String sai_bankingly_prestamo_cuanto = funcionesService.sai_prestamo_cuanto(o, p, a,fecha_sai,aux.getTipoamortizacion().intValue(), sai_auxiliar);
				System.out.println("Prestamo cuanto:"+sai_bankingly_prestamo_cuanto);
				String[] partes_cuanto_prestamo = sai_bankingly_prestamo_cuanto.split("\\|");
				List lista_posiciones_cuanto_prestamo = Arrays.asList(partes_cuanto_prestamo);
				System.out.println("Pocision 0 prestamo cuanto:"+Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(0))));
				
				
				interestAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(3)));
				OverDueAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(2)))+Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(5)));
		        othersAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(4)))+Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(6)));
		        principalAmount = Double.parseDouble(String.valueOf(lista_posiciones_cuanto_prestamo.get(7)));
		        montoCuota = interestAmount+OverDueAmount+othersAmount+principalAmount;		
			
			loanFee.setCapitalBalance(Double.parseDouble(aux.getSaldo().toString()));
			loanFee.setFeeNumber(idamortizacion);
			loanFee.setPrincipalAmount(principalAmount);// monto de la cuota(Se obtiene de la funcion pocision 7)
			loanFee.setDueDate(String.valueOf(list.get(10)));// fecha de vencimiento pocision 3 de sai auxiliar
			loanFee.setInterestAmount(interestAmount);// Monto de interes io + ivaio
			loanFee.setOverdueAmount(OverDueAmount);// Monto im+ivaim
			loanFee.setFeeStatusId(estatus_amortizacion);// Estado de la amortizacion
			loanFee.setOthersAmount(othersAmount);// Otros conceptos asociados seguros si tiene hipotecario
			loanFee.setTotalAmount(montoCuota);// Monto total de la cuota

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en obtener la proxima cuota:" + e.getMessage());
		}

		return loanFee;
	}

	// Metodo para devolver dias vencidos
	public int RSOverdueDays(int o, int p, int a) {
		int diasVencidos = 0;
		try {
			String sai_auxiliar = funcionesService.sai_auxiliar(o, p, a);
			String[] partes_sai = sai_auxiliar.split("\\|");
			List list = Arrays.asList(partes_sai);
			diasVencidos = Integer.parseInt(list.get(3).toString());
		} catch (Exception e) {
			System.out.println("Error en FeesDueData:" + e.getMessage());
		}
		return diasVencidos;
	}

	public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
		LocalDateTime localDateTime = null;
		try {
			localDateTime = dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		} catch (Exception e) {
			System.out.println("Error al convertir fecha:" + e.getMessage());
		}

		return localDateTime;
	}

}
