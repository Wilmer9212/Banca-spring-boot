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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Elliot
 */

@Entity
@Table(name = "auxiliares_d")
@Cacheable(false)
public class Auxiliares_d {

    @Id
    @Column(name="idorigenp")
    private Integer idorigenp;
    @Column(name="idproducto")
    private Integer idproducto;
    @Column(name="idauxiliar")
    private Integer idauxiliar;
    @Column(name = "cargoabono")
    private Short cargoabono;
    @Column(name = "monto")
    private BigDecimal monto;
    @Column(name = "montoio")
    private BigDecimal montoio;
    @Column(name = "montoim")
    private BigDecimal montoim;
    @Column(name = "montoiva")
    private BigDecimal montoiva;
    @Column(name = "idorigenc")
    private Integer idorigenc;
    @Column(name = "periodo")
    private String periodo;
    @Column(name = "idtipo")
    private Short idtipo;
    @Column(name = "idpoliza")
    private Integer idpoliza;
    @Column(name = "tipomov")
    private Short tipomov;
    @Column(name = "saldoec")
    private BigDecimal saldoec;
    @Column(name = "transaccion")
    private Integer transaccion;
    @Column(name = "montoivaim")
    private BigDecimal montoivaim;
    @Column(name = "efectivo")
    private BigDecimal efectivo;
    @Column(name = "diasvencidos")
    private int diasvencidos;
    @Column(name = "montovencido")
    private BigDecimal montovencido;
    @Column(name = "ticket")
    private Integer ticket;
    @Column(name = "montoidnc")
    private BigDecimal montoidnc;
    @Column(name = "montoieco")
    private BigDecimal montoieco;
    @Column(name = "montoidncm")
    private BigDecimal montoidncm;
    @Column(name = "montoiecom")
    private BigDecimal montoiecom;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    
    public Auxiliares_d() {
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
	
	public Short getCargoabono() {
		return cargoabono;
	}


	public void setCargoabono(Short cargoabono) {
		this.cargoabono = cargoabono;
	}


	public BigDecimal getMonto() {
		return monto;
	}


	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}


	public BigDecimal getMontoio() {
		return montoio;
	}


	public void setMontoio(BigDecimal montoio) {
		this.montoio = montoio;
	}


	public BigDecimal getMontoim() {
		return montoim;
	}


	public void setMontoim(BigDecimal montoim) {
		this.montoim = montoim;
	}


	public BigDecimal getMontoiva() {
		return montoiva;
	}


	public void setMontoiva(BigDecimal montoiva) {
		this.montoiva = montoiva;
	}


	public Integer getIdorigenc() {
		return idorigenc;
	}


	public void setIdorigenc(Integer idorigenc) {
		this.idorigenc = idorigenc;
	}


	public String getPeriodo() {
		return periodo;
	}


	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}


	public Short getIdtipo() {
		return idtipo;
	}


	public void setIdtipo(Short idtipo) {
		this.idtipo = idtipo;
	}


	public Integer getIdpoliza() {
		return idpoliza;
	}


	public void setIdpoliza(Integer idpoliza) {
		this.idpoliza = idpoliza;
	}


	public Short getTipomov() {
		return tipomov;
	}


	public void setTipomov(Short tipomov) {
		this.tipomov = tipomov;
	}


	public BigDecimal getSaldoec() {
		return saldoec;
	}


	public void setSaldoec(BigDecimal saldoec) {
		this.saldoec = saldoec;
	}


	public Integer getTransaccion() {
		return transaccion;
	}


	public void setTransaccion(Integer transaccion) {
		this.transaccion = transaccion;
	}


	public BigDecimal getMontoivaim() {
		return montoivaim;
	}


	public void setMontoivaim(BigDecimal montoivaim) {
		this.montoivaim = montoivaim;
	}


	public BigDecimal getEfectivo() {
		return efectivo;
	}


	public void setEfectivo(BigDecimal efectivo) {
		this.efectivo = efectivo;
	}


	public int getDiasvencidos() {
		return diasvencidos;
	}


	public void setDiasvencidos(int diasvencidos) {
		this.diasvencidos = diasvencidos;
	}


	public BigDecimal getMontovencido() {
		return montovencido;
	}


	public void setMontovencido(BigDecimal montovencido) {
		this.montovencido = montovencido;
	}


	public Integer getTicket() {
		return ticket;
	}


	public void setTicket(Integer ticket) {
		this.ticket = ticket;
	}


	public BigDecimal getMontoidnc() {
		return montoidnc;
	}


	public void setMontoidnc(BigDecimal montoidnc) {
		this.montoidnc = montoidnc;
	}


	public BigDecimal getMontoieco() {
		return montoieco;
	}


	public void setMontoieco(BigDecimal montoieco) {
		this.montoieco = montoieco;
	}


	public BigDecimal getMontoidncm() {
		return montoidncm;
	}


	public void setMontoidncm(BigDecimal montoidncm) {
		this.montoidncm = montoidncm;
	}


	public BigDecimal getMontoiecom() {
		return montoiecom;
	}


	public void setMontoiecom(BigDecimal montoiecom) {
		this.montoiecom = montoiecom;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	
    

}

