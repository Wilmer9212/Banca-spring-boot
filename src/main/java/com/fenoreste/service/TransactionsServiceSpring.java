package com.fenoreste.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.consumo.IConsumosHTTPService;
import com.fenoreste.dao.TablasRepository;
import com.fenoreste.dto.BackendOperationResultDTO;
import com.fenoreste.dto.RequestDataOrdenPagoDTO;
import com.fenoreste.dto.ResponseSPEIDTO;
import com.fenoreste.dto.TransactionToOwnAccountsDTO;
import com.fenoreste.dto.VaucherDTO;
import com.fenoreste.dto.ogsDTO;
import com.fenoreste.dto.opaDTO;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Auxiliares_d;
import com.fenoreste.entity.FoliosTarjeta;
import com.fenoreste.entity.Origenes;
import com.fenoreste.entity.RegistraMovimiento;
import com.fenoreste.entity.Productos;
import com.fenoreste.entity.Tablas;
import com.fenoreste.entity.TiposCuentaBankingly;
import com.fenoreste.entity.Transferencias;
import com.fenoreste.util.HerramientasUtil;
import com.fenoreste.util.PreparaSMS;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.borders.InsetBorder;
import com.itextpdf.text.log.SysoLogger;

@Service
public class TransactionsServiceSpring {

	
	@Autowired
	HerramientasUtil util;
	@Autowired
	IOrigenesService origenesService;
	@Autowired
	ITablasService tablasService;
	@Autowired
	IAuxiliaresService auxiliaresService;
	@Autowired
	ISaiFuncionesService funcionesService;
	@Autowired
	IFoliosTarjetasService foliosTarjetasService;
	@Autowired
	IProductosService productosService;
	@Autowired
	ITiposCuentaBankinglyService cuentasBankinglyService;
	@Autowired
	IConsumosHTTPService consumosService;
	@Autowired
	IAuxiliares_dService auxiliares_dService;
	@Autowired
	IOtrosService otrosService;
	@Autowired
	IProcesaMovimientoService procesarMovimientosService;
	@Autowired
	PreparaSMS preparaSMS;
	@Autowired
	ITransferenciasService transferenciasService;

	public BackendOperationResultDTO transferencias(TransactionToOwnAccountsDTO transactionOWN,int identificadorTransferencia, RequestDataOrdenPagoDTO SPEIOrden) {
		Date hoy = new Date();
		BackendOperationResultDTO backendResponse = new BackendOperationResultDTO();
		backendResponse.setBackendCode("2");
		backendResponse.setBackendMessage("Incorrecto");
		backendResponse.setBackendReference(null);
		backendResponse.setIsError(true);
		backendResponse.setTransactionIdenty("0");
		opaDTO opaOrigen = util.opa(transactionOWN.getDebitProductBankIdentifier());
		opaDTO opaDestino = null;
		Auxiliares aux_origen = auxiliaresService.AuxiliarByOpa(opaOrigen.getIdorigenp(),opaOrigen.getIdproducto(),opaOrigen.getIdauxiliar());
		Auxiliares aux_destino = null;
		
		//Cuando ya se proceso la operacion solo busco el movimientos para obtener el numero de autorizacion 
		Auxiliares_d ad_origen=null;
		Auxiliares_d ad_destino=null;
		
		//Busco el origen 
		Origenes matriz=origenesService.findMatrizOrigen();

		Productos producto_destino = null;
		
		if(identificadorTransferencia !=5 ) {
		opaDestino= util.opa(transactionOWN.getCreditProductBankIdentifier());
	    aux_destino = auxiliaresService.AuxiliarByOpa(opaDestino.getIdorigenp(),opaDestino.getIdproducto(),opaDestino.getIdauxiliar());
	    producto_destino = productosService.findProductoById(aux_destino.getIdproducto());
		}
		
		boolean banderaCSN = false;
		ResponseSPEIDTO response = null;
		String messageBackend = "";
		String mensajeBackendResult = "";
		boolean validacion_mitras=false;
		boolean banderaTDD = false;
		String validacion_reglas_mitras = "";
		
		if(identificadorTransferencia == 1) {
			if(matriz.getIdorigen() == 30300) {
			validacion_reglas_mitras = validacionesMitras(transactionOWN, identificadorTransferencia);			
			System.out.println("Las validaciones para mitras fueron:"+validacion_reglas_mitras);
			if(validacion_reglas_mitras.toUpperCase().contains("EXITO")) {
				messageBackend = validarTransferenciaEntreMisCuentas(transactionOWN.getDebitProductBankIdentifier(),
						         transactionOWN.getAmount(), transactionOWN.getCreditProductBankIdentifier(),
						         transactionOWN.getClientBankIdentifier());
			}else {
			  messageBackend = validacion_reglas_mitras;	
			}
			validacion_mitras = true;
			}
			backendResponse.setBackendMessage(messageBackend);
			
		}else if (identificadorTransferencia == 2) {	
			if(matriz.getIdorigen() == 30300) {
			validacion_reglas_mitras = validacionesMitras(transactionOWN, identificadorTransferencia);			
			System.out.println("Las validaciones para mitras fueron:"+validacion_reglas_mitras);
			if(validacion_reglas_mitras.toUpperCase().contains("EXITO")) {
			messageBackend = validarTransferenciaATerceros(transactionOWN.getDebitProductBankIdentifier(),
					         transactionOWN.getAmount(), transactionOWN.getCreditProductBankIdentifier(),
					         transactionOWN.getClientBankIdentifier());
			}else {
			messageBackend = validacion_reglas_mitras;	
			}
			validacion_mitras = true;
			}
			backendResponse.setBackendMessage(messageBackend);		
		}
		// Si es pago a un prestamo
		if (identificadorTransferencia == 3 || identificadorTransferencia == 4) {
			if(matriz.getIdorigen() == 30300) {
				validacion_reglas_mitras = validacionesMitras(transactionOWN, identificadorTransferencia);			
				System.out.println("Las validaciones para mitras fueron:"+validacion_reglas_mitras);
				if(validacion_reglas_mitras.toUpperCase().contains("EXITO")) {
				messageBackend = validarPagoAPrestamos(identificadorTransferencia,transactionOWN.getDebitProductBankIdentifier(),
						         transactionOWN.getAmount(), transactionOWN.getCreditProductBankIdentifier(),
						         transactionOWN.getClientBankIdentifier());
				}else {
				messageBackend = validacion_reglas_mitras;	
				}
				validacion_mitras = true;
				}
				backendResponse.setBackendMessage(messageBackend);				
		}

		System.out.println("backendMessage:" + backendResponse.getBackendMessage());
		try {
			if (backendResponse.getBackendMessage().toUpperCase().contains("EXITO")) {

				Transferencias transaction = new Transferencias();
				// Si la valicadion se realizo de manera correcta preparo una tabl historial
				transaction.setTransactionid(new BigDecimal(transactionOWN.getTransactionId()));
				transaction.setSubtransactiontypeid(transactionOWN.getSubTransactionTypeId());
				transaction.setCurrencyid(transactionOWN.getCurrencyId());
				transaction.setValuedate(transactionOWN.getValueDate());
				transaction.setTransactiontypeid(transactionOWN.getTransactionTypeId());
				transaction.setTransactionstatusid(transactionOWN.getTransactionStatusId());
				transaction.setClientbankidentifier(transactionOWN.getClientBankIdentifier());
				transaction.setDebitproductbankidentifier(transactionOWN.getDebitProductBankIdentifier());
				transaction.setDebitproducttypeid(transactionOWN.getDebitProductTypeId());
				transaction.setDebitcurrencyid(transactionOWN.getDebitCurrencyId());
				transaction.setCreditproductbankidentifier(transactionOWN.getCreditProductBankIdentifier());
				transaction.setCreditproducttypeid(transactionOWN.getCreditProductTypeId());
				transaction.setCreditcurrencyid(transactionOWN.getCreditCurrencyId());
				transaction.setAmount(transactionOWN.getAmount());
				transaction.setNotifyto(transactionOWN.getNotifyTo());
				transaction.setNotificationchannelid(transactionOWN.getNotificationChannelId());
				transaction.setDestinationname(transactionOWN.getDestinationName());
				transaction.setDestinationbank(transactionOWN.getDestinationBank());
				transaction.setDescription(transactionOWN.getDescription());
				transaction.setBankroutingnumber(transactionOWN.getBankRoutingNumber());
				transaction.setSourcename(transactionOWN.getSourceName());
				transaction.setSourcebank(transactionOWN.getSourceBank());
				transaction.setRegulationamountexceeded(transactionOWN.isRegulationAmountExceeded());
				transaction.setSourcefunds(transactionOWN.getSourceFunds());
				transaction.setDestinationfunds(transactionOWN.getDestinationFunds());
				transaction.setTransactioncost(transactionOWN.getTransactionCost());
				transaction.setTransactioncostcurrencyid(transactionOWN.getTransactionCostCurrencyId());
				transaction.setExchangerate(transactionOWN.getExchangeRate());
				transaction.setDestinationdocumentid_documentnumber(transactionOWN.getDestinationDocumentId().getDocumentNumber());
				transaction.setDestinationdocumentid_documenttype(transactionOWN.getDestinationDocumentId().getDocumentType());
				transaction.setSourcedocumentid_documentnumber(transactionOWN.getSourceDocumentId().getDocumentNumber());
				transaction.setSourcedocumentid_documenttype(transactionOWN.getSourceDocumentId().getDocumentType());
				transaction.setUserdocumentid_documentnumber(transactionOWN.getUserDocumentId().getDocumentNumber());
				transaction.setUserdocumentid_documenttype(transactionOWN.getUserDocumentId().getDocumentType());
				transaction.setFechaejecucion(hoy);
				
				//Lo utilizo para los datos a procesar
				long time = System.currentTimeMillis();
				Timestamp timestamp = new Timestamp(time);
				// Obtengo la sesion para los datos a procesar				
				String sesion = otrosService.sesion();
				// Obtengo un random que uso como complemento para generar referencia
				int rn = (int) (Math.random() * 999999 + 1);
				
				// Obtener HH:mm:ss.microsegundos
				String fecha_posiciones[] = timestamp.toString().substring(0, 10).split("-");
				//Formateo yyyy/MM/dd
				String fecha_correcta = fecha_posiciones[2] + "/" + fecha_posiciones[1] + "/" + fecha_posiciones[0];
				String referencia = String.valueOf(rn) + "" + String.valueOf(transaction.getSubtransactiontypeid()) + ""+ String.valueOf(transaction.getTransactiontypeid() + "" + fecha_correcta.replace("/", ""));
				
				// Buscamos el usuario para la banca movil para la tabla de datos a procesar y para las polizas
				Tablas tb_usuario_banca_movil = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil", "usuario_banca_movil");
				// Convierto a DateTime la fecha de trabajo				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				LocalDateTime localDate = LocalDateTime.parse(matriz.getFechatrabajo().toString().substring(0,19), dtf);
				Timestamp fecha_transferencia = Timestamp.valueOf(localDate);				
								
				// Leo la tabla donde se almacenan datos temporales de los datos a procesar
				RegistraMovimiento registrar_movimiento_origen = new RegistraMovimiento();
				RegistraMovimiento registrar_movimiento_destino = new RegistraMovimiento();
				
				// Si es un spei
				if (identificadorTransferencia == 5) {// tipo de orden SPEI
					// Busco la cuenta spei en tablas solo capital
					Tablas tb_spei_cuenta = tablasService.findTabla("bankingly_banca_movil", "cuenta_spei");
					// Busco la cuenta spei en tablas solo para comisiones
					Tablas tb_spei_cuenta_comisiones = tablasService.findTabla("bankingly_banca_movil","cuenta_spei_comisiones");
					// Busco la cuenta spei en tablas la cuenta a donde se ira el iva spei
					Tablas tb_spei_cuenta_comisiones_iva = tablasService.findTabla("bankingly_banca_movil","cuenta_spei_comisiones_iva");

					Double comisiones = Double.parseDouble(tb_spei_cuenta_comisiones.getDato2());
					Double iva_comisiones = Double.parseDouble(tb_spei_cuenta_comisiones.getDato2()) * 0.16;

					// Preparo en temporales los datos para el cargo
					registrar_movimiento_origen.setIdorigenp(aux_origen.getIdorigenp());
					registrar_movimiento_origen.setIdproducto(aux_origen.getIdproducto());
					registrar_movimiento_origen.setIdauxiliar(aux_origen.getIdauxiliar());
					registrar_movimiento_origen.setFecha(fecha_transferencia);
					registrar_movimiento_origen.setIdusuario(Integer.parseInt(tb_usuario_banca_movil.getDato1()));
					registrar_movimiento_origen.setSesion(sesion);
					registrar_movimiento_origen.setReferencia(referencia);
					registrar_movimiento_origen.setIdorigen(aux_origen.getIdorigen());
					registrar_movimiento_origen.setIdgrupo(aux_origen.getIdgrupo());
					registrar_movimiento_origen.setIdsocio(aux_origen.getIdsocio());
					registrar_movimiento_origen.setCargoabono(0);
					registrar_movimiento_origen.setMonto(transaction.getAmount());

					registrar_movimiento_origen.setIva(Double.parseDouble(aux_origen.getIva().toString()));
					registrar_movimiento_origen.setTipo_amort(Integer.parseInt(String.valueOf(aux_origen.getTipoamortizacion())));
					registrar_movimiento_origen.setSai_aux("");
					
					//Guardamos el movimiento
					boolean procesado = procesarMovimientosService.insertarMovimiento(registrar_movimiento_origen);

					//El abono a cuenta contable que recibe solo capital de la transferencia spei
					
					registrar_movimiento_origen.setIdorigen(0);
					registrar_movimiento_origen.setIdproducto(1);
					registrar_movimiento_origen.setIdauxiliar(0);
					registrar_movimiento_origen.setFecha(fecha_transferencia);
					registrar_movimiento_origen.setIdusuario(Integer.parseInt(tb_usuario_banca_movil.getDato1()));
					registrar_movimiento_origen.setSesion(sesion);
					registrar_movimiento_origen.setReferencia(referencia);
					registrar_movimiento_origen.setIdorigen(aux_origen.getIdorigen());
					registrar_movimiento_origen.setIdgrupo(aux_origen.getIdgrupo());
					registrar_movimiento_origen.setIdsocio(aux_origen.getIdsocio());
					registrar_movimiento_origen.setCargoabono(1);
					registrar_movimiento_origen.setIdcuenta(tb_spei_cuenta.getDato1());// el idcuenta para capital
					registrar_movimiento_origen.setMonto(transaction.getAmount() - Double.parseDouble(tb_spei_cuenta_comisiones.getDato2())- (Double.parseDouble(tb_spei_cuenta_comisiones.getDato2()) * 0.16));// El total de la transferencia
					registrar_movimiento_origen.setIva(0.0);
					registrar_movimiento_origen.setTipo_amort(0);
					registrar_movimiento_origen.setSai_aux("");
					
					procesado = procesarMovimientosService.insertarMovimiento(registrar_movimiento_origen);
					
					// guardo el abono a la cuenta contable comisiones
					registrar_movimiento_origen.setIdorigen(1);
					registrar_movimiento_origen.setIdproducto(1);
					registrar_movimiento_origen.setIdauxiliar(1);
					registrar_movimiento_origen.setFecha(fecha_transferencia);
					registrar_movimiento_origen.setIdusuario(Integer.parseInt(tb_usuario_banca_movil.getDato1()));
					registrar_movimiento_origen.setSesion(sesion);
					registrar_movimiento_origen.setReferencia(referencia);
					registrar_movimiento_origen.setIdorigen(aux_origen.getIdorigen());
					registrar_movimiento_origen.setIdgrupo(aux_origen.getIdgrupo());
					registrar_movimiento_origen.setIdsocio(aux_origen.getIdsocio());
					registrar_movimiento_origen.setCargoabono(1);
					registrar_movimiento_origen.setIdcuenta(tb_spei_cuenta_comisiones.getDato1());
					registrar_movimiento_origen.setMonto(comisiones);
					registrar_movimiento_origen.setIva(0.0);
					registrar_movimiento_origen.setTipo_amort(0);
					registrar_movimiento_origen.setSai_aux("");
					
					procesado = procesarMovimientosService.insertarMovimiento(registrar_movimiento_origen);
					
					
					// Guardo el abono a la cuenta contable para comisiones de SPi
					registrar_movimiento_origen.setIdorigen(2);
					registrar_movimiento_origen.setIdproducto(1);
					registrar_movimiento_origen.setIdauxiliar(2);					
					registrar_movimiento_origen.setFecha(fecha_transferencia);
					registrar_movimiento_origen.setIdusuario(Integer.parseInt(tb_usuario_banca_movil.getDato1()));
					registrar_movimiento_origen.setSesion(sesion);
					registrar_movimiento_origen.setReferencia(referencia);
					registrar_movimiento_origen.setIdorigen(aux_origen.getIdorigen());
					registrar_movimiento_origen.setIdgrupo(aux_origen.getIdgrupo());
					registrar_movimiento_origen.setIdsocio(aux_origen.getIdsocio());
					registrar_movimiento_origen.setCargoabono(1);
					registrar_movimiento_origen.setIdcuenta(tb_spei_cuenta_comisiones_iva.getDato1());
					registrar_movimiento_origen.setMonto(iva_comisiones);
					registrar_movimiento_origen.setIva(0.0);
					registrar_movimiento_origen.setTipo_amort(0);
					registrar_movimiento_origen.setSai_aux("");
					
					

				} else {
					registrar_movimiento_origen.setIdorigenp(aux_origen.getIdorigenp());
					registrar_movimiento_origen.setIdproducto(aux_origen.getIdproducto());
					registrar_movimiento_origen.setIdauxiliar(aux_origen.getIdauxiliar());
					registrar_movimiento_origen.setFecha(fecha_transferencia);
					registrar_movimiento_origen.setIdusuario(Integer.parseInt(tb_usuario_banca_movil.getDato1()));
					registrar_movimiento_origen.setSesion(sesion);
					registrar_movimiento_origen.setReferencia(referencia);
					registrar_movimiento_origen.setIdorigen(aux_origen.getIdorigen());
					registrar_movimiento_origen.setIdgrupo(aux_origen.getIdgrupo());
					registrar_movimiento_origen.setIdsocio(aux_origen.getIdsocio());
					registrar_movimiento_origen.setCargoabono(0);
					registrar_movimiento_origen.setMonto(transaction.getAmount());
					registrar_movimiento_origen.setIva(aux_origen.getIva().doubleValue());
					registrar_movimiento_origen.setTipo_amort(aux_origen.getTipoamortizacion().intValue());
					registrar_movimiento_origen.setSai_aux("");
					
					procesarMovimientosService.insertarMovimiento(registrar_movimiento_origen);
					registrar_movimiento_destino.setIdorigenp(aux_destino.getIdorigenp());
					registrar_movimiento_destino.setIdproducto(aux_destino.getIdproducto());
					registrar_movimiento_destino.setIdauxiliar(aux_destino.getIdauxiliar());
					registrar_movimiento_destino.setFecha(fecha_transferencia);
					registrar_movimiento_destino.setIdusuario(Integer.parseInt(tb_usuario_banca_movil.getDato1()));
					registrar_movimiento_destino.setSesion(sesion);
					registrar_movimiento_destino.setReferencia(referencia);
					registrar_movimiento_destino.setIdorigen(aux_destino.getIdorigen());
					registrar_movimiento_destino.setIdgrupo(aux_destino.getIdgrupo());
					registrar_movimiento_destino.setIdsocio(aux_destino.getIdsocio());
					registrar_movimiento_destino.setCargoabono(1);
					registrar_movimiento_destino.setMonto(transaction.getAmount());
					registrar_movimiento_destino.setIva(aux_destino.getIva().doubleValue());
					registrar_movimiento_destino.setTipo_amort(aux_destino.getTipoamortizacion().intValue());
					registrar_movimiento_destino.setSai_aux("");
					
					procesarMovimientosService.insertarMovimiento(registrar_movimiento_destino);
					
				}
				
				Date fechaTrabajoDate = matriz.getFechatrabajo();//sdf.parse(origen.getFechatrabajo());
				String datos_procesar = "";
				int total_procesados = 0;
				boolean finish = false;
				boolean clean = false;
				
				// Si los datos en la tabla temporal el cargo y abono se guardo correctamente
				// Ejecutamos la funcion para distribuir el capital
				// Solo para CSN
				if (validacion_mitras) {
					try {
						datos_procesar = funcionesService.sai_aplica_transaccion(
								         fechaTrabajoDate,
								         registrar_movimiento_origen.getIdusuario(),
								         registrar_movimiento_origen.getSesion(),
								         registrar_movimiento_origen.getReferencia());
						                 total_procesados = Integer.parseInt(String.valueOf(datos_procesar));
					} catch (Exception e) {
						total_procesados = 0;
						System.out.println("La funcion saicoop no pudo distribuir el capital");
					}

					if (total_procesados > 0) {
						finish = true;
					} else {
						backendResponse.setBackendMessage("NO SE APLICO EL MOVIMIENTO FALLA AL PROCESAR MOVIMIENTOS EN SAICOOP");
					}
					
				}
				
				
				//Obtengo el numero de la transaccion registrada
				//Transaccion origen
				ad_origen=auxiliares_dService.findUltimoRegistro(opaOrigen.getIdorigenp(),opaOrigen.getIdproducto(),opaOrigen.getIdauxiliar());
				//Transaccion destino
				ad_destino=auxiliares_dService.findUltimoRegistro(opaDestino.getIdorigenp(),opaDestino.getIdproducto(),opaDestino.getIdauxiliar());
				
				if (finish){
					if (identificadorTransferencia != 5) {
						// Si fue un pago a prestamo propio o de Tercero
						if (producto_destino.getTipoproducto() == 2) {
							// Obtengo los datos(Seguro hipotecario,comisones cobranza,interes ect.) Me
							// muestra de que manera se distribuyo mi pago
							String distribucion = funcionesService.sai_detalle_transaccion_aplicada(
									registrar_movimiento_origen.getFecha(),
									registrar_movimiento_origen.getIdusuario(),
									registrar_movimiento_origen.getSesion(),
									registrar_movimiento_origen.getReferencia());
							
							
							// Guardo en una lista los montos que se han procesado
							String ArrayDistribucion[] = distribucion.split("\\|");		
							mensajeBackendResult = "<html>"
                                    				+ "<body>"
                                    						+ "<h1><center>PAGO EXITOSO</center></h1>"
                                    						+ "<table border=1 align=center cellspacing=0>"
                                    						+ "<tr><td>SEGURO HIPOTECARIO    </td><td>" + ArrayDistribucion[0] + " </td></tr>"
                                    						+ "<tr><td>COMISIÓN COBRANZA     </td><td>" + ArrayDistribucion[1] + " </td></tr>"
                                    						+ "<tr><td>INTERES MORATORIO     </td><td>" + ArrayDistribucion[2] + " </td></tr>"
                                    						+ "<tr><td>IVA INTERES MORATORIO </td><td>" + ArrayDistribucion[3] + " </td></tr>"
                                    						+ "<tr><td>INTERES ORDINARIO     </td><td>" + ArrayDistribucion[4] + " </td></tr>"
                                    						+ "<tr><td>IVA INTERES ORDINARIO </td><td>" + ArrayDistribucion[5] + " </td></tr>"
                                    						+ "<tr><td>CAPITAL               </td><td>" + ArrayDistribucion[6] + " </td></tr>"
                                    						+ "<tr><td>ADELANTO DE INTERES   </td><td>" + ArrayDistribucion[7] + " </td></tr>"
                                    						+ "<tr><td>AUT.ORIGEN			 </td><td>" + ad_origen.getTransaccion() + " </td></tr>"
                                    						+ "<tr><td>AUT.DESTINO			 </td><td>" + ad_destino.getTransaccion() + " </td></tr>"
                                    						+ "</table>"
                                    				+ "</body>"
                                    			    + "</html>";
						   clean = true;
						} else if (producto_destino.getTipoproducto() == 0) {
							mensajeBackendResult = "TRANSACCION EXITOSA";
							mensajeBackendResult = "<html>"
													 + "<body>"
													 + "<h1><center>TRANSFERENCIA EXITOSA</center></h1>"
													 + "<table border=1 align=center cellspacing=0>"
													 + "<tr><td>AUTORIZACION ORIGEN</td><td>" + ad_origen.getTransaccion() + "</td></tr>"
													 + "<tr><td>AUTORIZACION DESTINO</td><td>" + ad_destino.getTransaccion() + "</td></tr>"
													 + "</table>"
													 + "</body>"
												  + "</html>";
							clean = true;
						}
					}
				}

				if (clean) {
					// Aplico la funcion para limpiar la tabla donde estaban los pagos cargo y abono
					String termina_transaccion = funcionesService.sai_termina_transaccion(
							registrar_movimiento_origen.getFecha(),
							registrar_movimiento_origen.getIdusuario(),
							registrar_movimiento_origen.getSesion(),
							registrar_movimiento_origen.getReferencia());

					int registros_limpiados = Integer.parseInt(String.valueOf(termina_transaccion));
					System.out.println("Registros Limpiados con exito:" + registros_limpiados);

					String envio_ok_sms = "";

					if (origenesService.findMatrizOrigen().getNombre().replace(" ","").toUpperCase().contains("SANNICOLAS")) {
						Tablas tb_sms_activo = tablasService.findTabla("bankingly_banca_movil", "smsactivo");
						if (Integer.parseInt(tb_sms_activo.getDato1()) == 1) {
							// Obtengo el minimo para enviar el SMS
							Tablas tb_minimo_sms = tablasService.findTabla("bankingly_banca_movil", "monto_minimo_sms");
							if (transaction.getAmount() >= Double.parseDouble(tb_minimo_sms.getDato1())) {
								if (identificadorTransferencia == 1) {
									envio_ok_sms = preparaSMS.enviaSMS_CSN(String.valueOf(transaction.getAmount()),
											1, transaction.getDebitproductbankidentifier(),
											transaction.getCreditproductbankidentifier(),
											transaction.getClientbankidentifier());
								} else if (identificadorTransferencia == 2) {
									System.out.println("entro a enviar sms a cuenta de tercero");
									envio_ok_sms = preparaSMS.enviaSMS_CSN(String.valueOf(transaction.getAmount()),
											2, transaction.getDebitproductbankidentifier(),
											transaction.getCreditproductbankidentifier(),
											transaction.getClientbankidentifier());
								} else if (identificadorTransferencia == 3) {
									System.out.println("entro a enviar sms abono propio");
									envio_ok_sms = preparaSMS.enviaSMS_CSN(String.valueOf(transaction.getAmount()),
											3, transaction.getDebitproductbankidentifier(),
											transaction.getCreditproductbankidentifier(),
											transaction.getClientbankidentifier());
								} else if (identificadorTransferencia == 4) {
									System.out.println("entro a enviar sms abono tercero");
									envio_ok_sms =preparaSMS.enviaSMS_CSN(String.valueOf(transaction.getAmount()),
											4, transaction.getDebitproductbankidentifier(),
											transaction.getCreditproductbankidentifier(),
											transaction.getClientbankidentifier());
								} else if (identificadorTransferencia == 5) {
									System.out.println("entro a enviar sms SPEI salida");
									// Enviamos datos a preparar el sms indicando que debe obtener datos de mensaje
									// a cuenta propia
									// new PreparaSMS().enviaSMS_CSN(em, String.valueOf(transaction.getAmount()), 5,
									// transaction.getDebitproductbankidentifier(),
									// transaction.getCreditproductbankidentifier(),
									// transaction.getClientbankidentifier());
									envio_ok_sms =preparaSMS.enviaSMS_CSN(String.valueOf(transaction.getAmount()),
											5, transaction.getDebitproductbankidentifier(),
											transaction.getCreditproductbankidentifier(),
											transaction.getClientbankidentifier());
								}
							}
						}
					}
					
					
					if (envio_ok_sms.toUpperCase().contains("ERROR")) {
						backendResponse.setBackendMessage(backendResponse.getBackendMessage() + " " + envio_ok_sms);
					}
					backendResponse.setBackendCode("1");
					backendResponse.setBackendReference("0.2");
					backendResponse.setIsError(false);
					// Si fuera transferencia SPEI se devuelve el idTransaccion(Devuelvo el id de la
					// orden SPEI)
					if (identificadorTransferencia == 5) {
						// Guardo la orden
						backendResponse.setTransactionIdenty(String.valueOf(response.getId()));
					} else {
						backendResponse.setTransactionIdenty("0.0");
					}
					/*if(identificadorTransferencia <3) {
						mensajeBackendResult = "<html>"
	                             + "<body>"
	                             + "<h1><center>TRANSFERENCIA EXITOSA</center></h1>"
	                             + "<table border=1 align=center cellspacing=0>"
	                             + "<tr><td>AUTORIZACION ORIGEN</td><td>" + ad_origen.getTransaccion() + "</td></tr>"
	                             + "<tr><td>AUTORIZACION DESTINO</td><td>" + ad_destino.getTransaccion() + "</td></tr></table>"
	                             + "</table>"
	                             + "</body>"
	                             + "</html>";
					}*/
					
					
					backendResponse.setBackendMessage(mensajeBackendResult);
					backendResponse.setBackendReference("0.1");

					// Para que no se genere error
					try {
						// Guardo en una tabla el historial de la operacion realizada
						transaction.setTransactionid(new BigDecimal(registrar_movimiento_origen.getReferencia().trim()));
						transferenciasService.guardaTransferencia(transaction);
					} catch (Exception e) {
						System.out.println("Error al persistir:" + e.getMessage());
					}

				}

			}

		} catch (Exception e) {
			//if (backendResponse.getBackendMessage().contains("EXITO")) {
				backendResponse.setBackendMessage("Error:" + e.getMessage());
			/*} else {
				backendResponse.setBackendMessage(e.getMessage());
			}*/
			System.out.println("Error Al ejecutar transferencia:" + e.getMessage());
			return backendResponse;
		} 

		return backendResponse;
	}
	
	public VaucherDTO vaucher(String idtransaccion) {
		VaucherDTO voucher= new VaucherDTO();	
		System.out.println("Llengando a bases de datos");
		try {
	    Auxiliares_d ad=auxiliares_dService.findByTransaccion(Integer.parseInt(idtransaccion));
		File file_html = construirHtmlVoucher(ad);
		
		if (crearPDF(ruta(),file_html.getName())) {
			String rutpdf =ruta()+file_html.getName().replace(".html",".pdf");
			byte[] input_file = Files.readAllBytes(Paths.get(rutpdf));
            byte[] encodedBytesFile = Base64.getEncoder().encode(input_file);
			voucher.setProductBankStatementFileName(file_html.getName().replace(".html",".pdf"));	
			voucher.setProductBankStatementFile(encodedBytesFile);
			
			file_html.delete();
		}
		} catch (Exception e) {
		  System.out.println("Error al formar el vaucher:"+e.getMessage());
		}
		return voucher;
		
	}
	
	
	
	//validaciones Mitras
	public String validacionesMitras(TransactionToOwnAccountsDTO transactionOWN,int identificadorTransferencia) {		 
		String mensaje="";
		
		//Formateando opa origen
		opaDTO opaOrigen = util.opa(transactionOWN.getDebitProductBankIdentifier());
		//Formateando opa destino
		opaDTO opaDestino = util.opa(transactionOWN.getCreditProductBankIdentifier());
		//Buscando folio origen
		Auxiliares aux_origen = auxiliaresService.findAuxiliaresByOPA(opaOrigen.getIdorigenp(),opaOrigen.getIdproducto(), opaOrigen.getIdauxiliar());
		//Buscando folio destino
		Auxiliares aux_destino = auxiliaresService.findAuxiliaresByOPA(opaDestino.getIdorigenp(),opaDestino.getIdproducto(), opaDestino.getIdauxiliar());
        
		
		Tablas tb_producto_retiro=null;
		Tablas tb_grupos_retiro=null;		
		Tablas tb_grupos_deposito=null;		
		Tablas tb_productos_deposito=null;
		
		boolean bandera_grupo_retiro=false;
		boolean bandera_grupo_deposito=false;
		boolean bandera_productos_deposito=false;
		boolean bandera_maximo_a_operar = false;
		try {
		if(aux_origen != null) {
			 // Busco el producto configurado para retiros
			 tb_producto_retiro = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil", "producto_retiro");			
			 //tb_grupos_retiro= tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","grupo_retiro");//Buscamos que el grupo destino permita depositos
			 //tb_grupos_deposito= tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","grupo_deposito");//valido que el producto acepte depositos
			 //tb_productos_deposito= tablasService.findIdtablaAndIdelemento("bankingly_banca_movil", "productos_deposito");
			 
			
		// Si el producto origen es el configurado para retiros
		if (aux_origen.getIdproducto() == Integer.parseInt(tb_producto_retiro.getDato1())) {  
			//Busco el producto en tablas para validar el maximo
			Tablas tb_productos_maximo = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","maximo_operar_captacion");
			String array_productos_captacion = tb_productos_maximo.getDato2();
			String cadena_productos[]=array_productos_captacion.split("\\|");
			for(int i=0;i<cadena_productos.length;i++) {
			   if(aux_destino.getIdproducto() == Integer.parseInt(cadena_productos[i])) {
				   if((aux_destino.getSaldo().doubleValue()+transactionOWN.getAmount()) <= Double.parseDouble(tb_productos_maximo.getDato1())){
					   bandera_maximo_a_operar = true;
				   }
			   }
			}
			System.out.println("LA transferencia es:"+identificadorTransferencia);
			    if (identificadorTransferencia == 1) {
			    	if(bandera_maximo_a_operar) {
					 mensaje = validarTransferenciaEntreMisCuentas(
							  transactionOWN.getDebitProductBankIdentifier(),
							  transactionOWN.getAmount(),
							  transactionOWN.getCreditProductBankIdentifier(),
							  transactionOWN.getClientBankIdentifier());
			    	}else {
			    	 mensaje = "EL MAXIMO PARA OPERAR EN EL PRODUCTO SOBREPASA AL PERMITIDO";	
			    	}
				} else if (identificadorTransferencia == 2) {
					System.out.println("enroketotkeorkt");
  				    mensaje = validarTransferenciaATerceros(
					          transactionOWN.getDebitProductBankIdentifier(),
					   		  transactionOWN.getAmount(),
							  transactionOWN.getCreditProductBankIdentifier(),
							  transactionOWN.getClientBankIdentifier());
				} else if(identificadorTransferencia == 3 || identificadorTransferencia == 4) {					
				    mensaje = validarPagoAPrestamos(identificadorTransferencia,
					   		  transactionOWN.getDebitProductBankIdentifier(),
							  transactionOWN.getAmount(),
							  transactionOWN.getCreditProductBankIdentifier(),
							  transactionOWN.getClientBankIdentifier());
			   }						
		     }else {
		        mensaje = " PRODUCTO NO CONIGURADO PARA RETIROS";
		    }	
		  }else {
			mensaje = "PRODUCTO ORIGEN NO EXISTE";
		 }
      }catch (Exception e) {
		 System.out.println("Error al validar operacion:"+e.getMessage()); 
	  }
		return mensaje.toUpperCase();
	}
	
	// Metodo solo para CSN aplicando reglas
	public String metodosTransferenciaCSN(TransactionToOwnAccountsDTO transactionOWN, int identificadorTransferencia,
			RequestDataOrdenPagoDTO SPEIOrden) throws ParseException {
		String mensaje = "";
    	boolean bandera_grupo_deposito = false;
		boolean bandera_grupo_retiro = false;
		boolean bandera_producto_deposito = false;

		FoliosTarjeta folio_tarjeta_origen = null;		
		Tablas tb_producto_tdd =null;		
		Tablas tb_grupos_retiro = null;		
		Tablas tb_grupos_deposito = null;		
		Tablas tb_producto_deposito = null;
		
		Tablas tb_producto_retiro= null;
		 
		// Bandera que me sirve para decir si existe o no la tdd
		opaDTO opaOrigen = util.opa(transactionOWN.getDebitProductBankIdentifier());
		opaDTO opaDestino = util.opa(transactionOWN.getCreditProductBankIdentifier());
		Auxiliares aux_origen = auxiliaresService.findAuxiliaresByOPA(opaOrigen.getIdorigenp(),opaOrigen.getIdproducto(), opaOrigen.getIdauxiliar());
		Auxiliares aux_destino = auxiliaresService.findAuxiliaresByOPA(opaDestino.getIdorigenp(),opaDestino.getIdproducto(), opaDestino.getIdauxiliar());
         
		
		if(aux_origen != null) {
			 // Busco el producto configurado para retiros
			 tb_producto_retiro = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil", "producto_retiro");
			 tb_producto_tdd = tablasService.findIdtablaAndIdelemento("bankingly_banca_movil", "producto_tdd");
			
			 tb_grupos_retiro= tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","grupo_retiro");
			 // Buscamos que el grupo destino permita depositos
			 tb_grupos_deposito= tablasService.findIdtablaAndIdelemento("bankingly_banca_movil","grupo_deposito");
			 // valido que el producto acepte depositos
			 tb_producto_deposito= tablasService.findIdtablaAndIdelemento("bankingly_banca_movil", "productos_deposito");
			
		// Si el producto origen es el configurado para retiros
		if (aux_origen.getIdproducto() == Integer.parseInt(tb_producto_retiro.getDato1())) {					
			// Si se esta retirando de la tdd	
			// Buscamos que el producto origen pertenezca al grupo de retiro
			
			if (Integer.parseInt(tb_producto_tdd.getDato1())== aux_origen.getIdproducto()) {				
				    // Verifico que la activar tdd este en 1--activado
				System.out.println("DEtecto a la tdd");
					Tablas activa_tdd = tablasService.findTabla("bankingly_banca_movil", "activa_tdd");
					if (Integer.parseInt(activa_tdd.getDato2()) == 1) {
						folio_tarjeta_origen = foliosTarjetasService.findFolioByOpa(opaOrigen.getIdorigenp(),opaOrigen.getIdproducto(), opaOrigen.getIdauxiliar());
						// si se encontro la Tarjeta de debito
						if (folio_tarjeta_origen != null) {
							// Si la tarjeta esta activa
							if (folio_tarjeta_origen.getActiva()) {
								// Consumo el saldo de la TDD desde el ws de alestra
								String saldo = consumosService.getBalanceQuery(folio_tarjeta_origen.getIdtarjeta());
								Double saldo_tdd = Double.parseDouble(saldo);
								if (saldo_tdd > transactionOWN.getAmount()) {////////////////									
									String grupos_retiro[] = tb_grupos_retiro.getDato1().split("\\|");
									List lista_grupos_retiro = Arrays.asList(grupos_retiro);
									for (int i = 0; i < lista_grupos_retiro.size(); i++) {
										if (aux_origen.getIdgrupo() == Integer
												.parseInt(String.valueOf(lista_grupos_retiro.get(i)))) {
											bandera_grupo_retiro = true;
										}
									}
									if (bandera_grupo_retiro) {
										String grupos_deposito[] = tb_grupos_deposito.getDato1().split("\\|");
										List lista_grupos_deposito = Arrays.asList(grupos_deposito);
										for (int i = 0; i < lista_grupos_deposito.size(); i++) {
											if (aux_destino.getIdgrupo() == Integer
													.parseInt(String.valueOf(lista_grupos_deposito.get(i)))) {
												bandera_grupo_deposito = true;
											}
										}
										if (bandera_grupo_deposito) {
											String productos_deposito[] = tb_producto_deposito.getDato2().split("\\|");
											List lista_productos_deposito = Arrays.asList(productos_deposito);
											for (int i = 0; i < lista_productos_deposito.size(); i++) {
												if (aux_destino.getIdproducto() == Integer
														.parseInt(String.valueOf(lista_productos_deposito.get(i)))) {
													bandera_producto_deposito = true;
												}
											}
											if (bandera_producto_deposito) {
												if (identificadorTransferencia == 1) {
													//Como es TDD y el saldo ya se valido desde el web service le paso -1 para que asi aunque la cuenta tenga 0 pasara esa validacion
													mensaje = validarTransferenciaEntreMisCuentas(
															transactionOWN.getDebitProductBankIdentifier(),
															-1.0,
															transactionOWN.getCreditProductBankIdentifier(),
															transactionOWN.getClientBankIdentifier())+"TDD";
												} else if (identificadorTransferencia == 2) {
													 //Deposito en pesos mexicanos menores
			                                        Tablas tb_menores_permitido_diario = tablasService.findTabla("bankingly_banca_movil", "total_deposito_diario_menores");
			                                        //Total en udis
			                                        Tablas tb_menores_udis_mensual = tablasService.findTabla("bankingly_banca_movil", "total_udis_mensual_menores");
			                                        //Deposito en pesos mexicanos juveniles
			                                        Tablas tb_juveniles_permitido_diario = tablasService.findTabla("bankingly_banca_movil", "total_deposito_diario_juveniles");
			                                        //Deposito en udis juveniles
			                                        Tablas tb_juveniles_udis_mensual = tablasService.findTabla("bankingly_banca_movil", "total_udis_mensual_juveniles");
			                                        //Obtengo la fecha de trabajo
			                                        String fecha_trabajo = origenesService.fechaTrabajo();

			                                        //tb_precio_udi en el periodo
			                                        Tablas tb_precio_udi_periodo = tablasService.findTabla("valor_udi", fecha_trabajo.substring(0, 7).replace("\\/", ""));
			                                        SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");
			                                        Date fecha_trabajo_= sdf.parse(fecha_trabajo);
			                                        double monto_diario = auxiliares_dService.montoPesos(aux_destino.getIdorigen(),aux_destino.getIdgrupo(),aux_destino.getIdsocio(),fecha_trabajo_);
			                                        double monto_saldo_en_el_periodo = auxiliares_dService.montoUdis(aux_destino.getIdorigen(),aux_destino.getIdgrupo(),aux_destino.getIdsocio(),fecha_trabajo.substring(0, 7).replace("\\/", ""));
			                                        double total_udis_periodo=monto_saldo_en_el_periodo * Double.parseDouble(tb_precio_udi_periodo.getDato1());
			                                        
			                                        //Si el grupo es un menor
			                                        if (aux_destino.getIdgrupo() == 20) {
			                                            //Busco si lo que se le permite a un menor diario no es mayor a lo que hay en la tabla  			                                            
			                                            if ((monto_diario + transactionOWN.getAmount()) <= Double.parseDouble(tb_menores_permitido_diario.getDato1())) {
			                                                //Ahora busco todos los movimientos para saber el total de udis
			                                                if (total_udis_periodo <= Double.parseDouble(tb_menores_udis_mensual.getDato1())) {
			                                                	mensaje = validarTransferenciaATerceros(
																		transactionOWN.getDebitProductBankIdentifier(),
																		-1.0,
																		transactionOWN.getCreditProductBankIdentifier(),
																		transactionOWN.getClientBankIdentifier())+"TDD";																
			                                                } else {
			                                                    mensaje = "MONTO EN UDIS TRASPASA AL PERMITIDO POR MES";
			                                                }
			                                             } else {
			                                                mensaje = "EL MONTO DIARIO TRASPASA AL PERMITIDO";
			                                            }

			                                        } else if (aux_destino.getIdgrupo() == 25) {//Si es un juvenil
			                                            //Busco si lo que se le permite a un menor dirario no es mayor a lo que hay en la tabla 			                                            
			                                            if ((monto_diario + transactionOWN.getAmount()) <= Double.parseDouble(tb_juveniles_permitido_diario.getDato1())) {
			                                                //Ahora busco todos los movimientos para saber el total de udis			                                                
			                                                if (total_udis_periodo <= Double.parseDouble(tb_juveniles_udis_mensual.getDato1())) {
			                                                	mensaje = validarTransferenciaATerceros(
																		transactionOWN.getDebitProductBankIdentifier(),
																		-1.0,
																		transactionOWN.getCreditProductBankIdentifier(),
																		transactionOWN.getClientBankIdentifier())+"TDD";	
			                                              } else {
			                                                    mensaje = "MONTO EN UDIS TRASPASA AL PERMITIDO POR MES";
			                                              }
			                                           } else {
			                                                mensaje = "EL MONTO DIARIO TRASPASA AL PERMITIDO";
			                                          }	
			                                       } else {
			                                    	   //si no es grupo menor ni juvenil entonces es grupo 10 y verifio si es una tdd tercero
			                                    	   if(aux_destino.getIdproducto() == Integer.parseInt(tb_producto_tdd.getDato1())){
			                                    		  //Busco el folio para la tdd destino
			                                    		  FoliosTarjeta tarjeta_destino = foliosTarjetasService.findFolioByOpa(aux_destino.getIdorigenp(), aux_destino.getIdproducto(), aux_destino.getIdauxiliar());
			                                              if(tarjeta_destino != null) {
			                                            	  if(tarjeta_destino.getActiva()) {
			                                            		  mensaje = validarTransferenciaATerceros(
																            transactionOWN.getDebitProductBankIdentifier(),
																			-1.0,
																			transactionOWN.getCreditProductBankIdentifier(),
																			transactionOWN.getClientBankIdentifier())+"TDD TERCERO";
			                                            	  }else {
			                                            	   mensaje = "PRODUCTO DESTINO TARJETA NO ESTA ACITVO";
			                                            	  }			                                            	  
			                                              } else {
			                                            	mensaje = "PRODUCTO DESTINO TARJETA NO EXISTE";  	
			                                              }
			                                           }else {
			                                        	   mensaje = validarTransferenciaATerceros(
														            transactionOWN.getDebitProductBankIdentifier(),
																	-1.0,
																	transactionOWN.getCreditProductBankIdentifier(),
																	transactionOWN.getClientBankIdentifier());
			                                           }
			                                            
			                                       }
												} else if (identificadorTransferencia == 3 || identificadorTransferencia == 4) {
													mensaje = validarPagoAPrestamos(identificadorTransferencia,
															transactionOWN.getDebitProductBankIdentifier(),
															transactionOWN.getAmount(),
															transactionOWN.getCreditProductBankIdentifier(),
															transactionOWN.getClientBankIdentifier());
												} else {
													//mensaje = validaOrdenSPEI(SPEIOrden);
												}
											} else {
												mensaje = "PRODUCTO NO CONFIGURADO PARA RECIBIR DEPOSITOS";
											}
										} else {
											mensaje = "GRUPO NO CONFIGURADO PARA DEPOSITOS";
										}
									} else {
										mensaje = "SOCIO NO PERTENECE AL GRUPO DE RETIRO";
									}
								} else {
									mensaje = "SALDO INSUFICIENTE PARA COMPLETAR LA OPERACION";
								}
							} else {
								mensaje = "ESTATUS TARJETA DE DEBITO:INACTIVA";
							}
						} else {
							mensaje = "NO EXISTE FOLIO PARA LA TARJETA DE DEBITO";
						}
					} else {
						mensaje = "POR FAVOR SOLICITE QUE SE ACTIVE EL USO DE TARJETA DE DEBITO";
					}
			} else {///////Termina la tdd
				// Solo para pruebas
				System.out.println("Es prueba:"+tb_grupos_retiro);
				String grupos_retiro[] = tb_grupos_retiro.getDato1().split("\\|");
				List lista_grupos_retiro = Arrays.asList(grupos_retiro);
				for (int i = 0; i < lista_grupos_retiro.size(); i++) {
					if (aux_origen.getIdgrupo() == Integer
							.parseInt(String.valueOf(lista_grupos_retiro.get(i)))) {
						bandera_grupo_retiro = true;
					}
				}
				System.out.println("Paso a pruebas verificando el grupo de retiro:"+tb_grupos_deposito);
				if (bandera_grupo_retiro) {
					String grupos_deposito[] = tb_grupos_deposito.getDato1().split("\\|");
					List lista_grupos_deposito = Arrays.asList(grupos_deposito);
					for (int i = 0; i < lista_grupos_deposito.size(); i++) {
						if (aux_destino.getIdgrupo() == Integer
								.parseInt(String.valueOf(lista_grupos_deposito.get(i)))) {
							bandera_grupo_deposito = true;
						}
					}
					if (bandera_grupo_deposito) {
						String productos_deposito[] = tb_producto_deposito.getDato2().split("\\|");
						List lista_productos_deposito = Arrays.asList(productos_deposito);
						for (int i = 0; i < lista_productos_deposito.size(); i++) {
							if (aux_destino.getIdproducto() == Integer
									.parseInt(String.valueOf(lista_productos_deposito.get(i)))) {
								bandera_producto_deposito = true;
							}
						}
						if (bandera_producto_deposito) {
							if (identificadorTransferencia == 1) {
								//Como es TDD y el saldo ya se valido desde el web service le paso -1 para que asi aunque la cuenta tenga 0 pasara esa validacion
								mensaje = validarTransferenciaEntreMisCuentas(
										transactionOWN.getDebitProductBankIdentifier(),
										transactionOWN.getAmount(),
										transactionOWN.getCreditProductBankIdentifier(),
										transactionOWN.getClientBankIdentifier())+"TDD";
							} else if (identificadorTransferencia == 2) {
								 //Deposito en pesos mexicanos menores
                                Tablas tb_menores_permitido_diario = tablasService.findTabla("bankingly_banca_movil", "total_deposito_diario_menores");
                                //Total en udis
                                Tablas tb_menores_udis_mensual = tablasService.findTabla("bankingly_banca_movil", "total_udis_mensual_menores");
                                //Deposito en pesos mexicanos juveniles
                                Tablas tb_juveniles_permitido_diario = tablasService.findTabla("bankingly_banca_movil", "total_deposito_diario_juveniles");
                                //Deposito en udis juveniles
                                Tablas tb_juveniles_udis_mensual = tablasService.findTabla("bankingly_banca_movil", "total_udis_mensual_juveniles");
                                //Obtengo la fecha de trabajo
                                String fecha_trabajo = origenesService.fechaTrabajo();

                                //tb_precio_udi en el periodo
                                Tablas tb_precio_udi_periodo = tablasService.findTabla("valor_udi", fecha_trabajo.substring(0, 7).replace("\\/", ""));
                                SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");
                                Date fecha_trabajo_= sdf.parse(fecha_trabajo);
                                double monto_diario = auxiliares_dService.montoPesos(aux_destino.getIdorigen(),aux_destino.getIdgrupo(),aux_destino.getIdsocio(),fecha_trabajo_);
                                double monto_saldo_en_el_periodo = auxiliares_dService.montoUdis(aux_destino.getIdorigen(),aux_destino.getIdgrupo(),aux_destino.getIdsocio(),fecha_trabajo.substring(0, 7).replace("\\/", ""));
                                double total_udis_periodo=monto_saldo_en_el_periodo * Double.parseDouble(tb_precio_udi_periodo.getDato1());
                                
                                //Si el grupo es un menor
                                if (aux_destino.getIdgrupo() == 20) {
                                    //Busco si lo que se le permite a un menor diario no es mayor a lo que hay en la tabla  			                                            
                                    if ((monto_diario + transactionOWN.getAmount()) <= Double.parseDouble(tb_menores_permitido_diario.getDato1())) {
                                        //Ahora busco todos los movimientos para saber el total de udis
                                        if (total_udis_periodo <= Double.parseDouble(tb_menores_udis_mensual.getDato1())) {
                                        	mensaje = validarTransferenciaATerceros(
													transactionOWN.getDebitProductBankIdentifier(),
													transactionOWN.getAmount(),
													transactionOWN.getCreditProductBankIdentifier(),
													transactionOWN.getClientBankIdentifier())+"TDD";																
                                        } else {
                                            mensaje = "MONTO EN UDIS TRASPASA AL PERMITIDO POR MES";
                                        }
                                     } else {
                                        mensaje = "EL MONTO DIARIO TRASPASA AL PERMITIDO";
                                    }

                                } else if (aux_destino.getIdgrupo() == 25) {//Si es un juvenil
                                    //Busco si lo que se le permite a un menor dirario no es mayor a lo que hay en la tabla 			                                            
                                    if ((monto_diario + transactionOWN.getAmount()) <= Double.parseDouble(tb_juveniles_permitido_diario.getDato1())) {
                                        //Ahora busco todos los movimientos para saber el total de udis			                                                
                                        if (total_udis_periodo <= Double.parseDouble(tb_juveniles_udis_mensual.getDato1())) {
                                        	mensaje = validarTransferenciaATerceros(
													transactionOWN.getDebitProductBankIdentifier(),
													transactionOWN.getAmount(),
													transactionOWN.getCreditProductBankIdentifier(),
													transactionOWN.getClientBankIdentifier())+"TDD";	
                                      } else {
                                            mensaje = "MONTO EN UDIS TRASPASA AL PERMITIDO POR MES";
                                      }
                                   } else {
                                        mensaje = "EL MONTO DIARIO TRASPASA AL PERMITIDO";
                                  }	
                               } else {
                            	   //si no es grupo menor ni juvenil entonces es grupo 10 y verifio si es una tdd tercero
                            	   if(aux_destino.getIdproducto() == Integer.parseInt(tb_producto_tdd.getDato1())){
                            		  //Busco el folio para la tdd destino
                            		  FoliosTarjeta tarjeta_destino = foliosTarjetasService.findFolioByOpa(aux_destino.getIdorigenp(), aux_destino.getIdproducto(), aux_destino.getIdauxiliar());
                                      if(tarjeta_destino != null) {
                                    	  if(tarjeta_destino.getActiva()) {
                                    		  mensaje = validarTransferenciaATerceros(
											            transactionOWN.getDebitProductBankIdentifier(),
											            transactionOWN.getAmount(),
														transactionOWN.getCreditProductBankIdentifier(),
														transactionOWN.getClientBankIdentifier())+"TDD TERCERO";
                                    	  }else {
                                    	   mensaje = "PRODUCTO DESTINO TARJETA NO ESTA ACITVO";
                                    	  }			                                            	  
                                      } else {
                                    	mensaje = "PRODUCTO DESTINO TARJETA NO EXISTE";  	
                                      }
                                   }else {
                                	   mensaje = validarTransferenciaATerceros(
									            transactionOWN.getDebitProductBankIdentifier(),
												transactionOWN.getAmount(),
												transactionOWN.getCreditProductBankIdentifier(),
												transactionOWN.getClientBankIdentifier());
                                   }
                                    
                               }
							} else if (identificadorTransferencia == 3 || identificadorTransferencia == 4) {
								System.out.println("Ya paso hasta aqui");
								mensaje = validarPagoAPrestamos(identificadorTransferencia,
										transactionOWN.getDebitProductBankIdentifier(),
										transactionOWN.getAmount(),
										transactionOWN.getCreditProductBankIdentifier(),
										transactionOWN.getClientBankIdentifier());
							} else {
								//mensaje = validaOrdenSPEI(SPEIOrden);
							}
						} else {
							mensaje = "PRODUCTO NO CONFIGURADO PARA RECIBIR DEPOSITOS";
						}
					} else {
						mensaje = "GRUPO NO CONFIGURADO PARA DEPOSITOS";
					}
				} else {
					mensaje = "SOCIO NO PERTENECE AL GRUPO DE RETIRO";
				}
			}			
		}else {
		   mensaje = " PRODUCTO NO CONIGURADO PARA RETIROS";
		}	
		}else {
			mensaje = "PRODUCTO ORIGEN NO EXISTE";
		}
		
		return mensaje.toUpperCase();
	}

	public String validarTransferenciaEntreMisCuentas(String opaOrigen, Double monto, String opaDestino,String clientBankIdentifier) {
		opaDTO opaO = util.opa(opaOrigen);
		opaDTO opaD = util.opa(opaDestino);
		
		ogsDTO ogs = util.ogs(clientBankIdentifier);
		Auxiliares aux_origen = auxiliaresService.findAuxiliaresByOPA(opaO.getIdorigenp(), opaO.getIdproducto(),opaO.getIdauxiliar());
		Auxiliares aux_destino = auxiliaresService.findAuxiliaresByOPA(opaD.getIdorigenp(), opaD.getIdproducto(),opaD.getIdauxiliar());
		
		String message = "";
		
		try {
			//Si existe el auxiliar origen en tabla auxiliares
			if (aux_origen != null) {				
				//Busco descripcion del idproducto origen
				Productos producto_origen = productosService.findProductoById(aux_origen.getIdproducto());
				// Valido que el producto origen se trabaje en banca movil
				TiposCuentaBankingly cuenta_bankingly_origen = cuentasBankinglyService.findTipoCuentaById(aux_origen.getIdproducto());
				if (cuenta_bankingly_origen != null) {
					//Me aseguro que se intenta retirar de un producto tipo 0
					if (producto_origen.getTipoproducto() == 0) {
						if (aux_origen.getEstatus() == 2) {
							if (aux_origen.getSaldo().doubleValue() >= monto) {
								if (aux_destino != null) {
									if (aux_destino.getEstatus() == 2) {
										// Valido que producto destino opera para banca movil
										TiposCuentaBankingly cuenta_bankingly_destino = cuentasBankinglyService.findTipoCuentaById(aux_destino.getIdproducto());
										if (cuenta_bankingly_destino != null) {
											// Busco el producto destino
											Productos producto_destino = productosService.findProductoById(aux_destino.getIdproducto());
											// Valido que el producto destino no sea un prestamo
											if (producto_destino.getTipoproducto() != 2) {
												// Valido que realmente el el producto destino pertenezca al mismo socio
												// por ser transferencia entre mis cuentas												
												if (Integer.parseInt(aux_origen.getIdorigen().toString()) == Integer.parseInt(aux_destino.getIdorigen().toString())&&
													Integer.parseInt(aux_origen.getIdgrupo().toString()) == Integer.parseInt(aux_destino.getIdgrupo().toString())&&
													Integer.parseInt(aux_origen.getIdsocio().toString()) == Integer.parseInt(aux_destino.getIdsocio().toString())) {
													message = "VALIDADO CON EXITO";
												} else {
													message = "PRODUCTO DESTINO NO PERTENECE AL MISMO SOCIO";
												}
											} else {
												message = "PRODUCTO DESTINO SOLO ACEPTA PAGOS(PRESTAMO)";
											}
										} else {
											message = "PRODUCTO DESTINO NO OPERA PARA BANCA MOVIL";
										}
									} else {
										message = "PRODUCTO DESTINO ESTA INACTIVO";
									}
								} else {
									message = "NO SE ENCONTRO PRODUCTO DESTINO";
								}
							} else {
								message = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
							}
						} else {
							message = "PRODUCTO ORIGEN INACTIVO";
						}

					} else {
						message = "PRODUCTO ORIGEN NO PERMITE RETIROS";
					}
				} else {
					message = "PRODUCTO ORIGEN NO OPERA PARA BANCA MOVIL";
				}
			} else {
				message = "PRODUCTO ORIGEN NO PERTENECE AL SOCIO:" + clientBankIdentifier;
			}

		} catch (Exception e) {
			message = "ERROR AL PROCESAR CONSULTA VALIDACIONES DE DATOS";
			System.out.println("Error en validacion transferencia entre mis cuentas:" + e.getMessage());
			return message;
		}
		return message.toUpperCase();
	}
			
	//Metodo para validar transferencia a otras cuentas
    public String validarTransferenciaATerceros(String opaOrigen, Double monto, String opaDestino, String clientBankIdentifier) {  
        opaDTO opaO = util.opa(opaOrigen);
        opaDTO opaD = util.opa(opaDestino);
        ogsDTO ogs = util.ogs(clientBankIdentifier);
        
        Auxiliares aux_origen = auxiliaresService.findAuxiliaresByOPA(opaO.getIdorigenp(),opaO.getIdproducto(),opaO.getIdauxiliar());
        Auxiliares aux_destino = auxiliaresService.findAuxiliaresByOPA(opaD.getIdorigenp(),opaD.getIdproducto(),opaD.getIdauxiliar());
        String message = "";
        
        if (aux_origen != null) {
                Double saldo = Double.parseDouble(aux_origen.getSaldo().toString());    
                //Busco descripcion del idproducto origen
                Productos producto_origen = productosService.findProductoById(aux_origen.getIdproducto());
                //Valido que el producto origen se trabaje en banca movil
                TiposCuentaBankingly tipo_cuenta_bankingly_origen = cuentasBankinglyService.findTipoCuentaById(aux_origen.getIdproducto());
                if (tipo_cuenta_bankingly_origen != null) {
                    //si el producto no es un prestamo            
                    if (producto_origen.getTipoproducto() != 2) {
                        //Verifico el estatus de la cuenta origen
                        if (aux_origen.getEstatus() == 2) {
                            //verifico que el saldo del producto origen es mayor o igual a lo que se intenta transferir
                            if (saldo >= monto) {                                
                                if (aux_destino != null) {
                                    //Busco el producto destino
                                    Productos producto_destino = productosService.findProductoById(aux_destino.getIdproducto());
                                    //Valido que la cuenta destino este activa
                                    if (aux_destino.getEstatus() == 2) {
                                        //Busco si existe el producto destino en el catalogo de banca movil
                                        TiposCuentaBankingly tipo_cuenta_bankingly_destino = cuentasBankinglyService.findTipoCuentaById(aux_destino.getIdproducto());
                                        if (tipo_cuenta_bankingly_destino != null) {
                                        	System.out.println(aux_origen.getIdorigen()+"|"+aux_destino.getIdorigen().toString());
                                        	System.out.println(aux_origen.getIdgrupo()+"|"+aux_destino.getIdgrupo().toString());
                                        	System.out.println(aux_origen.getIdsocio()+"|"+aux_destino.getIdsocio().toString());
                                            //Verifico si de verdad cuenta destinono pertence al mismo socio ya que es transferencia a tercero
                                        	if (aux_origen.getIdorigen().intValue() != aux_destino.getIdorigen().intValue()&&
                                        		aux_origen.getIdsocio().intValue() != aux_destino.getIdsocio().intValue()) {                                        		
                                                //Valido que el producto destino no sea un prestamo
                                                if (producto_destino.getTipoproducto() == 0) {                                                   
                                                        message = "VALIDADO CON EXITO";                                                    
                                                } else {
                                                    message = "PRODUCTO DESTINO SOLO ACEPTA PAGOS";
                                                }
                                            } else {
                                                message = "EL TIPO DE TRANSFERENCIA ES A TERCEROS PERO TU CUENTA DESTINO PERTENECE AL MISMO SOCIO";
                                            }
                                        } else {
                                            message = "PRODUCTO DESTINO NO OPERA PARA BANCA MOVIL";
                                        }
                                    } else {
                                        message = "PRODUCTO DESTINO ESTA INACTIVO";
                                    }
                                } else {
                                    message = "PRODUCTO DESTINO NO EXISTE";
                                }
                            } else {
                                message = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
                            }
                        } else {
                            message = "PRODUCTO ORIGEN INACTIVO";
                        }
                    } else {
                        message = "PRODUCTO ORIGEN NO PERMITE RETIROS";
                    }
                } else {
                    message = "PRODUCTO ORIGEN NO OPERA PARA BANCA MOVIL";
                }
            } else {
                message = "PRODUCTO ORIGEN NO PERTENECE AL SOCIO:" + clientBankIdentifier;
            } 
        return message.toUpperCase();
   }

    public String validarPagoAPrestamos(int identificadorTr, String opaOrigen, Double monto, String opaDestino, String clientBankIdentifier) {

    opaDTO opaO = util.opa(opaOrigen);
    opaDTO opaD = util.opa(opaDestino);

    boolean identificador_prestamo_propio = false;
    boolean identificador_prestamo_tercero = false;
    
    Auxiliares aux_origen = auxiliaresService.AuxiliarByOpa(opaO.getIdorigenp(),opaO.getIdproducto(),opaO.getIdauxiliar());
    Auxiliares aux_destino = auxiliaresService.AuxiliarByOpa(opaD.getIdorigenp(),opaD.getIdproducto(),opaD.getIdauxiliar());    
    String message = "";
   
         if (aux_origen != null) {
            Double saldo = aux_origen.getSaldo().doubleValue();  
            //Valido que el producto origen se trabaje en banca movil
            TiposCuentaBankingly  cuenta_bankingly_origen = cuentasBankinglyService.findTipoCuentaById(aux_origen.getIdproducto());
            if (cuenta_bankingly_origen != null) {
                //Busco descripcion del idproducto origen
                Productos producto_origen = productosService.findProductoById(aux_origen.getIdproducto());
                //Si el producto no es un prestamo            
                if (producto_origen.getTipoproducto() == 0) {
                    //Verifico el estatus de la cuenta origen
                    if (aux_origen.getEstatus() == 2) {
                        //verifico que el saldo del producto origen es mayor o igual a lo que se intenta transferir
                        if (saldo >= monto) {                           
                            if (aux_destino != null) {
                                //Busco el producto destino
                                Productos producto_destino = productosService.findProductoById(aux_destino.getIdproducto());
                                //Valido que la cuenta destino este activa
                                if (aux_destino.getEstatus() == 2) {
                                    //Valido que cuenta destino pertenezca al mismo socio
                                    if (identificadorTr == 3) {
                                    	System.out.println("idorigen:"+aux_origen.getIdorigen()+"|"+aux_destino.getIdorigen());
                                    	System.out.println("idorigen:"+aux_origen.getIdgrupo()+"|"+aux_destino.getIdgrupo());
                                    	System.out.println("idorigen:"+aux_origen.getIdsocio()+"|"+aux_destino.getIdsocio());                                    	
                                        if (aux_origen.getIdorigen().intValue() == aux_destino.getIdorigen().intValue() && 
                                        	aux_origen.getIdgrupo().intValue() == aux_destino.getIdgrupo().intValue() &&
                                        	aux_origen.getIdsocio().intValue() == aux_destino.getIdsocio().intValue()) {
                                            identificador_prestamo_propio = true;
                                        } else {
                                            message = "CUENTA DESTINO NO PERTENECE AL MISMO SOCIO";
                                        }
                                    } else if (identificadorTr == 4) {
                                        if (aux_origen.getIdorigen().intValue() == aux_destino.getIdorigen().intValue() &&
                                        	aux_origen.getIdgrupo().intValue() == aux_destino.getIdgrupo().intValue() &&
                                        	aux_origen.getIdsocio().intValue() == aux_destino.getIdsocio().intValue()) {
                                            message = "TU TIPO DE PAGO SE IDENTIFICA COMO TERCERO PERO LA CUENTA DESTINO PERTENECE AL MISMO SOCIO";
                                        } else {
                                            identificador_prestamo_tercero = true;
                                        }
                                    }

                                    if (identificador_prestamo_propio || identificador_prestamo_tercero) {                                     
                                    	if (producto_destino.getTipoproducto() == 2) {                                    		
                                             if (monto <= Double.parseDouble(aux_destino.getSaldo().toString())) {                                               
                                            	  message = "VALIDADO CON EXITO";                                                    
                                            } else {
                                                message = "EL SALDO QUE INTENTA PAGAR SOBREPASA A LA DEUDA";
                                            }
                                        } else {
                                            message = "PRODUCTO DESTINO NO ES UN PRESTAMO";
                                        }
                                    } else {
                                        message = message;
                                    }

                                } else {
                                    message = "PRODUCTO DESTINO ESTA INACTIVO";
                                }
                            } else {
                                message = "NO SE ENCONTRO PRODUCTO DESTINO";
                            }
                        } else {
                            message = "FONDOS INSUFICIENTES PARA COMPLETAR LA TRANSACCION";
                        }
                    } else {
                        message = "PRODUCTO ORIGEN INACTIVO";
                    }
                } else {
                    message = "PRODUCTO ORIGEN NO PERMITE RETIROS";
                }
            } else {
                message = "PRODUCTO ORIGEN NO OPERA PARA BANCA MOVIL";
            }
        } else {
            message = "PRODUCTO ORIGEN NO PERTENECE AL SOCIO:" + clientBankIdentifier;
        }
         return message.toUpperCase();
    }
    

    public File construirHtmlVoucher(Auxiliares_d ad) throws FileNotFoundException {
    	    //Leyendo el .txt con estructura del html
    	    File fileTxt = new File(ruta() + "voucher.txt");
    	    //El nombre que se le dara al html
    		String nombre_html = "voucher"+ad.getTransaccion().toString()+ad.getFecha().toString().replace("-","").replace(":","").replace(".","");// =nombre_txt.replace(".txt",".html");
    		String html = ruta() + nombre_html+ ".html";
    		File file_html = new File(html);
    		
    		FileOutputStream fs = new FileOutputStream(file_html);
    		OutputStreamWriter out = new OutputStreamWriter(fs);
    		try {
    			FileReader fr = new FileReader(fileTxt);
    			BufferedReader br = new BufferedReader(fr);
    			String linea;
    			String linea_contenedor = "";
    			
    			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:MM:ss");
    		    String hoy=sdf.format(new Date());
    		    
    		    String mov="";
    		    
    		    if(ad.getCargoabono() == 0) {
    		    	mov = "Retiro";
    		    }else {
    		    	mov = "Deposito";
    		    }
    			//Leyendo el txt
    			while ((linea = br.readLine()) != null) {
    				if(linea.contains("@@direccion@@")) {
    					linea = linea.replace("@@direccion@@",origenesService.findMatrizOrigen().getNombre());
    				}else if(linea.contains("/usr/local/saicoop/img_caratula_ahorros")) {
    				linea = linea.replace("/usr/local/saicoop/img_caratula_ahorros/",ruta());
    				System.out.println("linea:"+linea);
    				}else if(linea.contains("@@opa@@")) {
    					linea = linea.replace("@@opa@@",ad.getIdorigenp()+"-"+ad.getIdproducto()+"-"+ad.getIdauxiliar());
    				}else if(linea.contains("@@tipoMov@@")) {
    					linea = linea.replace("@@tipoMov@@",mov);
    				}else if(linea.contains("@@fecha@@")){
    					linea = linea.replace("@@fecha@@",ad.getFecha().toString().substring(0,19));
    				}else if(linea.contains("@@monto@@")){
    					linea = linea.replace("@@monto@@","$"+ad.getMonto().toString());
    				}else if(linea.contains("@@saldoec@@")){
    					linea = linea.replace("@@saldoec@@","$"+ad.getSaldoec().toString());
    				}else if(linea.contains("@@ticket@@")){
    					linea = linea.replace("@@ticket@@",ad.getTransaccion().toString());
    				}else if(linea.contains("@@hoy@@")){
    					linea = linea.replace("@@hoy@@",hoy);
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
    
    public static String ruta() {
        String home = System.getProperty("user.home");
        String separador = System.getProperty("file.separator");
        String actualRuta = home + separador + "Banca" + separador + "voucher" + separador;        
        return actualRuta;
    }


    }
    

