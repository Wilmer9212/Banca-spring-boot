package com.fenoreste.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.consumo.IConsumosHTTPService;
import com.fenoreste.dto.*;
import com.fenoreste.entity.Amortizacion;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Auxiliares_d;
import com.fenoreste.entity.FoliosTarjeta;
import com.fenoreste.entity.Origenes;
import com.fenoreste.entity.Persona;
import com.fenoreste.entity.Tablas;
import com.fenoreste.entity.TiposCuentaBankingly;
import com.fenoreste.util.HerramientasUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

@Service
public class ProductsServiceSpring {

	@Autowired
	ITablasService tablasService;
	@Autowired
	IAuxiliaresService auxiliaresService;
	@Autowired
	ITiposCuentaBankinglyService cuentasBankinglyService;
	@Autowired
	IOrigenesService origenesService;
	@Autowired
	HerramientasUtil util;
	@Autowired
	IFoliosTarjetasService foliosService;
	@Autowired
	IConsumosHTTPService httpService;
	@Autowired
	ISaiFuncionesService funcionesService;
	@Autowired
	IPersonaService personasService;
	@Autowired
	IAmortizacionesService amortizacionesService;
	@Autowired
	IAuxiliares_dService auxiliares_dService;

	public List<ProductosDTO> listaProductos(String clientBankIdnetifier, Integer productType) {

		String productTypeId = "", descripcion = "",canTransact="";
		
		ogsDTO ogs = util.ogs(clientBankIdnetifier);
		List<ProductosDTO> listaDto = new ArrayList<>();

		List<Auxiliares> auxiliaresBancaMovil = null;
		if (productType != null) {			
			auxiliaresBancaMovil = auxiliaresService.findListaCuentasBankingly(ogs.getIdorigen(),ogs.getIdgrupo(), ogs.getIdsocio(), productType);
		} else {
			auxiliaresBancaMovil = auxiliaresService.findListaCuentasBankinglySinType(ogs.getIdorigen(),ogs.getIdgrupo(), ogs.getIdsocio());
		}
		
		System.out.println("Lista productos :"+auxiliaresBancaMovil);
		TiposCuentaBankingly cuenta_bankingly = null;
		for (int i = 0; i < auxiliaresBancaMovil.size(); i++) {			
			ProductosDTO auxiliar_dto = new ProductosDTO();
			Auxiliares a = auxiliaresBancaMovil.get(i);
			cuenta_bankingly = cuentasBankinglyService.findTipoCuentaById(a.getIdproducto());
			productTypeId = String.valueOf(cuenta_bankingly.getProductTypeId());
			descripcion = cuenta_bankingly.getDescripcion();
			String opa = String.format("%06d", a.getIdorigenp()) + String.format("%05d", a.getIdproducto())+ String.format("%08d", a.getIdauxiliar());
			// Por efault el estatus del producto le mando un activo pero si es necesario
			// revisar la tablas catalogo_status_bankingly
			int estatus_producto = 1;
			
			Tablas producto_retiro = null;
			if (origenesService.findMatrizOrigen().getIdorigen()==30200) {
				producto_retiro = tablasService.findTabla("bankingly_banca_movil", "producto_retiro");
				if (a.getIdproducto() == Integer.parseInt(producto_retiro.getDato1())) {
					canTransact = "3";// Solo retiro
				} else {
					canTransact = "2";// Solo deposito
				}
		    //Mitras
			}else if(origenesService.findMatrizOrigen().getIdorigen() == 30300) {
				producto_retiro = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","producto_retiro");
				System.out.println("producto_retiro:"+producto_retiro);
				if (a.getIdproducto() == Integer.parseInt(producto_retiro.getDato1())) {
					canTransact = "3";// Solo retiro
				} else {
					 //Si es una inversion
					 if(cuenta_bankingly.getProductTypeId()==4) {
						 canTransact="0";//solo consulta de saldos
					 }else {
						 canTransact = "2";// Solo deposito	 
					 }					
				}
			}

			auxiliar_dto.setProductBankIdentifier(opa);
			auxiliar_dto.setClientBankIdentifier(clientBankIdnetifier);
			auxiliar_dto.setProductNumber(String.valueOf(a.getIdproducto()));
			auxiliar_dto.setProductStatusId(estatus_producto);
			auxiliar_dto.setProductTypeId(productTypeId);
			auxiliar_dto.setProductAlias(descripcion);
			auxiliar_dto.setCanTransact(canTransact);
			auxiliar_dto.setCurrencyId("1");
			listaDto.add(auxiliar_dto);

			// Limpiamos variables
			productTypeId = "";
			descripcion = "";
		}

		return listaDto;

	}

	public List<ProductConsolidatedPositionDTO> consolidatedPosition(String clientBankIdentifier,List<String> productsBank) {
		List<ProductConsolidatedPositionDTO> ListaReturn = new ArrayList<>();
		ogsDTO ogs = util.ogs(clientBankIdentifier);
		Double saldo = 0.0;

		for (int ii = 0; ii < productsBank.size(); ii++) {
			// Obtengo el opa de formateado de cada folio en la lista
			opaDTO opa = util.opa(productsBank.get(ii));
			// Genero la consulta para buscar el auxiliar
			Auxiliares a = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
			if (a != null) {
				saldo = a.getSaldo().doubleValue();

				// Si el origen es CSN
				if (origenesService.findMatrizOrigen().getNombre().toUpperCase().replace(" ", "").contains("SANNICOLAS")) {
					// Si el producto es la TDD
					Tablas tb_producto_tdd = tablasService.findTabla("bankingly_banca_movil", "producto_tdd");
					if (a.getIdproducto() == Integer.parseInt(tb_producto_tdd.getDato1())) {
						// Consumimos a Alestra
						FoliosTarjeta folioTdd = foliosService.findFolioByOpa(a.getIdorigenp(), a.getIdproducto(),a.getIdauxiliar());
						JSONObject BalanceQuery = null;
						try {
							BalanceQuery = new JSONObject(httpService.getBalanceQuery(folioTdd.getIdtarjeta()));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				String vencimiento = "";
				Double tasa = 0.0;
				String proximo_pago = "";
				int cuotas_pagadas_int = 0;
				int total_cuotas_int = 0;
				String canTransact = "";
				TiposCuentaBankingly cuenta_bankingly = cuentasBankinglyService.findTipoCuentaById(a.getIdproducto());
				// si es una inversion porque los datos en el sai_auxilair lo da de diferente
				// manera por eso identificamos que sea una inversion
				if (cuenta_bankingly.getProductTypeId() == 4 || cuenta_bankingly.getProductTypeId() == 5) {
					String sai_auxiliar = funcionesService.sai_auxiliar(a.getIdorigenp(), a.getIdproducto(),a.getIdauxiliar());
					if (cuenta_bankingly.getProductTypeId() == 4) {
						String[] partes_sai_inversiones = sai_auxiliar.split("\\|");
						List lista_posciones_sai_inversiones = Arrays.asList(partes_sai_inversiones);
						String[] partes_fecha_vencimiento = lista_posciones_sai_inversiones.get(2).toString()
								.split("/");
						vencimiento = partes_fecha_vencimiento[2] + "-" + partes_fecha_vencimiento[1] + "-"
								+ partes_fecha_vencimiento[0];// String.valueOf(lista_posciones_sai_inversiones.get(2).toString().replace("/","-"));
						tasa = Double.parseDouble(a.getTasaio().toString());
					} else if (cuenta_bankingly.getProductTypeId() == 5) {
						System.out.println("sai_auxiliar :    : "+sai_auxiliar);
						String[] partes_sai_prestamo = sai_auxiliar.split("\\|");
						List posiciones_sai = Arrays.asList(partes_sai_prestamo);// Posiciones sai
						for(int i=0;i< posiciones_sai.size();i++) {
							//System.out.println("El valor en la posicion "+i+":"+posiciones_sai.get(i).toString());
						}
						double io = Double.parseDouble(posiciones_sai.get(6).toString())+ Double.parseDouble(posiciones_sai.get(17).toString());
						double im = Double.parseDouble(posiciones_sai.get(15).toString())+ Double.parseDouble(posiciones_sai.get(18).toString());

						Double saldo1 = a.getSaldo().doubleValue() + io + im;
						saldo = saldo1;
						vencimiento = String.valueOf(posiciones_sai.get(8));

						int cuotas_pagadas = amortizacionesService.findCountAmortizacionesPagadas(a.getIdorigenp(), a.getIdproducto(), a.getIdauxiliar());
						cuotas_pagadas_int = cuotas_pagadas;
						Integer total_cuotas = amortizacionesService.findCountAmortizaciones(a.getIdorigenp(), a.getIdproducto(), a.getIdauxiliar());
						total_cuotas_int = total_cuotas;
						proximo_pago = String.valueOf(posiciones_sai.get(10));

					}					
				}else {
					saldo=a.getSaldo().doubleValue();
				}
				Tablas producto_retiro = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","producto_retiro");
				
				if (a.getIdproducto() == Integer.parseInt(producto_retiro.getDato1())){
					canTransact = "3";// Solo retiro
				} else {
					 //Si es una inversion
					 if(cuenta_bankingly.getProductTypeId()==4) {
						 canTransact="0";//solo consulta de saldos
					 }else {
						 canTransact = "2";// Solo deposito	 
					 }					
				}
				// Busco la persona solo para obtener el nombre
				Persona p = personasService.findByOGS(a.getIdorigen(), a.getIdgrupo(), a.getIdsocio());
				ProductConsolidatedPositionDTO dto = new ProductConsolidatedPositionDTO();
				dto.setClientBankIdentifier(clientBankIdentifier);
				dto.setProductBankIdentifier(productsBank.get(ii));
				dto.setProductTypeId(String.valueOf(cuenta_bankingly.getProductTypeId()));
				dto.setProductAlias(cuenta_bankingly.getDescripcion());
				dto.setProductNumber(String.valueOf(a.getIdproducto()));
				dto.setLocalCurrencyId(1);
				dto.setLocalBalance(saldo);
				dto.setInternationalCurrencyId(1);
				dto.setInternationalBalance(0.0);
				dto.setRate(tasa);
				dto.setExpirationDate(vencimiento);
				dto.setPaidFees(cuotas_pagadas_int);
				dto.setTerm(total_cuotas_int);
				dto.setNextFeeDueDate(proximo_pago);
				dto.setProductOwnerName(p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno());
				dto.setProductBranchName(origenesService.findOrigenById(a.getIdorigenp()).getNombre());
				dto.setCanTransact(Integer.parseInt(canTransact));
				dto.setSubsidiaryId(1);
				dto.setSubsidiaryName("");
				dto.setBackendId(1);
				
				ListaReturn.add(dto);

			}

		}
		return ListaReturn;
	}

	public List<ProductBankStatementDTO> statements(String cliente, String productBankIdentifier, int productType) {
		List<ProductBankStatementDTO> listaEstadosDeCuenta = new ArrayList<>();
		opaDTO opa = util.opa(productBankIdentifier);
		ogsDTO ogs = util.ogs(cliente);

		Auxiliares a = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(), opa.getIdproducto(), opa.getIdauxiliar());
		if (a != null) {
			// Busco la fecha Trabajo
			String fechaTrabajo = origenesService.fechaTrabajo();
			String inicio_mes_fecha_trabajo = fechaTrabajo.substring(0, 7) + "-01";
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDate = LocalDateTime.parse(inicio_mes_fecha_trabajo + " 00:00:00", dtf);
			Tablas tb_total_estados_cta = tablasService.findTabla("bankingly_banca_movil", "total_estados_cuenta");
			// Defino variable para inicio y fin de cada estado de cuenta
			String fecha_inicio = "";
			String fecha_fin = "";
			int c=0;
			for (int i = 0; i < Integer.parseInt(tb_total_estados_cta.getDato1()); i++) {
				
				ProductBankStatementDTO estadoCuenta = new ProductBankStatementDTO();
				fecha_fin = String.valueOf(localDate.plusMonths(-i));
				fecha_inicio = String.valueOf(localDate.plusMonths(-i - 1));				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaInicio=null;
				Date fechaFinal = null;
				try {
					fechaFinal = sdf.parse(fecha_fin);
					fechaInicio = sdf.parse(fecha_inicio);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<Auxiliares_d> listaAuxiliares_d = auxiliares_dService.findAuxiliares_dByOpaFecha(a.getIdorigenp(),a.getIdproducto(), a.getIdauxiliar(), fechaInicio,fechaFinal);
				if (listaAuxiliares_d.size() > 0) {
					
					File file = crear_llenar_txt(productBankIdentifier, fechaInicio,fechaFinal, productType);
					if (file.isFile()) {
						
						// Creo un html
						try {
							File fileHTML = crear_llenar_html(file, file.getName().replace(".txt", ".html"));
							if (crearPDF(ruta(), fileHTML.getName())) {
								estadoCuenta.setProductBankIdentifier(dtf.format(LocalDateTime.now()));
								estadoCuenta.setProductBankIdentifier(productBankIdentifier);
								estadoCuenta.setProductBankStatementId(fileHTML.getName().replace(".html",""));
								estadoCuenta.setProductType(productType);
								estadoCuenta.setProductBankStatementDate(fecha_fin.substring(0, 10));
								listaEstadosDeCuenta.add(estadoCuenta);
								file.delete();
								fileHTML.delete();
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		return listaEstadosDeCuenta;
	}

	public File crear_llenar_txt(String opaCadena, Date FInicio, Date FFinal, int tipoproducto) {
		int numeroAleatorio = (int) (Math.random() * 9 + 1);
		opaDTO opa = util.opa(opaCadena);
		System.out.println("Fechas:"+FInicio+"fechaFin:"+FFinal);
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("HH:mm:ss.SSS");
		String hora = dateFormatLocal.format(new Date());

		String resultado_sai_estado_cuenta = "";
		String nombre_txt = "";
		if (tipoproducto == 1) {
			nombre_txt = "e_cuenta_corriente";
			resultado_sai_estado_cuenta = funcionesService.sai_estado_cuenta_ahorros(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar(), FInicio, FFinal);
		} else if (tipoproducto == 2) {
			nombre_txt = "e_cuenta_ahorro";
			resultado_sai_estado_cuenta = funcionesService.sai_estado_cuenta_ahorros(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar(), FInicio, FFinal);
		} else if (tipoproducto == 4) {
			nombre_txt = "e_cuenta_inversion";
			resultado_sai_estado_cuenta = funcionesService.sai_estado_cuenta_inversiones(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar(), FInicio, FFinal);
		} else if (tipoproducto == 5) {
			nombre_txt = "e_cuenta_prestamo";
			resultado_sai_estado_cuenta = funcionesService.sai_estado_cuenta_prestamos(opa.getIdorigenp(),
					opa.getIdproducto(), opa.getIdauxiliar(), FInicio, FFinal);
			
			System.out.println("Resultado:"+resultado_sai_estado_cuenta);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		
		nombre_txt = nombre_txt + sdf.format(FInicio).substring(0, 10).replace("-", "") + ""
				+ sdf.format(FFinal).substring(0, 10).replace("-", "") + hora.replace(":", "").replace(".","")+ String.valueOf(numeroAleatorio)
				+ ".txt";

		File file = null;
		try {
			String fichero_txt = ruta() + nombre_txt;
			String contenido_para_txt = "";
			contenido_para_txt = resultado_sai_estado_cuenta;

			file = new File(fichero_txt);
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(contenido_para_txt);
			bw.close();

		} catch (Exception e) {
			System.out.println("Error en crear estado de cuenta a TXT:" + e.getMessage());
		}
		return file;
	}

	// Creamos y llenamos el HTML
	public File crear_llenar_html(File file, String nombre) throws FileNotFoundException {
		String nombre_html = nombre;// =nombre_txt.replace(".txt",".html");
		String html = ruta() + nombre_html.replace(".txt", ".html");
		File file_html = new File(html);
		FileOutputStream fs = new FileOutputStream(file_html);
		OutputStreamWriter out = new OutputStreamWriter(fs);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			String linea_contenedor = "";
			int c = 0;
			while ((linea = br.readLine()) != null) {

				if (linea.contains("/usr/local/saicoop/img_estado_cuenta_ahorros/")) {
					String cade = ruta();
					linea = linea.replace("/usr/local/saicoop/img_estado_cuenta_ahorros/", cade.replace("\\", "/"));
				}
				if (linea.contains(" & ")) {
					linea = linea.replace(" & ", " y ");
				}
				if (linea.contains(".contenedor")) {
					c = c + 1;
					linea_contenedor = linea_contenedor + linea;
				}
				if (linea.contains("width:") && linea_contenedor.contains(".contenedor") && c <= 2) {
					String[] lineas = linea.split(":");
					String linea_despues_de_2puntos = lineas[1].replace(" ", "");
					linea_despues_de_2puntos = ":auto;";
					String linea_reconstruida = lineas[0] + linea_despues_de_2puntos;
					linea = linea_reconstruida;
				}
				if (linea.replace(" ", "").contains("height:") && linea_contenedor.contains(".contenedor") && c <= 2) {
					String[] lineas = linea.split(":");
					String linea_despues_de_2puntos = lineas[1].replace(" ", "");
					linea_despues_de_2puntos = ":auto;";
					String linea_reconstruida = lineas[0] + linea_despues_de_2puntos;
					linea = linea_reconstruida;
				}
				out.write(linea);
			}
			out.close();
		} catch (Exception e) {
			System.out.println("Excepcion leyendo txt" + ": " + e.getMessage());
		}
		return file_html;
	}

	public boolean crearPDF(String ruta, String nombreDelHTMLAConvertir) {
		try {
			// ruta donde esta el html a convertir
			String ficheroHTML = ruta + nombreDelHTMLAConvertir;

			String url = new File(ficheroHTML).toURI().toURL().toString();
			// ruta donde se almacenara el pdf y que nombre se le data
			String ficheroPDF = ruta + nombreDelHTMLAConvertir.replace(".html", ".pdf");
			File htmlSource = new File(ficheroHTML);
			File pdfDest = new File(ficheroPDF);
			// pdfHTML specific code
			ConverterProperties converterProperties = new ConverterProperties();
			HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest),
					converterProperties);
			return true;
		} catch (Exception e) {
			System.out.println("Error al crear PDF:" + e.getMessage());
			return false;
		}

	}
	
	public String validaciones_mitras(Auxiliares a) {
		
		return "";
	}

	// Parao obtener la ruta del servidor
	public static String ruta() {
		String home = System.getProperty("user.home");
		String separador = System.getProperty("file.separator");
		String actualRuta = home + separador + "Banca" + separador;
		return actualRuta;
	}
}
