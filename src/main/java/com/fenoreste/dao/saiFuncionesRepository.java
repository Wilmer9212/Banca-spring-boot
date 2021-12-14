package com.fenoreste.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Productos;

public interface saiFuncionesRepository extends CrudRepository<Productos, Long>  {
	
	@Query(value = "SELECT sai_auxiliar(?,?,?,(SELECT date(fechatrabajo) FROM origenes LIMIT 1))" , nativeQuery = true)
	String sai_auxiliar(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	@Query(value = "SELECT sai_estado_cuenta_ahorros(?,?,?,?,?)" , nativeQuery = true)
	String sai_estado_cuenta_ahorros(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	
	@Query(value = "SELECT sai_estado_cuenta_prestamos(?,?,?,?,?)" , nativeQuery = true)
	String sai_estado_cuenta_prestamos(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	
	@Query(value = "SELECT sai_estado_cuenta_dpfs(?,?,?,?,?)" , nativeQuery = true)
	String sai_estado_cuenta_inversiones(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
	
	@Query(value = "SELECT sai_calcula_saldo_promedio_diario(?,?,?,?,?,0)" , nativeQuery = true)
	String sai_calcula_saldo_promedio_diario(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fechaInicio,Date fechaFinal);
    
	@Query(value="SELECT sai_bankingly_prestamo_cuanto(?1,?2,?3,?4,?5,?6)", nativeQuery=true)
	String sai_prestamo_cuanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Date fecha,Integer tipoamortizacion,String sai);
	
	@Query(value="SELECT sai_bankingly_monto_adelanto_interes(?1,?2,?3,(SELECT date(fechatrabajo) FROM origenes LIMIT 1),?4)", nativeQuery=true)
	String sai_prestamo_adelanto_interes(Integer idorigenp,Integer idproducto,Integer idauxiliar,String sai);
	
	@Query(value ="SELECT sai_bankingly_aplica_transaccion(?,?,?,?)",nativeQuery = true)
	String sai_procesa_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
	
	@Query(value = "SELECT sai_bankingly_limite_adelanto(?,?,?,(SELECT date(fechatrabajo) FROM origenes limit 1),NULL)", nativeQuery = true)
	String sai_limite_adelanto(Integer idorigenp,Integer idproducto,Integer idauxiliar,Double amount); 
	
	@Query(value ="SELECT sai_bankingly_detalle_transaccion_aplicada(?,?,?,?)", nativeQuery = true)
	String sai_detalle_transaccion_aplicada(Date fecha,Integer idusuario,String sesion,String referencia);
	
	@Query(value ="SELECT sai_bankingly_termina_transaccion(?,?,?,?)",nativeQuery = true)
	String sai_termina_transaccion(Date fecha,Integer idusuario,String sesion,String referencia);
	
	
	
	
}
