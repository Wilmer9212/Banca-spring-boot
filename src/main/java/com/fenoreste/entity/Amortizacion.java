/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fenoreste.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Elliot
 */

@Entity
@Table(name = "amortizaciones")
@Cacheable(false)
public class Amortizacion implements Serializable {   
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="idorigenp")
    private Integer idorigenp;
    @Column(name="idproducto")
    private Integer idproducto;
    @Column(name="idauxiliar")
    private Integer idauxiliar; 
    @Column(name="idamortizacion")
    private Integer idamortizacion;
    @Column(name = "vence")
    @Temporal(TemporalType.DATE)
    private Date vence;
    @Column(name = "abono")
    private BigDecimal abono;
    @Column(name = "io")
    private BigDecimal io;
    @Column(name = "abonopag")
    private BigDecimal abonopag;
    @Column(name = "iopag")
    private BigDecimal iopag;
    @Column(name = "bonificado")
    private Boolean bonificado;
    @Column(name = "pagovariable")
    private Boolean pagovariable;
    @Column(name = "todopag")
    private Boolean todopag;
    @Column(name = "atiempo")
    private Boolean atiempo;
    @Column(name = "bonificacion")
    private BigDecimal bonificacion;
    @Column(name = "anualidad")
    private BigDecimal anualidad;
    @Column(name = "diasvencidos")
    private Integer diasvencidos;

    
    public Amortizacion(Integer idorigenp, Integer idproducto, Integer idauxiliar, Integer idamortizacion, Date vence,
			BigDecimal abono, BigDecimal io, BigDecimal abonopag, BigDecimal iopag, Boolean bonificado,
			Boolean pagovariable, Boolean todopag, Boolean atiempo, BigDecimal bonificacion, BigDecimal anualidad,
			Integer diasvencidos) {
		this.idorigenp = idorigenp;
		this.idproducto = idproducto;
		this.idauxiliar = idauxiliar;
		this.idamortizacion = idamortizacion;
		this.vence = vence;
		this.abono = abono;
		this.io = io;
		this.abonopag = abonopag;
		this.iopag = iopag;
		this.bonificado = bonificado;
		this.pagovariable = pagovariable;
		this.todopag = todopag;
		this.atiempo = atiempo;
		this.bonificacion = bonificacion;
		this.anualidad = anualidad;
		this.diasvencidos = diasvencidos;
	}

	public Amortizacion() {
    
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

	public Integer getIdamortizacion() {
		return idamortizacion;
	}

	public void setIdamortizacion(Integer idamortizacion) {
		this.idamortizacion = idamortizacion;
	}

	public Date getVence() {
		return vence;
	}

	public void setVence(Date vence) {
		this.vence = vence;
	}

	public BigDecimal getAbono() {
		return abono;
	}

	public void setAbono(BigDecimal abono) {
		this.abono = abono;
	}

	public BigDecimal getIo() {
		return io;
	}

	public void setIo(BigDecimal io) {
		this.io = io;
	}

	public BigDecimal getAbonopag() {
		return abonopag;
	}

	public void setAbonopag(BigDecimal abonopag) {
		this.abonopag = abonopag;
	}

	public BigDecimal getIopag() {
		return iopag;
	}

	public void setIopag(BigDecimal iopag) {
		this.iopag = iopag;
	}

	public Boolean getBonificado() {
		return bonificado;
	}

	public void setBonificado(Boolean bonificado) {
		this.bonificado = bonificado;
	}

	public Boolean getPagovariable() {
		return pagovariable;
	}

	public void setPagovariable(Boolean pagovariable) {
		this.pagovariable = pagovariable;
	}

	public Boolean getTodopag() {
		return todopag;
	}

	public void setTodopag(Boolean todopag) {
		this.todopag = todopag;
	}

	public Boolean getAtiempo() {
		return atiempo;
	}

	public void setAtiempo(Boolean atiempo) {
		this.atiempo = atiempo;
	}

	public BigDecimal getBonificacion() {
		return bonificacion;
	}

	public void setBonificacion(BigDecimal bonificacion) {
		this.bonificacion = bonificacion;
	}

	public BigDecimal getAnualidad() {
		return anualidad;
	}

	public void setAnualidad(BigDecimal anualidad) {
		this.anualidad = anualidad;
	}

	public Integer getDiasvencidos() {
		return diasvencidos;
	}

	public void setDiasvencidos(Integer diasvencidos) {
		this.diasvencidos = diasvencidos;
	}

	@Override
	public String toString() {
		return "Amortizaciones [idorigenp=" + idorigenp + ", idproducto=" + idproducto + ", idauxiliar=" + idauxiliar
				+ ", idamortizacion=" + idamortizacion + ", vence=" + vence + ", abono=" + abono + ", io=" + io
				+ ", abonopag=" + abonopag + ", iopag=" + iopag + ", bonificado=" + bonificado + ", pagovariable="
				+ pagovariable + ", todopag=" + todopag + ", atiempo=" + atiempo + ", bonificacion=" + bonificacion
				+ ", anualidad=" + anualidad + ", diasvencidos=" + diasvencidos + "]";
	}
   
	
    
}
