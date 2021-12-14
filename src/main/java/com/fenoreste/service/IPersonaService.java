package com.fenoreste.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.fenoreste.entity.Persona;

@Service
public interface IPersonaService {
    public List<Persona> findAll();		
	public Persona findById(Long id);	
	public Persona findByOGS(Integer idorigen,Integer idgrupo,Integer idsocio);
	public List<String> findByOGS2(Integer idorigen,Integer idgrupo,Integer idsocio);
	public Persona save(Persona persona);
	public void deleteById(Long id);
	public Persona findPersonaMatriculacion(String nombre,String apellidos,String curp,String email,String celular,String telefono);

}
