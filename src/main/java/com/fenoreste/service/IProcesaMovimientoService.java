package com.fenoreste.service;

import java.util.Date;

import com.fenoreste.entity.RegistraMovimiento;

public interface IProcesaMovimientoService {
    
	public boolean insertarMovimiento(RegistraMovimiento mov);
	public boolean eliminaMovimiento(RegistraMovimiento mov);
	
	public void save(Date fecha,
			  Integer idusuario,
			  String sesion,
			  String referencia,
			  Integer idorigen,
			  Integer idgrupo,
			  Integer idsocio,			  
			  Integer idorigenp,
			  Integer idproducto,
			  Integer idauxiliar,			  
			  Integer cargoabono,
			  Double monto,
			  Double iva,
			  Integer tipo_amortizacion,
			  String sai);
	
}
