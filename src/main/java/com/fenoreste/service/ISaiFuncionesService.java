package com.fenoreste.service;

import java.util.Date;

import org.springframework.stereotype.Service;



@Service
public interface ISaiFuncionesService {

	public String sai_auxiliar(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	public String sai_estado_cuenta_ahorros(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	public String sai_estado_cuenta_prestamos(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	public String sai_estado_cuenta_inversiones(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	public String sai_calcula_saldo_promedio_diario(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	public String sai_prestamo_cuanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha,Integer tipoamortizacion,String sai);
	public String sai_prestamo_adelanto_intereses(Integer idorigenp,Integer idproducto,Integer idauxiliar,String sai);
	public String sai_aplica_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
	public String sai_limite_adelanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Double amount);
	public String sai_detalle_transaccion_aplicada(Date fecha,Integer idusuario,String sesion,String referencia);
	public String sai_termina_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
}
