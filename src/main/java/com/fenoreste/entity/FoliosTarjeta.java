package com.fenoreste.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ws_siscoop_folios_tarjetas")
public class FoliosTarjeta implements Serializable {

	@Id
	@Column(name = "idorigenp")
	private int idorigenp;
	@Column(name = "idproducto")
	private int idproducto;
	@Column(name = "idauxiliar")
	private int idauxiliar;
	@Column(name = "idtarjeta")
	private String idtarjeta;
	@Column(name = "asignada")
	private Boolean asignada;
	@Column(name = "activa")
	private Boolean activa;
	@Column(name = "bloqueada")
	private Boolean bloqueada;
	@Column(name = "fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHora;

	public FoliosTarjeta() {
		// TODO Auto-generated constructor stub
	}


	public int getIdorigenp() {
		return idorigenp;
	}


	public void setIdorigenp(int idorigenp) {
		this.idorigenp = idorigenp;
	}


	public int getIdproducto() {
		return idproducto;
	}


	public void setIdproducto(int idproducto) {
		this.idproducto = idproducto;
	}


	public int getIdauxiliar() {
		return idauxiliar;
	}


	public void setIdauxiliar(int idauxiliar) {
		this.idauxiliar = idauxiliar;
	}


	public String getIdtarjeta() {
		return idtarjeta;
	}


	public void setIdtarjeta(String idtarjeta) {
		this.idtarjeta = idtarjeta;
	}


	public Boolean getAsignada() {
		return asignada;
	}


	public void setAsignada(Boolean asignada) {
		this.asignada = asignada;
	}


	public Boolean getActiva() {
		return activa;
	}


	public void setActiva(Boolean activa) {
		this.activa = activa;
	}


	public Boolean getBloqueada() {
		return bloqueada;
	}


	public void setBloqueada(Boolean bloqueada) {
		this.bloqueada = bloqueada;
	}


	public Date getFechaHora() {
		return fechaHora;
	}


	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}


	private static final long serialVersionUID = 1L;

}
