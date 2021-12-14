package com.fenoreste.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.fenoreste.entity.Amortizacion;

@Service
public interface IAmortizacionesService {
	
	//Amortizaciones pagadas
	public Integer findCountAmortizacionesPagadas(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	//Amortizaciones pendientes
	public Integer findCountAmortizacionesActivas(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	//Total amortizaciones para un opa
	public Integer findCountAmortizaciones(Integer idorigenp,Integer idproducto,Integer idauxiliar);	
	
	//Proxima amortizacion para un opa 
	public List<Object[]> findProximaAmortizacion(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	//Amortizacion por opa y idamortizacion
	public List<Object[]> findAmortizacionByIdamortizacion(Integer idorigenp,Integer idproducto,Integer idauxiliar,Integer idamortizacion);
	
	public List<Object[]> findAmortizaciones(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	public List<Object[]> findAmortizacionesPagadasIdamortizacionAsc(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
		
	public List<Object[]> findAmortizacionesActivasIdamortizacionAsc(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	public List<Object[]> findAmortizacionesPagadas(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	public List<Object[]> findAmortizacionesActivas(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	//Lista de pagos
	List<Object[]>findAmortizacionesTotal(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	
	List<Amortizacion>findAll(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
			
}
