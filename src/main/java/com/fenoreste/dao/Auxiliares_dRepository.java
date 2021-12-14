package com.fenoreste.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fenoreste.entity.Auxiliares_d;

public interface Auxiliares_dRepository extends CrudRepository<Auxiliares_d, Long> {

	@Query(value = "SELECT * FROM auxiliares_d WHERE idorigenp=?1  AND idproducto=?2 AND idauxiliar=?3", nativeQuery = true)
	List<Auxiliares_d> findAuxiliares_dByOpa(Integer idorigenp, Integer idproducto, Integer idauxiliar);

	@Query(value = "SELECT * FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) between ?4 AND ?5 ", nativeQuery = true)
	List<Auxiliares_d> findAuxiliares_dByOpaFecha(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date fechaI, Date fechaF);

	//Saldo para un opa ultimas 24 horass
	@Query(value = "SELECT saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3"
			+ " AND date(fecha) <= ?4 ORDER BY fecha DESC limit 1  ", nativeQuery = true)
	Double findSaldoUltimas24Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha);

	// Saldo para un opa ultimas 48 horass
	@Query(value = "SELECT saldoec  FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3"
			+ " AND date(fecha) <= ?4 ORDER BY fecha DESC limit 1 ", nativeQuery = true)
	Double findSaldoUltimas48Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha);

	// Saldo para un opa ultimas 48 horass
	@Query(value = "SELECT saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3"	
			+ " AND date(fecha) < ?4 ORDER BY fecha DESC limit 1 ", nativeQuery = true)
	Double findSaldoMasDe48Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha);

	// ultimo movimiento
	@Query(value = "SELECT * FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY fecha DESC limit 1 ", nativeQuery = true)
	Auxiliares_d findUltimoRegistro(Integer idorigenp, Integer idproducto, Integer idauxiliar);

	//Ultimos 5 movimientos
	@Query(value = "SELECT date(fecha),cargoabono,monto,transaccion,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY fecha DESC limit 5 ", nativeQuery = true)
	List<Object[]> findUltimos5Movimientos(Integer idorigenp, Integer idproducto, Integer idauxiliar);	
	
	//moviminentos por rango de fechas y ordenacion por fecha ASC 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) BETWEEN ?4 AND ?5 ORDER BY fecha ASC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaAscRF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Date FFinal,Pageable pageable);
	
	//moviminentos cuando la fecha sea mayor a la de inicio y ordenacion por fecha ASC 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) > ?4 ORDER BY fecha ASC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaAscFI(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Pageable pageable);
	
	//moviminentos cuando la fecha sea menor a la de FFinal y ordenacion por fecha ASC 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) < ?4 ORDER BY fecha ASC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaAscFF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FFinal,Pageable pageable);
	
	//moviminentos cuando la fecha sea menor a la de FFinal y ordenacion por fecha ASC 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3  ORDER BY fecha ASC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	
	//moviminentos por rango de fechas y ordenacion por fecha Desc 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) BETWEEN ?4 AND ?5 ORDER BY fecha DESC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaDescRF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Date FFinal,Pageable pageable);
		
	//moviminentos cuando la fecha sea mayor a la de inicio y ordenacion por fecha ADesc
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) > ?4 ORDER BY fecha DESC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaDescFI(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FInicio,Pageable pageable);
	
	//moviminentos cuando la fecha sea menor a la de FFinal y ordenacion por fecha DEsc
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 AND date(fecha) < ?4 ORDER BY fecha DESC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaDescFF(Integer idorigenp, Integer idproducto, Integer idauxiliar,Date FFinal,Pageable pageable);
		
	//moviminentos ordenacion por fecha DESC
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY fecha DESC", nativeQuery =  true)
	List<Object[]> findMovimientosFechaDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	//moviminentos ordenacion por fecha 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY fecha ", nativeQuery =  true)
	List<Object[]> findMovimientosFechaDefault(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
			
		
		
		
	
	//moviminentos ordenacion por poliza ASC 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY (idorigenc+periodo::integer+idtipo+idpoliza) ASC", nativeQuery =  true)
	List<Object[]> findMovimientosPolizaAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
	
	//moviminentos ordenacion por poliza Desc
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY (idorigenc+periodo::integer+idtipo+idpoliza) DESC", nativeQuery =  true)
	List<Object[]> findMovimientosPolizaDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
	
	//moviminentos ordenacion por poliza ASC 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY (idorigenc+periodo::integer+idtipo+idpoliza)", nativeQuery =  true)
	List<Object[]> findMovimientosPoliza(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
	
	//moviminentos ordenacion por cargoabono asc
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY cargoabono ASC", nativeQuery =  true)
	List<Object[]> findMovimientosCargoAbonoAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	//moviminentos ordenacion por cargoabono desc  
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY cargoabono DESC", nativeQuery =  true)
	List<Object[]> findMovimientosCargoAbonoDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
			
	//moviminentos ordenacion por cargoabono  
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY cargoabono", nativeQuery =  true)
	List<Object[]> findMovimientosCargoAbono(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
			
	//moviminentos ordenacion por monto asc  
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY monto ASC", nativeQuery =  true)
	List<Object[]> findMovimientosMontoAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
			
	//moviminentos ordenacion por monto Desc 
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY monto DESC", nativeQuery =  true)
	List<Object[]> findMovimientosMontoDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	//moviminentos ordenacion por monto   
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY monto", nativeQuery =  true)
	List<Object[]> findMovimientosMonto(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	//moviminentos ordenacion por saldoec ASc  
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY saldoec ASC", nativeQuery =  true)
	List<Object[]> findMovimientosSaldoecAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
	
	//moviminentos ordenacion por monto Desc  
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY saldoec DESC", nativeQuery =  true)
	List<Object[]> findMovimientosSaldoecDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	//moviminentos ordenacion por monto  
	@Query(value = "SELECT fecha,cargoabono,monto,idorigenc,periodo,idtipo,idpoliza,saldoec FROM auxiliares_d WHERE idorigenp=?1 AND idproducto=?2 AND idauxiliar=?3 ORDER BY saldoec", nativeQuery =  true)
	List<Object[]> findMovimientosSaldoec(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable);
		
	
	
	
	//Para transacciones
	@Query(value ="SELECT sum(monto) FROM auxiliares_d ad"
    + " INNER JOIN auxiliares a USING(idorigenp,idproducto,idauxiliar)"
    + " INNER JOIN productos pr USING(idproducto)"
    + " WHERE estatus=2"
    + " AND a.idorigen=?1"
    + " AND a.idgrupo=?2"
    + " AND a.idsocio=?3"
    + " AND pr.tipoproducto=0"
    + " AND ad.cargoabono=1"
    + " AND ad.date(fecha)=?4",nativeQuery = true )
    Double montoPesos(Integer idorigen, Integer idgrupo,Integer idsocio,Date fecha);
	
	@Query(value = "SELECT sum(monto) FROM auxiliares_d ad"
    + " INNER JOIN auxiliares a USING(idorigenp,idproducto,idauxiliar)"
    + " INNER JOIN productos pr USING(idproducto)"
    + " WHERE a.estatus=2"
    + " AND a.idorigen=?1"
    + " AND a.idgrupo=?2"
    + " AND a.idsocio=?3"
    + " AND pr.tipoproducto=0"
    + " AND ad.periodo=?4"
    + " AND ad.cargoabono=1", nativeQuery = true)
	Double montoUdis(Integer idorigen,Integer idgrupo,Integer idsocio,String periodo);  
}
