package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.UsuariosBancaMovilRepository;
import com.fenoreste.entity.UsuariosBancaMovil;

@Service
public class UsuariosBancaMovilServiceImpl  implements IUsuariosBancaMovilService{
	
	@Autowired
	UsuariosBancaMovilRepository usuariosDao;
	@Override
	public UsuariosBancaMovil findByOgs(Integer idorigen, Integer idgrupo, Integer idsocio) {
		// TODO Auto-generated method stub
		return usuariosDao.findByOgs(idorigen, idgrupo, idsocio);
	}
	@Override
	public UsuariosBancaMovil insertar(UsuariosBancaMovil user) {
		// TODO Auto-generated method stub
		return usuariosDao.save(user);
	}
	@Override
	public void eliminar(Integer idorigen, Integer idgrupo, Integer idsocio) {
	usuariosDao.eliminar(idorigen,idgrupo,idsocio);		
	}
	@Override
	public void modificar(String username, Integer idorigen, Integer idgrupo, Integer idsocio) {
	usuariosDao.modificar(username, idorigen, idgrupo, idsocio);
	}
	@Override
	public void save(Integer idorigen, Integer idgrupo, Integer idsocio, String alias, Integer idorigenp,Integer idproducto, Integer idauxiliar, boolean estatus) {
		usuariosDao.save(idorigen, idgrupo, idsocio, alias, idorigenp, idproducto, idauxiliar, estatus);
		
	}
	@Override
	public void update(UsuariosBancaMovil user) {	 
	usuariosDao.save(user);
	}
	
	
}
