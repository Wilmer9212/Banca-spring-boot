package com.fenoreste.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author wilmer
 */
@Entity
@Table(name = "bankingly_movimientos_ca")
public class RegistraMovimiento implements Serializable {
    @Id
    @Column(name="idproducto")
    private Integer idproducto;
    @Column(name="idorigenp")
    private Integer idorigenp;    
    @Column(name = "idauxiliar")
    private Integer idauxiliar;
    @Column(name = "fecha")
    private Timestamp fecha;
    @Column(name = "idusuario")
    private Integer idusuario;
    @Column(name = "sesion")
    private String sesion;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "idorigen")
    private Integer idorigen;
    @Column(name = "idgrupo")
    private Integer idgrupo;
    @Column(name = "idsocio")
    private Integer idsocio;
    @Column(name = "cargoabono")
    private Integer cargoabono;
    @Column(name = "monto")
    private Double monto;
    @Column(name = "idcuenta")
    private String idcuenta;
    @Column(name = "iva")
    private Double iva;
    @Column(name = "tipo_amort")
    private Integer tipo_amort;   
    @Column(name = "sai_aux")
    private String sai_aux;

    public RegistraMovimiento() {
		// TODO Auto-generated constructor stub
	}
    
    
    public Integer getIdorigenp() {
		return idorigenp;
	}


	public void setIdorigenp(Integer idorigenp) {
		this.idorigenp = idorigenp;
	}


	public Integer getIdproducto() {
		return idproducto;
	}


	public void setIdproducto(Integer idproducto) {
		this.idproducto = idproducto;
	}


	public Integer getIdauxiliar() {
		return idauxiliar;
	}


	public void setIdauxiliar(Integer idauxiliar) {
		this.idauxiliar = idauxiliar;
	}


	public Timestamp getFecha() {
		return fecha;
	}


	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}


	public Integer getIdusuario() {
		return idusuario;
	}


	public void setIdusuario(Integer idusuario) {
		this.idusuario = idusuario;
	}


	public String getSesion() {
		return sesion;
	}


	public void setSesion(String sesion) {
		this.sesion = sesion;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}


	public Integer getIdorigen() {
		return idorigen;
	}


	public void setIdorigen(Integer idorigen) {
		this.idorigen = idorigen;
	}


	public Integer getIdgrupo() {
		return idgrupo;
	}


	public void setIdgrupo(Integer idgrupo) {
		this.idgrupo = idgrupo;
	}


	public Integer getIdsocio() {
		return idsocio;
	}


	public void setIdsocio(Integer idsocio) {
		this.idsocio = idsocio;
	}


	public Integer getCargoabono() {
		return cargoabono;
	}


	public void setCargoabono(Integer cargoabono) {
		this.cargoabono = cargoabono;
	}


	public Double getMonto() {
		return monto;
	}


	public void setMonto(Double monto) {
		this.monto = monto;
	}


	public String getIdcuenta() {
		return idcuenta;
	}


	public void setIdcuenta(String idcuenta) {
		this.idcuenta = idcuenta;
	}


	public Double getIva() {
		return iva;
	}


	public void setIva(Double iva) {
		this.iva = iva;
	}


	public Integer getTipo_amort() {
		return tipo_amort;
	}


	public void setTipo_amort(Integer tipo_amort) {
		this.tipo_amort = tipo_amort;
	}


	public String getSai_aux() {
		return sai_aux;
	}


	public void setSai_aux(String sai_aux) {
		this.sai_aux = sai_aux;
	}


	private static final long serialVersionUID = 1L;

    
}
