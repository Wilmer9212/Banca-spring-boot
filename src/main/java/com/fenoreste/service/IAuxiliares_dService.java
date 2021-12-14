package com.fenoreste.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.fenoreste.dto.AuxiliaresdDTO;
import com.fenoreste.entity.Auxiliares_d;

@Service
public interface IAuxiliares_dService {

	public List<Auxiliares_d> findAuxiliares_dByOpa(Integer idorigenp, Integer idproducto, Integer idauxiliar);

	public List<Auxiliares_d> findAuxiliares_dByOpaFecha(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date fechaI, Date fechaF);

	public Double findSaldoUltimas24Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha);

	public Double findSaldoUltimas48Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha);

	public Double findSaldoMasDe48Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha);

	public Auxiliares_d findUltimoRegistro(Integer idorigenp, Integer idproducto, Integer idauxiliar);

	public List<Object[]> findUltimos5Movimientos(Integer idorigenp, Integer idproducto, Integer idauxiliar);	
	
	public List<Object[]> findMovimientosFechaAscRF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Date FFinal,Pageable pageable);
	
	public List<Object[]> findMovimientosFechaAscFI(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Pageable pageable);
	
	public List<Object[]> findMovimientosFechaAscFF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FFinal,Pageable pageable);

	public List<Object[]> findMovimientosFechaAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
	
	
	public List<Object[]> findMovimientosFechaDescRF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Date FFinal,Pageable pageable);
		
	public List<Object[]> findMovimientosFechaDescFI(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Pageable pageable);
	
	public List<Object[]> findMovimientosFechaDescFF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FFinal,Pageable pageable);
		
	public List<Object[]> findMovimientosFechaDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
	
	public List<Object[]> findMovimientosFechaDefault(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
				
	
		
	public List<Object[]> findMovimientosPolizaAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
	
	public List<Object[]> findMovimientosPolizaDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
	
	public List<Object[]> findMovimientosPoliza(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
	
	public List<Object[]> findMovimientosCargoAbonoAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
		
	public List<Object[]> findMovimientosCargoAbonoDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
			
	public List<Object[]> findMovimientosCargoAbono(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
			
	public List<Object[]> findMovimientosMontoAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
			
	public List<Object[]> findMovimientosMontoDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
		
	public List<Object[]> findMovimientosMonto(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
		
	public List<Object[]> findMovimientosSaldoecAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
	
	public List<Object[]> findMovimientosSaldoecDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
		
	public List<Object[]> findMovimientosSaldoec(Integer idorigenp, Integer idproducto, Integer idauxiliar ,Pageable pageable);
	
	
	public  Double montoPesos(Integer idorigen, Integer idgrupo,Integer idsocio,Date fecha);
	public  Double montoUdis(Integer idorigen,Integer idgrupo,Integer idsocio,String periodo);
	
	public Auxiliares_d findByTransaccion(Integer transaccion);
		
}
