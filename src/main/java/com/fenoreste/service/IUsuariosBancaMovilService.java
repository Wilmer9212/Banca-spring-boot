package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.UsuariosBancaMovil;

@Service
public interface IUsuariosBancaMovilService {
	
public UsuariosBancaMovil findByOgs(Integer idorigen,Integer idgrupo,Integer idsocio);
	
	public UsuariosBancaMovil insertar(UsuariosBancaMovil user);
	
	public void save(Integer idorigen, Integer idgrupo, Integer idsocio,String alias,Integer idorigenp,Integer idproducto,Integer idauxiliar,boolean estatus);
	
	public void eliminar(Integer idorigen,Integer idgrupo,Integer idsocio); 
	
	public void modificar(String username,Integer idorigen,Integer idgrupo,Integer idsocio);	
	
	public void update(UsuariosBancaMovil user);

}
