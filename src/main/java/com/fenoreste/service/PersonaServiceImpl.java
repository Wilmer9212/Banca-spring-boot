package com.fenoreste.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenoreste.dao.PersonaRepository;
import com.fenoreste.entity.Persona;


@Service
public class PersonaServiceImpl implements IPersonaService {
    
	@Autowired
	PersonaRepository personaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {
		return (List<Persona>) personaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Persona findById(Long id) {
		return personaDao.findById(id).orElse(null);
	}
	
	@Override
	public Persona findByOGS(Integer idorigen, Integer idgrupo, Integer idsocio) {
		return personaDao.findByOGS(idorigen, idgrupo, idsocio);
	}
	
	@Override
	public List<String>findByOGS2(Integer idorigen, Integer idgrupo, Integer idsocio) {
		return personaDao.findByOGS2(idorigen, idgrupo, idsocio);
	}

	@Override
	public Persona save(Persona persona) {
		return personaDao.save(persona);
	}

	@Override
	public void deleteById(Long id) {
		personaDao.deleteById(id);		
	}

	
	@Override
	public Persona findPersonaMatriculacion(String nombre,String apellidos,String curp,String email,String celular,String telefono) {
	return personaDao.findPersonaMatriculacion(nombre,apellidos,curp,email,celular,telefono);
	}
}
