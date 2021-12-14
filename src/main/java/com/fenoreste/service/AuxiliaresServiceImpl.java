package com.fenoreste.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenoreste.dao.AuxiliaresRepository;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Tablas;

@Service
public class AuxiliaresServiceImpl implements IAuxiliaresService {

	@Autowired
	AuxiliaresRepository auxiliaresDao;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	@Transactional(readOnly = true)
	public Auxiliares findAuxiliaresByOPA(Integer idorigenp, Integer idproducto, Integer idauxiliar) {
	
	return auxiliaresDao.findAuxiliarByOPA(idorigenp, idproducto, idauxiliar);
	}

	@Override
	@Transactional(readOnly = true)
	public Auxiliares findAuxiliarBancaMovil(Integer idorigen, Integer idgrupo, Integer idsocio,Integer idproducto) {
		return auxiliaresDao.findAuxiliarBancaMovil(idorigen, idgrupo, idsocio,idproducto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Auxiliares> AuxiliaresActivos(Integer idorigen, Integer idgrupo, Integer idsocio) {
		return auxiliaresDao.AuxiliaresActivos(idorigen, idgrupo, idsocio);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Auxiliares findAuxiliarTdd(Integer idorigen, Integer idgrupo, Integer idsocio,Integer idproducto) {
		return auxiliaresDao.findAuxiliarTdd(idorigen, idgrupo, idsocio,idproducto);
	}

	@Override
	public List<Auxiliares> findListaCuentasBankingly(Integer idorigen,Integer idgrupo, Integer idsocio,Integer productType) {
		return auxiliaresDao.findListaCuentasBankingly(idorigen, idgrupo, idsocio,productType);
	}

	@Override
	public List<Auxiliares> findListaCuentasBankinglySinType(Integer idorigen, Integer idgrupo, Integer idsocio) {
		int size=  jdbc.query("SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly tp USING(idproducto) WHERE idorigen="+idorigen+" AND idgrupo="+idgrupo+" AND idsocio="+idsocio+" AND estatus=2",new BeanPropertyRowMapper<>(Auxiliares.class)).size();
		List<Auxiliares>lista=new ArrayList<Auxiliares>();	
       
		if(size > 0) {
			lista=jdbc.query("SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly tp USING(idproducto) WHERE idorigen="+idorigen+" AND idgrupo="+idgrupo+" AND idsocio="+idsocio+" AND estatus=2",new BeanPropertyRowMapper<>(Auxiliares.class));
	    }		
		return lista;// auxiliaresDao.findListaCuentasBankinglySinType(idorigen, idgrupo, idsocio);
	}

	@Override
	public Auxiliares AuxiliarByOpa(Integer idorigenp,Integer idproducto, Integer idauxiliar) {
		String consulta = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly tp USING(idproducto) WHERE idorigenp="+idorigenp+" AND idproducto="+idproducto+" AND idauxiliar="+idauxiliar+" AND estatus=2";
		int size=  jdbc.query(consulta,new BeanPropertyRowMapper<>(Auxiliares.class)).size();
		Auxiliares a=null;		
		if(size > 0) {
			a=jdbc.query("SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly tp USING(idproducto) WHERE idorigenp="+idorigenp+" AND idproducto="+idproducto+" AND idauxiliar="+idauxiliar+" AND estatus=2",new BeanPropertyRowMapper<>(Auxiliares.class)).get(0);
	    }		
		return a;
	}
	

}
