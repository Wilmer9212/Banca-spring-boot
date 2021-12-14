package com.fenoreste.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Persona;
import com.fenoreste.service.TablasServiceImpl;


public interface PersonaRepository extends CrudRepository<Persona,Long> {
   		
	//Buscar persona para matricular
	@Query(value = "SELECT * FROM personas WHERE"
			+ " replace(upper(nombre),' ','')=?1 "
			+ " and replace(upper(appaterno||apmaterno),' ','')=?2 "
			+ " and upper(curp)=?3 "
			+ " and upper((CASE WHEN email IS NULL THEN '' ELSE trim(email) END))=?4 "
			+ " and (CASE WHEN celular IS NULL THEN '' ELSE trim(celular) END)=?5 "
			+ " and (CASE WHEN telefono IS NULL THEN '' ELSE trim(telefono) END)=?6" , nativeQuery = true)
	Persona findPersonaMatriculacion(String nombre,String apellidos,String curp,String email,String celular,String telefono);	
	@Query(value = "SELECT * FROM personas WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3" , nativeQuery = true)
	Persona findByOGS(int idorigen,int idgrupo,int idsocio);
	@Query(value = "SELECT nombre,appaterno FROM personas WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3" , nativeQuery = true)
	List<String> findByOGS2(Integer idorigen,int idgrupo,int idsocio);
	
	
}
