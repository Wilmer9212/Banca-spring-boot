package com.fenoreste.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Auxiliares;

@Service
public interface IAuxiliaresService { 
	public Auxiliares findAuxiliaresByOPA(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	public Auxiliares findAuxiliarBancaMovil(Integer idorigen,Integer idgrupo,Integer idsocio,Integer idproducto);
	
	public List<Auxiliares>AuxiliaresActivos(Integer idorigen,Integer idgrupo,Integer idsocio);
	
	public List<Auxiliares> findListaCuentasBankingly(Integer idorigen,Integer idgrupo,Integer idsocio,Integer productType);
	
	public List<Auxiliares> findListaCuentasBankinglySinType(Integer idorigen, Integer idgrupo, Integer idsocio);
	
	public Auxiliares AuxiliarByOpa(Integer idorigenp,Integer idproducto,Integer idauxiliar);
	
	//Solo CSN
	public Auxiliares findAuxiliarTdd(Integer idorigen,Integer idgrupo,Integer idsocio,Integer idproducto);
	
	
	
}
