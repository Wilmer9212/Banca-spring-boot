package com.fenoreste.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.consumo.IConsumosHTTPService;
import com.fenoreste.dto.ByDocumentsDTO;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.FoliosTarjeta;
import com.fenoreste.entity.Origenes;
import com.fenoreste.entity.Persona;
import com.fenoreste.entity.Tablas;
import com.fenoreste.entity.UsuariosBancaMovil;
@Service
public class CustomerServiceSpring {

	@Autowired
	ITablasService tablasService;

	@Autowired
	IPersonaService personaService;

	@Autowired
	IAuxiliaresService auxiliaresService;

	@Autowired
	IOrigenesService origenesService;
	
	@Autowired
	IFoliosTarjetasService foliosService;
	
	@Autowired
	IOtrosService otrosService;
	
	@Autowired
	IConsumosHTTPService httpService;
	
	@Autowired
	IUsuariosBancaMovilService usuariosService;
	


	public ByDocumentsDTO getClientsByDocuments(String name, String lastName, String documentId, String mail,
			String cellPhone, String phone,String username) {
		ByDocumentsDTO matriculacion = new ByDocumentsDTO();
		// Bandera para hacer verdadera todas las validacione excepto buscar el alias
		// registrado
		boolean bandera_validacion = false;
		Auxiliares a_producto_banca_movil=null;
		// Busco si existe la persona
		Persona p = personaService.findPersonaMatriculacion(name.toUpperCase().replace(" ", "").trim(),
				lastName.toUpperCase().replace(" ", "").trim(), documentId.toUpperCase().trim(),
				mail.toUpperCase().trim(), cellPhone.trim(), phone.trim());
		if (p != null) {
			Tablas tb_producto_banca_movil = tablasService.findTabla("bankingly_banca_movil", "producto_banca_movil");
			System.out.println("Tablas Producto banca:"+tb_producto_banca_movil);
			if (tb_producto_banca_movil != null) {
				// Reviso si esta capturado el folio de producto para banca movil con el socio
				 a_producto_banca_movil = auxiliaresService.findAuxiliarBancaMovil(p.getIdorigen(),	p.getIdgrupo(), p.getIdsocio(), Integer.parseInt(tb_producto_banca_movil.getDato1()));
				if (a_producto_banca_movil != null) {
					// Busco el origen
					Origenes origen = origenesService.findMatrizOrigen();
					//CSN
					if (origen.getIdorigen()==30200) {
						if(validacion_csn(p).toUpperCase().contains("EXITO")) {
							bandera_validacion = true;
						}else {
							matriculacion.setClientName(validacion_csn(p).toUpperCase());
						}
				    //Mitras
					} else if(origen.getIdorigen() == 30300) {
						if(validacion_mitras(p).toUpperCase().contains("EXITO")) {
							bandera_validacion = true;
						}else {
							matriculacion.setClientName(validacion_mitras(p).toUpperCase());
						}
					}else {
						bandera_validacion = true;
					}
				} else {
					matriculacion.setClientName("PRODUCTO PARA BANCA MOVIL NO CAPTURADO");
				}
			} else {
				matriculacion.setClientName("CONFIGURACION PARA PRODUCTO DE BANCA MOVIL NO EXISTE");
			}
		} else {
			matriculacion.setClientName("PERSONA NO EXISTE");
		}

		if (bandera_validacion) {
			//Busco que el usuario no este registrado
			
			UsuariosBancaMovil usuario=usuariosService.findByOgs(p.getIdorigen(),p.getIdgrupo(),p.getIdsocio());
			
			//if(usuario != null) {
				//matriculacion.setClientName("USUARIO YA ESTA REGISTRADO CON EL ALIAS:"+usuario.getAlias_usuario());				
			//}else {
				matriculacion.setClientName(p.getNombre()+" "+p.getAppaterno()+" "+p.getApmaterno());
				matriculacion.setDocumentId(p.getCurp().toUpperCase());
				matriculacion.setClientType("1");
				matriculacion.setClientBankIdentifier(String.format("%06d",p.getIdorigen())+String.format("%02d",p.getIdgrupo())+String.format("%06d",p.getIdsocio()));
				if(usuario == null) {
				usuario=new UsuariosBancaMovil();
				usuario.setIdorigen(p.getIdorigen());
				usuario.setIdgrupo(p.getIdgrupo());
				usuario.setIdsocio(p.getIdsocio());
				usuario.setAlias_usuario(username);
				usuario.setIdorigenp(a_producto_banca_movil.getIdorigenp());
				usuario.setIdproducto(a_producto_banca_movil.getIdproducto());
				usuario.setIdauxiliar(a_producto_banca_movil.getIdauxiliar());
				usuario.setEstatus(bandera_validacion);
				usuariosService.save(usuario.getIdorigen(),usuario.getIdgrupo(), usuario.getIdsocio(),username,usuario.getIdorigenp(),usuario.getIdproducto(),usuario.getIdauxiliar(),true);
				}else {	
			    usuariosService.eliminar(usuario.getIdorigen(),usuario.getIdgrupo(),usuario.getIdsocio());
			    usuario=new UsuariosBancaMovil();
				usuario.setIdorigen(p.getIdorigen());
				usuario.setIdgrupo(p.getIdgrupo());
				usuario.setIdsocio(p.getIdsocio());
				usuario.setAlias_usuario(username);
				usuario.setIdorigenp(a_producto_banca_movil.getIdorigenp());
				usuario.setIdproducto(a_producto_banca_movil.getIdproducto());
				usuario.setIdauxiliar(a_producto_banca_movil.getIdauxiliar());
				usuario.setEstatus(bandera_validacion);
				usuariosService.save(usuario.getIdorigen(),usuario.getIdgrupo(), usuario.getIdsocio(),username,usuario.getIdorigenp(),usuario.getIdproducto(),usuario.getIdauxiliar(),true);
				}
			    
		}

		return matriculacion;
	}

	public String validacion_csn(Persona p) {
		String mensaje_validacion = "";
		
		// Busco tabla con producto tarjeta de debito
		Tablas tb_producto_tdd = tablasService.findTabla("bankingly_banca_movil", "producto_tdd");	
		
		System.out.println("Tablas producto tdd:"+tb_producto_tdd);
		if (tb_producto_tdd != null) {
			// Ahora busco el auxiliar de la cuenta tdd para el socio
			Auxiliares a_producto_tdd = auxiliaresService.findAuxiliarTdd(p.getIdorigen(), p.getIdgrupo(),
					p.getIdsocio(), Integer.parseInt(tb_producto_tdd.getDato2()));
			if (a_producto_tdd != null) {
				// Verifico el estatus del auxiliar
				if (a_producto_tdd.getEstatus() == 2) {
					// Ahora se lee el saldo de la tdd desde el WS de ALestra
					//Busco el folio para el idtarjeta
					FoliosTarjeta folioTdd= foliosService.findFolioByOpa(a_producto_tdd.getIdorigenp(),a_producto_tdd.getIdproducto(),a_producto_tdd.getIdauxiliar());
					JSONObject BalanceQuery = null;
					try {
						BalanceQuery = new JSONObject(httpService.getBalanceQuery(folioTdd.getIdtarjeta()));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Double saldo_en_tdd = null;
					try {
						saldo_en_tdd = Double.parseDouble(BalanceQuery.getString("availableAmount"));
					} catch (NumberFormatException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (saldo_en_tdd >= Double.parseDouble(tb_producto_tdd.getDato2())) {
						//Ahora busco el mensaje para bloqueo en sopar
				 		Tablas tb_sopar = tablasService.findTabla("bankingly_banca_movil", "sopar");
				 		System.out.println("Tablas sopar:"+tb_sopar);
						Integer count_sopar = otrosService.count_sopar(a_producto_tdd.getIdorigen(),a_producto_tdd.getIdgrupo(),a_producto_tdd.getIdsocio(),tb_sopar.getDato2());
						if(count_sopar == 0) {
							mensaje_validacion = "VALIDADO CON EXITO";
						}else {
							mensaje_validacion = "SOCIO ESTA BLOQUEADO";
						}
					} else {
						mensaje_validacion = "TARJETA DE DEBITO NO TIENE EL MONTO MINIMO:" + tb_producto_tdd.getDato2();
					}
				} else {
					mensaje_validacion = "ESTATUS FOLIO PARA TARJETA DE DEBITO NO ES ACTIVO:"+ a_producto_tdd.getEstatus();
				}
			} else {
				mensaje_validacion = "SOCIO NO TIENE PRODUCTO PARA TARJETA DE DEBITO:" + tb_producto_tdd.getDato1();
			}
		} else {
			mensaje_validacion = "NO EXISTEN REGISTROS PARA TARJETA DE DEBITO";
		}

		return mensaje_validacion;
	}
	
	public String validacion_mitras(Persona p) {
		String mensaje_validacion = "";
		
		if(p.getIdgrupo() == 10) {
		   	mensaje_validacion = "VALIDADO CON EXITO";
		}else if(p.getIdgrupo() == 12) {
			mensaje_validacion = "SOCIO NO ES APTO PARA CONTRATAR SERVICIO...";
		}else {
			mensaje_validacion = "SOCIO NO ES APTO PARA CONTRATAR SERVICIO...";
		}
		return mensaje_validacion;
	}
}
