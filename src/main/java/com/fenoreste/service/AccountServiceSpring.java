package com.fenoreste.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fenoreste.consumo.IConsumosHTTPService;
import com.fenoreste.dto.AccountDetailsDTO;
import com.fenoreste.dto.AccountLastMovementDTO;
import com.fenoreste.dto.AuxiliaresdDTO;
import com.fenoreste.dto.opaDTO;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Auxiliares_d;
import com.fenoreste.entity.FoliosTarjeta;
import com.fenoreste.entity.Origenes;
import com.fenoreste.entity.Productos;
import com.fenoreste.entity.Tablas;
import com.fenoreste.util.HerramientasUtil;

@Service
public class AccountServiceSpring {

	@Autowired
	HerramientasUtil util;
	@Autowired
	IAuxiliaresService auxiliaresService;
	@Autowired
	IAuxiliares_dService auxiliares_dService;
	@Autowired
	IProductosService productosService;
	@Autowired
	IOrigenesService origeneseService;
	@Autowired
	ITablasService tablasService;
	@Autowired
	IFoliosTarjetasService foliosService;
	@Autowired
	IConsumosHTTPService consumosService;
	@Autowired
	ISaiFuncionesService funcionesService;

	public AccountDetailsDTO getAccountDetails(String accountId) {

		opaDTO opa = util.opa(accountId);
		AccountDetailsDTO cuenta = new AccountDetailsDTO();
		String fechaOrigen = origeneseService.fechaTrabajo();
		try {
			Auxiliares aux = auxiliaresService.findAuxiliaresByOPA(opa.getIdorigenp(), opa.getIdproducto(),
					opa.getIdauxiliar());
			Productos prod = productosService.findProductoById(aux.getIdproducto());		    

			// Obtenemos saldo segun las horas pasadas
			double saldo24 = 0.0, saldo48 = 0.0, saldo_mas_de_48_horas = 0.0, saldo_promedio_mensual = 0.0;
			double arreglo_saldos[] = null;

			if (origeneseService.findMatrizOrigen().getNombre().toUpperCase().replace(" ", "").contains("SANNICOLAS")) {
				// Busco la tabla para saber cual es el producto tdd
				Tablas tb_producto_tdd = tablasService.findTabla("bankingly_banca_movil", "producto_tdd");
				if (opa.getIdproducto() == Integer.parseInt(tb_producto_tdd.getDato1())) {
					FoliosTarjeta folio_tdd = foliosService.findFolioByOpa(opa.getIdorigenp(), opa.getIdproducto(),
							opa.getIdauxiliar());
					String saldo_tdd = consumosService.getBalanceQuery(folio_tdd.getIdtarjeta());
					JSONObject json_saldo = new JSONObject(saldo_tdd);
					saldo24 = Double.parseDouble(json_saldo.getString("availableAmount"));
					saldo48 = Double.parseDouble(json_saldo.getString("availableAmount"));
					saldo_mas_de_48_horas = Double.parseDouble(json_saldo.getString("availableAmount"));
				} else {
					arreglo_saldos = getSaldoAuxiliaresD(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar(),
							substractDate(1, fechaOrigen),substractDate(1, fechaOrigen));
					saldo24 = arreglo_saldos[0];
					saldo48 = arreglo_saldos[1];
					saldo_mas_de_48_horas = arreglo_saldos[2];
				}
			} else {
				arreglo_saldos = getSaldoAuxiliaresD(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar(),
						substractDate(1, fechaOrigen),substractDate(2, fechaOrigen));
				saldo24 = arreglo_saldos[0];
				saldo48 = arreglo_saldos[1];
				saldo_mas_de_48_horas = arreglo_saldos[2];
			}

			// Movimient
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha_final_saldo_promedio = sdf.parse(fechaOrigen);
			List<Auxiliares_d> listaMov = auxiliares_dService.findAuxiliares_dByOpaFecha(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar(), subtractIntervalMonth(fechaOrigen),
					fecha_final_saldo_promedio);
			Origenes origen = origeneseService.findOrigenById(opa.getIdorigenp());
			String calculo_promedio = funcionesService.sai_calcula_saldo_promedio_diario(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar(), subtractIntervalMonth(fechaOrigen),
					fecha_final_saldo_promedio);
			cuenta.setAccountBankIdentifier(accountId);
			cuenta.setAccountOfficerName(prod.getNombre());
			cuenta.setAccountAvailableBalance(saldo24);
			cuenta.setAccountBalance24Hrs(saldo24);
			cuenta.setAccountBalance48Hrs(saldo48);
			cuenta.setAccountBalance48MoreHrs(saldo_mas_de_48_horas);
			cuenta.setMonthlyAverageBalance(Double.parseDouble(calculo_promedio.replace(",","")));
			cuenta.setPendingChecks(0);
			cuenta.setChecksToReleaseToday(0);
			cuenta.setChecksToReleaseTomorrow(0);
			cuenta.setCancelledChecks(0);
			cuenta.setCertifiedChecks(0);
			cuenta.setRejectedChecks(0);
			cuenta.setBlockedAmount(0);
			cuenta.setMovementsOfTheMonth(listaMov.size());
			cuenta.setChecksDrawn(0);
			cuenta.setOverdrafts(0.0);
			cuenta.setProductBranchName(prod.getNombre());
			cuenta.setProductOwnerName(origen.getNombre());
			cuenta.setShowCurrentAccountChecksInformation(true);
			cuenta.setAccountCountableBalance(0.0);

		} catch (Exception e) {
			System.out.println("Error en GetAccountDetails:" + e.getMessage());
		}
		return cuenta;

	}

	public List<AccountLastMovementDTO> getAccountLast5Movements(String accountId) {
		opaDTO opa = util.opa(accountId);

		boolean isDC = false;// is debit
		String Description = "";
		List<AccountLastMovementDTO> Last5Movements = new ArrayList<>();
		try {
			int movementTypeId = 0;
			List<Object[]> ListaAuxiliaresD = auxiliares_dService.findUltimos5Movimientos(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar());
			for (Object[] obj : ListaAuxiliaresD) {
				AccountLastMovementDTO cuenta = new AccountLastMovementDTO();
				Productos producto = productosService.findProductoById(opa.getIdproducto());
				if (Integer.parseInt(obj[1].toString()) == 0) {
					movementTypeId = 2;
					Description = "retiro";
				} else {
					movementTypeId = 3;
					Description = "Deposito";
				}
				cuenta.setAccountBankIdentifier(accountId);
				cuenta.setMovementId(Integer.parseInt(obj[3].toString()));
				cuenta.setDescription(producto.getNombre());
				cuenta.setAmount(Double.parseDouble(obj[2].toString()));
				cuenta.setBalance(Double.parseDouble(obj[4].toString()));
				cuenta.setMovementTypeId(movementTypeId);
				cuenta.setTypeDescription(Description);
				cuenta.setCheckId("0");
				cuenta.setVoucherId(obj[3].toString());
				cuenta.setIsDebit(true);
				cuenta.setMovementDate(obj[0].toString());
				Last5Movements.add(cuenta);

			}

		} catch (Exception e) {
			System.out.println("Error en GetAccountLast5Movements:" + e.getMessage());
		}
		return Last5Movements;
	}

	public List<AccountLastMovementDTO> getAccountMovements(String productBankIdentifier, String dateFromFilter,
			String dateToFilter, int pageSize, int pageStartIndex, String orderBy) {		
		String Description = "";
		List<AccountLastMovementDTO> ListaDTO = new ArrayList<>();
		String complemento = "";
		opaDTO opa = util.opa(productBankIdentifier);
		List<Object[]>Lista_Movs = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date finicial = null;
			Date ffinal = null;
			switch (orderBy.toUpperCase()) {
			
			case "MOVEMENTDATE ASC":
				complemento = "ORDER BY fecha ASC";
				//Si se ingreo fecha de inicio y fecha de fin de busqueda 
				if (!dateFromFilter.equals("") && !dateToFilter.equals("")) {
					finicial = sdf.parse(dateFromFilter);
					ffinal = sdf.parse(dateToFilter);
				    //Corro el query buscando por rango de fechas ordenado por fecha ASC
					Lista_Movs = auxiliares_dService.findMovimientosFechaAscRF(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),finicial,ffinal,PageRequest.of(pageStartIndex, pageSize));
				}else if (!dateFromFilter.equals("") && dateToFilter.equals("")) {
					finicial = sdf.parse(dateFromFilter);
					//Corro el query para traer solo los registros que sean mayor a la fecha de inicio ordenado por fecha ASC					
					Lista_Movs = auxiliares_dService.findMovimientosFechaAscFI(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),finicial,PageRequest.of(pageStartIndex, pageSize));
					
			    }else if (dateFromFilter.equals("") && !dateToFilter.equals("")) {
			    	//Corro el query para traer solo los registros que sean menor a la fecha de inicio ordenado por fecha ASC
			    	ffinal = sdf.parse(dateToFilter);
			    	Lista_Movs = auxiliares_dService.findMovimientosFechaAscFF(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),ffinal,PageRequest.of(pageStartIndex, pageSize));
			    }else {
			    	Lista_Movs = auxiliares_dService.findMovimientosFechaAsc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
			    }
				break;
			case "MOVEMENTDATE DESC":
				complemento = "ORDER BY fecha DESC";
				//Si se ingreo fecha de inicio y fecha de fin de busqueda 
				if (!dateFromFilter.equals("") && !dateToFilter.equals("")) {
					finicial = sdf.parse(dateFromFilter);
					ffinal = sdf.parse(dateFromFilter);
				    //Corro el query buscando por rango de fechas ordenado por fecha Desc	
					Lista_Movs = auxiliares_dService.findMovimientosFechaDescRF(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),finicial,ffinal,PageRequest.of(pageStartIndex, pageSize));
				}else if (!dateFromFilter.equals("") && dateToFilter.equals("")) {
					//Corro el query para traer solo los registros que sean mayor a la fecha de inicio ordenado por fecha Desc	
					finicial = sdf.parse(dateFromFilter);
					Lista_Movs = auxiliares_dService.findMovimientosFechaDescFI(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),finicial,PageRequest.of(pageStartIndex, pageSize));
			    }else if (dateFromFilter.equals("") && !dateToFilter.equals("")) {
			    	//Corro el query para traer solo los registros que sean menor a la fecha de inicio ordenado por fecha Desc
			    	ffinal = sdf.parse(dateToFilter);
			    	Lista_Movs = auxiliares_dService.findMovimientosFechaDescFF(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),ffinal,PageRequest.of(pageStartIndex, pageSize));
				}else {
					Lista_Movs = auxiliares_dService.findMovimientosFechaDesc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				}
				break;
			case "MOVEMENTDATE":
				complemento = "ORDER BY fecha";
				System.out.println("Entro aquii");
				//Corro el query para traer solo los registros ordenado por fecha Desc				
				Lista_Movs = auxiliares_dService.findMovimientosFechaDefault(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "ID ASC":
				complemento = "ORDER BY idpoliza ASC";
				Lista_Movs = auxiliares_dService.findMovimientosPolizaAsc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "ID DESC":
				complemento = "ORDER BY idpoliza DESC";
				Lista_Movs = auxiliares_dService.findMovimientosPolizaDesc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "ID":
				complemento = "ORDER BY idpoliza";
				Lista_Movs = auxiliares_dService.findMovimientosPoliza(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "DESCRIPTION ASC":
				complemento = "ORDER BY cargoabono ASC";
				Lista_Movs = auxiliares_dService.findMovimientosCargoAbonoAsc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "DESCRIPTION DESC":
				complemento = "ORDER BY cargoabono DESC";
				Lista_Movs = auxiliares_dService.findMovimientosCargoAbonoDesc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "DESCRIPTION":
				complemento = "ORDER BY cargoabono";
				Lista_Movs = auxiliares_dService.findMovimientosCargoAbono(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "AMOUNT ASC":
				complemento = "ORDER BY monto ASC";
				Lista_Movs = auxiliares_dService.findMovimientosMontoAsc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "AMOUNT DESC":
				complemento = "ORDER BY monto DESC";
				Lista_Movs = auxiliares_dService.findMovimientosMontoDesc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "AMOUNT":
				complemento = "ORDER BY monto";
				Lista_Movs = auxiliares_dService.findMovimientosMonto(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "BALANCE ASC":
				complemento = "ORDER BY saldoec ASC";
				Lista_Movs = auxiliares_dService.findMovimientosSaldoecAsc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "BALANCE DESC":
				complemento = "ORDER BY saldoec DESC";
				Lista_Movs = auxiliares_dService.findMovimientosSaldoecDesc(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			case "BALANCE":
				complemento = "ORDER BY saldoec";
				Lista_Movs = auxiliares_dService.findMovimientosSaldoec(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar(),PageRequest.of(pageStartIndex, pageSize));
				break;
			}

			int pageNumber = pageStartIndex;
			int pageSizes = pageSize;
			int inicioB = 0;
			String consulta = "";
							
				int movementTypeId = 0;
				for (Object[] obj : Lista_Movs) {
					if (Integer.parseInt(obj[1].toString()) == 1) {
						Description = "Abono";
						movementTypeId = 2;
					} else if (Integer.parseInt(obj[1].toString()) == 0) {
						Description = "Cargo";
						movementTypeId = 3;
					}
					Productos producto = productosService.findProductoById(opa.getIdproducto());					
					AccountLastMovementDTO cuentas = new AccountLastMovementDTO();
					
					cuentas.setAccountBankIdentifier(productBankIdentifier);
					cuentas.setMovementId(Integer.parseInt(obj[3].toString()) + Integer.parseInt(obj[4].toString())
							+ Integer.parseInt(obj[5].toString()) + Integer.parseInt(obj[6].toString()));
					cuentas.setDescription(producto.getNombre());
					cuentas.setAmount(Double.parseDouble(obj[2].toString()));
					cuentas.setBalance(Double.parseDouble(obj[7].toString()));
					cuentas.setMovementTypeId(movementTypeId);
					cuentas.setTypeDescription(Description);
					cuentas.setCheckId("0");
					cuentas.setVoucherId("0");
					cuentas.setIsDebit(true);
					cuentas.setMovementDate(obj[0].toString());					
					ListaDTO.add(cuentas);
				}
			
		} catch (Exception e) {
			System.out.println("Error en account:" + e.getMessage());
		} 
		return ListaDTO;
	}

	// Metodo que no ayuda a obtener saldo por rango de fechas, se utilizo en el
	// metodo de getAccountDetails
	public double[] getSaldoAuxiliaresD(int o, int p, int a, Date fecha_hace_24_horas, Date fecha_hace_48_horas) {
		double saldos[] = new double[3];
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// Si hay fecha de inicio
			Double saldo_ultimas_24_hora = auxiliares_dService.findSaldoUltimas24Horas(o, p, a, fecha_hace_24_horas);
			Double saldo_ultimas_48_hora = auxiliares_dService.findSaldoUltimas48Horas(o, p, a, fecha_hace_48_horas);
			Double saldo_mas_48_horas = auxiliares_dService.findSaldoMasDe48Horas(o, p, a, fecha_hace_48_horas);
			
			saldos[0] = saldo_ultimas_24_hora;
			saldos[1] = saldo_ultimas_48_hora;
			saldos[2] = saldo_mas_48_horas;
		} catch (Exception e) {
			System.out.println("Error en obtener  saldo auxiliares_d:" + e.getMessage());
		}
		return saldos;
	}

	public static Date substractDate(int numeroDias, String fecha) {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		// Si vamos a usar la fecha en tiempo real date=fechaActual
		// date = fechaActual;
		try {
			date = d.parse(fecha);
		} catch (ParseException ex) {
			System.out.println("Error al parsear fecha:" + ex.getMessage());
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -numeroDias);
		return cal.getTime();
	}

	public static Date subtractIntervalMonth(String fecha) {
		SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = d.parse(fecha);
		} catch (ParseException ex) {
			System.out.println("Error al parsear fecha:" + ex.getMessage());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

}
