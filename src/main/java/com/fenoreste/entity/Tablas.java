/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fenoreste.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.cache.annotation.CacheEvict;


/**
 *
 * @author Elliot
 */
@Entity
@Table(name = "tablas")
public class Tablas implements Serializable {    
    /**
	 * 
	 */
	
	@Id
    @Column(name="idtabla")
    private String idtabla;
    @Column(name="idelemento")
    private String idelemento;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "dato1")
    private String dato1;
    @Column(name = "dato2")
    private String dato2;
    @Column(name = "dato3")
    private String dato3;
    @Column(name = "dato4")
    private String dato4;
    @Column(name = "dato5")
    private String dato5;
    @Column(name = "tipo")
    private short tipo;

    public Tablas() {
    }
   
    public String getIdtabla() {
		return idtabla;
	}

	public void setIdtabla(String idtabla) {
		this.idtabla = idtabla;
	}

	public String getIdelemento() {
		return idelemento;
	}
	
	public void setIdelemento(String idelemento) {
		this.idelemento = idelemento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDato1() {
		return dato1;
	}

	public void setDato1(String dato1) {
		this.dato1 = dato1;
	}

	public String getDato2() {
		return dato2;
	}

	public void setDato2(String dato2) {
		this.dato2 = dato2;
	}

	public String getDato3() {
		return dato3;
	}

	public void setDato3(String dato3) {
		this.dato3 = dato3;
	}

	public String getDato4() {
		return dato4;
	}

	public void setDato4(String dato4) {
		this.dato4 = dato4;
	}

	public String getDato5() {
		return dato5;
	}

	public void setDato5(String dato5) {
		this.dato5 = dato5;
	}

	public short getTipo() {
		return tipo;
	}

	public void setTipo(short tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Tablas [idtabla=" + idtabla + ", idelemento=" + idelemento + ", nombre=" + nombre + ", dato1=" + dato1
				+ ", dato2=" + dato2 + ", dato3=" + dato3 + ", dato4=" + dato4 + ", dato5=" + dato5 + ", tipo=" + tipo
				+ "]";
	}

	private static final long serialVersionUID = -5945623366191309245L;

}
