package com.fenoreste.service;

import java.sql.Connection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.ProcesaMovimientoRepository;
import com.fenoreste.entity.RegistraMovimiento;

@Service
public class ProcesaMovimientoImpl implements IProcesaMovimientoService{
     
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	ProcesaMovimientoRepository procesarMovDao;
	
	@Override
	public boolean insertarMovimiento(RegistraMovimiento mov) {
	  procesarMovDao.saveAndFlush(mov);
	 
	  return true;
	}

	@Override
	public boolean eliminaMovimiento(RegistraMovimiento mov) {
		procesarMovDao.delete(mov);
	return true;
	}

	@Override
	public void save(Date fecha, Integer idusuario, String sesion, String referencia, Integer idorigen, Integer idgrupo,
			Integer idsocio, Integer idorigenp, Integer idproducto, Integer idauxiliar, Integer cargoabono,
			Double monto, Double iva, Integer tipo_amortizacion, String sai) {
	procesarMovDao.saves(fecha, idusuario, sesion, referencia, idorigen, idgrupo, idsocio, idorigenp, idproducto, idauxiliar, cargoabono, monto, iva, tipo_amortizacion, sai);
		
	}

	

	

}
