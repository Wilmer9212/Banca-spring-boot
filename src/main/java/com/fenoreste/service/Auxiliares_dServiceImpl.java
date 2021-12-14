package com.fenoreste.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.Auxiliares_dRepository;
import com.fenoreste.dto.AuxiliaresdDTO;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Auxiliares_d;

@Service
public class Auxiliares_dServiceImpl implements IAuxiliares_dService{

	@Autowired
	Auxiliares_dRepository auxiliares_dDao;
	
	@Autowired
	JdbcTemplate jdbc;
	
	
	@Override
	public List<Auxiliares_d> findAuxiliares_dByOpa(Integer idorigenp, Integer  idproducto, Integer  idauxiliar) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findAuxiliares_dByOpa(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public List<Auxiliares_d> findAuxiliares_dByOpaFecha(Integer  idorigenp, Integer  idproducto, Integer  idauxiliar, Date fechaI,
			Date fechaF) {
		return auxiliares_dDao.findAuxiliares_dByOpaFecha(idorigenp, idproducto, idauxiliar, fechaI, fechaF);
	}

	@Override
	public Double findSaldoUltimas24Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findSaldoUltimas24Horas(idorigenp, idproducto, idauxiliar, fecha);
	}

	@Override
	public Double findSaldoUltimas48Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findSaldoUltimas48Horas(idorigenp, idproducto, idauxiliar, fecha);
	}

	@Override
	public Auxiliares_d findUltimoRegistro(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		int size =jdbc.query("SELECT * FROM auxiliares_d WHERE idorigenp="+idorigenp+" AND idproducto="+idproducto+" AND idauxiliar="+idauxiliar+" ORDER BY fecha DESC limit 1",new BeanPropertyRowMapper<>(Auxiliares_d.class)).size();
		Auxiliares_d ad=null;
		if(size>0) {
			ad = jdbc.query("SELECT * FROM auxiliares_d WHERE idorigenp="+idorigenp+" AND idproducto="+idproducto+" AND idauxiliar="+idauxiliar+" ORDER BY fecha DESC limit 1",new BeanPropertyRowMapper<>(Auxiliares_d.class)).get(0);
		}
		return ad;//auxiliares_dDao.findUltimoRegistro(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public Double findSaldoMasDe48Horas(Integer idorigenp, Integer idproducto, Integer idauxiliar, Date fecha) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findSaldoMasDe48Horas(idorigenp, idproducto, idauxiliar, fecha);
	}

	@Override
	public List<Object[]> findUltimos5Movimientos(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findUltimos5Movimientos(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public List<Object[]> findMovimientosFechaAscRF(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date FInicio, Date FFinal, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaAscRF(idorigenp, idproducto, idauxiliar, FInicio, FFinal, pageable);
	}

	@Override
	public List<Object[]> findMovimientosFechaAscFI(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date FInicio, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaAscFI(idorigenp, idproducto, idauxiliar, FInicio, pageable);
	}

	@Override
	public List<Object[]> findMovimientosFechaAscFF(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date FFinal, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaAscFF(idorigenp, idproducto, idauxiliar,FFinal, pageable);
	}
	
	@Override
	public List<Object[]> findMovimientosFechaAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaAsc(idorigenp, idproducto, idauxiliar, pageable);
	}
	
	@Override
	public List<Object[]> findMovimientosFechaDescRF(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date FInicio, Date FFinal, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaDescRF(idorigenp, idproducto, idauxiliar, FInicio, FFinal, pageable);
	}

	@Override
	public List<Object[]> findMovimientosFechaDescFI(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date FInicio, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaDescFI(idorigenp, idproducto, idauxiliar, FInicio, pageable);
	}

	@Override
	public List<Object[]> findMovimientosFechaDescFF(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Date FFinal, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaDescFF(idorigenp, idproducto, idauxiliar, FFinal, pageable);
	}

	@Override
	public List<Object[]> findMovimientosFechaDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaDesc(idorigenp, idproducto, idauxiliar, pageable);
	}
	
	@Override
	public List<Object[]> findMovimientosFechaDefault(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosFechaDefault(idorigenp, idproducto, idauxiliar, pageable);
	}
	
	

	
	@Override
	public List<Object[]> findMovimientosPolizaAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosPolizaAsc(idorigenp, idproducto, idauxiliar, pageable);
	}

	@Override
	public List<Object[]> findMovimientosPolizaDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosPolizaDesc(idorigenp, idproducto, idauxiliar, pageable);
	}

	@Override
	public List<Object[]> findMovimientosPoliza(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.findMovimientosPoliza(idorigenp, idproducto, idauxiliar,pageable);
	}

	@Override
	public List<Object[]> findMovimientosCargoAbonoAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosCargoAbonoDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosCargoAbono(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosMontoAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosMontoDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosMonto(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosSaldoecAsc(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosSaldoecDesc(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object[]> findMovimientosSaldoec(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double montoPesos(Integer idorigen, Integer idgrupo, Integer idsocio, Date fecha) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.montoPesos(idorigen, idgrupo, idsocio, fecha);
	}

	@Override
	public Double montoUdis(Integer idorigen, Integer idgrupo, Integer idsocio, String periodo) {
		// TODO Auto-generated method stub
		return auxiliares_dDao.montoUdis(idorigen, idgrupo, idsocio, periodo);
	}

	@Override
	public Auxiliares_d findByTransaccion(Integer transaccion) {
		String consulta = "SELECT * FROM auxiliares_d WHERE transaccion="+transaccion;
		int size = jdbc.query(consulta, new BeanPropertyRowMapper<>(Auxiliares_d.class)).size();
		System.out.println("Consulta : "+ consulta);
		Auxiliares_d ad = null;
		if(size > 0) {
			ad = jdbc.query("SELECT * FROM auxiliares_d WHERE transaccion="+transaccion, new BeanPropertyRowMapper<>(Auxiliares_d.class)).get(0);
		}
	return ad;
	}


	
	
}