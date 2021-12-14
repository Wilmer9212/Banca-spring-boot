package com.fenoreste.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dto.ogsDTO;
import com.fenoreste.dto.opaDTO;
import com.fenoreste.entity.Auxiliares_d;
import com.fenoreste.entity.Persona;
import com.fenoreste.entity.Productos;
import com.fenoreste.entity.Tablas;
import com.fenoreste.service.IAuxiliares_dService;
import com.fenoreste.service.IPersonaService;
import com.fenoreste.service.IProductosService;
import com.fenoreste.service.ITablasService;

@Service
public class PreparaSMS {

	@Autowired
	HerramientasUtil util;
	@Autowired 
	EnviarSMS sendSMS;
	@Autowired
	ITablasService tablasService;
	@Autowired
	IAuxiliares_dService auxiliares_dService;
	@Autowired
	IPersonaService personasService;
	@Autowired
	IProductosService productosService;
	

    // ENVIA EL SMS METODO PRA CSN
    public String enviaSMS_CSN(String montoAbono, int identificadorOperacion, String debitAccount, String creditAccount, String numeroSocio) {
        System.out.println("preparando el sms");
        String respuesta_sms = "";
        //consulto en tablas si existe la url del script de san nicolas para envio de mensajes
          try {
            
            Tablas tablasUrlSMS =tablasService.findTabla("bankingly_banca_movil", "liga_envio_mensajes");
            opaDTO opa_origen = util.opa(debitAccount);

            opaDTO opa_destino = null;

            //Buscamos el movimiento que se hizo
            Auxiliares_d aux_origen_d = auxiliares_dService.findUltimoRegistro(opa_origen.getIdorigenp(),opa_origen.getIdproducto(),opa_origen.getIdauxiliar());
            String auth_destino = "";
            
            if (identificadorOperacion != 5) {
                opa_destino = util.opa(creditAccount);
                Auxiliares_d aux_destino_d = auxiliares_dService.findUltimoRegistro(opa_destino.getIdorigenp(),opa_destino.getIdproducto(),opa_destino.getIdauxiliar());
                //pr_destino = em.find(Productos.class, opa_destino.getIdproducto());
                auth_destino = aux_destino_d.getIdorigenc() + "" + aux_destino_d.getPeriodo() + "" + aux_destino_d.getIdtipo() + "" + aux_destino_d.getIdpoliza();
            }
            Productos producto_origen = productosService.findProductoById(aux_origen_d.getIdproducto());
            Productos producto_destino = productosService.findProductoById(opa_destino.getIdproducto());
            if (tablasUrlSMS.getDato2().length() > 0) {
                System.out.println("se encontro la url para envio de sms");
                //Obtengo el celular del socio 
                ogsDTO ogs = util.ogs(numeroSocio);
                Persona p = personasService.findByOGS(ogs.getIdorigen(),ogs.getIdgrupo(),ogs.getIdsocio());
                Tablas tablaContenidoSMS = null;
                String contenidoSMS = "";
                String auth_origen = aux_origen_d.getIdorigenc() + "" + aux_origen_d.getPeriodo() + "" + aux_origen_d.getIdtipo() + "" + aux_origen_d.getIdpoliza();
                //Se identifica para transferencias a cuentas propias
                if (identificadorOperacion == 1) {
                    tablaContenidoSMS = tablasService.findTabla("bankingly_banca_movil", "sms_retiro_cuenta_propia");
                    System.out.println("tabla contenido sms:" + tablaContenidoSMS);
                    contenidoSMS = contenidoSMS(tablaContenidoSMS.getDato2(), montoAbono, producto_origen.getNombre(), producto_destino.getNombre(), auth_origen, auth_destino);
                    System.out.println("El contenido de tu mensaje es:" + contenidoSMS);
                    //sendSMS.enviar(tablasUrlSMS.getDato2(), p.getCelular(), contenidoSMS);
                    respuesta_sms = sendSMS.enviarSMS(tablasUrlSMS.getDato2(), p.getCelular(), contenidoSMS);
                    //Transferencia a terceros dentro de la entidad
                } else if (identificadorOperacion == 2) {
                    System.out.println("Tercero");
                    tablaContenidoSMS = tablasService.findTabla("bankingly_banca_movil", "sms_retiro_cuenta_tercero");
                    System.out.println("tabla contenido sms:" + tablaContenidoSMS);
                    contenidoSMS = contenidoSMS(tablaContenidoSMS.getDato2(), montoAbono, producto_origen.getNombre(), producto_destino.getNombre(), auth_origen, auth_destino);
                    System.out.println("El contenido de tu mensaje es:" + contenidoSMS);
                    respuesta_sms = sendSMS.enviarSMS(tablasUrlSMS.getDato2(), p.getCelular(), contenidoSMS);
                    //Pago de prestamos
                } else if (identificadorOperacion == 3) {
                    System.out.println("Pago prestamo propio");
                    tablaContenidoSMS = tablasService.findTabla("bankingly_banca_movil", "sms_retiro_cuenta_propia");
                    System.out.println("tabla contenido sms:" + tablaContenidoSMS);
                    contenidoSMS = contenidoSMS(tablaContenidoSMS.getDato2(), montoAbono, producto_origen.getNombre(), producto_destino.getNombre(), auth_origen, auth_destino);
                    System.out.println("El contenido de tu mensaje es:" + contenidoSMS);
                    respuesta_sms = sendSMS.enviarSMS(tablasUrlSMS.getDato2(), p.getCelular(), contenidoSMS);
                } else if (identificadorOperacion == 4) {
                    System.out.println("Pago prestamo tercero");
                    tablaContenidoSMS = tablasService.findTabla("bankingly_banca_movil", "sms_retiro_cuenta_tercero");
                    System.out.println("tabla contenido sms:" + tablaContenidoSMS);
                    contenidoSMS = contenidoSMS(tablaContenidoSMS.getDato2(), montoAbono, producto_origen.getNombre(), producto_destino.getNombre(), auth_origen, auth_destino);
                    System.out.println("El contenido de tu mensaje es:" + contenidoSMS);
                    respuesta_sms = sendSMS.enviarSMS(tablasUrlSMS.getDato2(), p.getCelular(), contenidoSMS);
                } else if (identificadorOperacion == 5) {
                    System.out.println("Pago orden SPEI");
                    tablaContenidoSMS = tablasService.findTabla("bankingly_banca_movil", "sms_retiro_cuenta_tercero");
                    System.out.println("tabla contenido sms:" + tablaContenidoSMS);
                    contenidoSMS = contenidoSMS(tablaContenidoSMS.getDato2(), montoAbono, producto_origen.getNombre(), creditAccount, auth_origen, "");
                    System.out.println("El contenido de tu mensaje es:" + contenidoSMS);
                    respuesta_sms = sendSMS.enviarSMS(tablasUrlSMS.getDato2(), p.getCelular(), contenidoSMS);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en sms:" + e.getMessage());
        }        
        return respuesta_sms;
    }

    // RELLENA EL CONTENIDO DEL SMS
    private String contenidoSMS(String contenidoSMS, String monto, String productoOrigen, String productoDestino, String authOrigen, String authDestino) {

        Date hoy = new Date();
        System.out.println("Formando contenido fecha:" + hoy);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:MM:ss");
        String fecha = sdf.format(hoy);
        System.out.println("Fecha String:" + fecha);
        return contenidoSMS.replace("@monto@", monto)
                .replace("@fechayHora@", fecha)
                .replace("@productoOrigen@", productoOrigen)
                .replace("@productoDestino@", productoDestino)
                .replace("@autorizacionOrigen@", authOrigen)
                .replace("@autorizacionDestino@", authDestino);
    }

}
