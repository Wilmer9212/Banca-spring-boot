package com.fenoreste.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fenoreste.entity.*;

public interface AmortizacionesRepository extends JpaRepository<Amortizacion, Integer> {
     
	@Query(value = "SELECT count(*) FROM amortizaciones WHERE idorigenp=?1  AND idproducto=?2 AND idauxiliar=?3 AND todopag=true" , nativeQuery = true)
	Integer findCountAmortizacionesPagadas(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	@Query(value = "SELECT count(*) FROM amortizaciones WHERE idorigenp=?1  AND idproducto=?2 AND idauxiliar=?3 AND todopag=false" , nativeQuery = true)
	Integer findCountAmortizacionesActivas(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	
	@Query(value = "SELECT count(*) FROM amortizaciones WHERE idorigenp=?1  AND idproducto=?2 AND idauxiliar=?3" , nativeQuery = true)
	Integer findCountAmortizaciones(Integer idorigenp,Integer idproducto,Integer idauxiliar);	
	
	
	//Proxima cuota cuando el pago esta al corriente
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 and todopag=false ORDER BY vence ASC LIMIT 1", nativeQuery = true)
	List<Object[]> findProximaAmortizacion(Integer idorigenp,Integer idproducto,Integer idauxiliar);	
	
	//Buscar amortizacion por opa y por idamortizacion
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND idamortizacion=?4" , nativeQuery = true)
	List<Object[]> findAmortizacionByIdamortizacion(Integer idorigenp,Integer idproducto,Integer idauxiliar,Integer idamortizacion);
	
	
	
	
	//Buscar amortizaciones ordenado por idamortizacion ASC
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 " , nativeQuery = true)
	List<Object[]> findAmortizaciones(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	//Buscar amortizaciones pagadas ordenado por idamortizacion ASC
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND todopag=true ORDER BY idamortizacion ASC" , nativeQuery = true)
	List<Object[]> findAmortizacionesPagadasIdamortizacionAsc(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
		
	//Buscar amortizaciones activas ordenado por idamortizacion ASC
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND todopag=true ORDER BY idamortizacion ASC" , nativeQuery = true)
	List<Object[]> findAmortizacionesActivasIdamortizacionAsc(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	//Buscar amortizaciones pagadas
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND todopag=true" , nativeQuery = true)
	List<Object[]> findAmortizacionesPagadas(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	//Buscar amortizaciones activas
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND todopag=false" , nativeQuery = true)
	List<Object[]> findAmortizacionesActivas(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
			
	//Lista de pagos
	@Query(value = "SELECT vence,idamortizacion,abono,abonopag,todopag,io,atiempo,diasvencidos FROM amortizaciones WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY idamortizacion" , nativeQuery = true)
	List<Object[]>findAmortizacionesTotal(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
	
	@Query(value = "SELECT * FROM amortizaciones WHERE idorigenp=? AND idproducto=? AND idauxiliar=? ORDER BY idamortizacion", nativeQuery = true)
	List<Amortizacion>findAll(Integer idorigenp,Integer idproducto,Integer idauxiliar,Pageable pageable);
		
}
