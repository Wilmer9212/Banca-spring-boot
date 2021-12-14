package com.fenoreste.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.AmortizacionesRepository;
import com.fenoreste.entity.Amortizacion;

@Service
public class AmortizacionesServiceImpl implements IAmortizacionesService {

	@Autowired
	AmortizacionesRepository amortizacionesDao;

	@Override
	public Integer findCountAmortizacionesPagadas(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findCountAmortizacionesPagadas(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public Integer findCountAmortizaciones(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findCountAmortizaciones(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public List<Object[]> findProximaAmortizacion(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findProximaAmortizacion(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public List<Object[]> findAmortizacionByIdamortizacion(Integer idorigenp, Integer idproducto, Integer idauxiliar,
			Integer idamortizacion) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAmortizacionByIdamortizacion(idorigenp, idproducto, idauxiliar, idamortizacion);
	}

	@Override
	public List<Object[]> findAmortizaciones(Integer idorigenp, Integer idproducto,Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAmortizaciones(idorigenp, idproducto, idauxiliar,pageable);
	}

	@Override
	public List<Object[]> findAmortizacionesPagadasIdamortizacionAsc(Integer idorigenp, Integer idproducto,
			Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAmortizacionesPagadasIdamortizacionAsc(idorigenp, idproducto, idauxiliar,pageable);
	}

	@Override
	public List<Object[]> findAmortizacionesActivasIdamortizacionAsc(Integer idorigenp, Integer idproducto,
			Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAmortizacionesActivasIdamortizacionAsc(idorigenp, idproducto, idauxiliar,pageable);
	}

	@Override
	public List<Object[]> findAmortizacionesPagadas(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAmortizacionesPagadas(idorigenp, idproducto, idauxiliar,pageable);
	}

	@Override
	public List<Object[]> findAmortizacionesActivas(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAmortizacionesActivas(idorigenp, idproducto, idauxiliar,pageable);
	}

	@Override
	public Integer findCountAmortizacionesActivas(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findCountAmortizacionesActivas(idorigenp, idproducto, idauxiliar);
	}

	@Override
	public List<Object[]> findAmortizacionesTotal(Integer idorigenp, Integer idproducto, Integer idauxiliar,Pageable pageable) {
        
		return amortizacionesDao.findAmortizacionesTotal(idorigenp, idproducto, idauxiliar, pageable);
	}

	@Override
	public List<Amortizacion> findAll(Integer idorigenp, Integer idproducto, Integer idauxiliar, Pageable pageable) {
		// TODO Auto-generated method stub
		return amortizacionesDao.findAll(idorigenp, idproducto, idauxiliar, pageable);
	}


	
	

}
