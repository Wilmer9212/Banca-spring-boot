package com.fenoreste.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "banca_movil_usuarios_bankingly")
public class UsuariosBancaMovil implements Serializable {

	    private static final long serialVersionUID = 1L;
	    
	    @Id
	    @Column(name="idorigen")
	    private Integer idorigen;
	    @Column(name="idgrupo")
	    private Integer idgrupo;
	    @Column(name="idsocio")
	    private Integer idsocio; 
	    @Column(name="alias_usuario")
	    private String alias_usuario;
	    @Column(name="idorigenp")
	    private int idorigenp;
	    @Column(name = "idproducto")
	    private int idproducto;
	    @Column(name="idauxiliar")
	    private int idauxiliar;
	    @Column(name="estatus")
	    private boolean estatus;

	    public UsuariosBancaMovil() {
			
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

		public String getAlias_usuario() {
			return alias_usuario;
		}

		public void setAlias_usuario(String alias_usuario) {
			this.alias_usuario = alias_usuario;
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

		public boolean isEstatus() {
			return estatus;
		}

		public void setEstatus(boolean estatus) {
			this.estatus = estatus;
		}
	    
	
	   

}
