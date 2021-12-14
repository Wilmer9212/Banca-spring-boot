package com.fenoreste.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.saiFuncionesRepository;

@Service
public class SaiFuncionesServiceImpl implements ISaiFuncionesService{

	@Autowired
	saiFuncionesRepository saiFuncionesDao;
	
	@Override
	public String sai_auxiliar(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_auxiliar(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public String sai_estado_cuenta_ahorros(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fechaInicio,
			Date fechaFinal) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_estado_cuenta_ahorros(idorigenp, idproducto, idauxiliar, fechaInicio, fechaFinal);
	}

	@Override
	public String sai_estado_cuenta_prestamos(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date fechaInicio, Date fechaFinal) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_estado_cuenta_prestamos(idorigenp, idproducto, idauxiliar, fechaInicio, fechaFinal);
	}

	@Override
	public String sai_estado_cuenta_inversiones(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date fechaInicio, Date fechaFinal) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_estado_cuenta_inversiones(idorigenp, idproducto, idauxiliar, fechaInicio, fechaFinal);
	}

	@Override
	public String sai_calcula_saldo_promedio_diario(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date fechaInicio, Date fechaFinal) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_calcula_saldo_promedio_diario(idorigenp, idproducto, idauxiliar, fechaInicio, fechaFinal);
	}

	@Override
	public String sai_prestamo_cuanto(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date fecha,Integer tipoamortizacion, String sai) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_prestamo_cuanto(idorigenp, idproducto, idauxiliar,fecha,tipoamortizacion, sai);
	}

	@Override
	public String sai_prestamo_adelanto_intereses(Integer idorigenp, Integer idproducto, Integer idauxiliar,String sai) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_prestamo_adelanto_interes(idorigenp, idproducto, idauxiliar,sai);
	}

	@Override
	public String sai_aplica_transaccion(Date fecha, Integer idusuario, String sesion, String referencia) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_procesa_transaccion(fecha, idusuario, sesion, referencia);
	}

	@Override
	public String sai_limite_adelanto(Integer idorigenp, Integer idproducto, Integer idauxiliar, Double amount) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_limite_adelanto(idorigenp, idproducto, idauxiliar, amount);
	}

	@Override
	public String sai_detalle_transaccion_aplicada(Date fecha, Integer idusuario, String sesion, String referencia) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_detalle_transaccion_aplicada(fecha, idusuario, sesion, referencia);
	}

	@Override
	public String sai_termina_transaccion(Date fecha, Integer idusuario, String sesion, String referencia) {
		// TODO Auto-generated method stub
		return saiFuncionesDao.sai_termina_transaccion(fecha, idusuario, sesion, referencia);
	}

	

}
