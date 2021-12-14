package com.fenoreste.entity;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author root
 */

@Entity
@Table(name = "colonias")
public class Colonia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idcolonia")
    private Integer idcolonia;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "idmunicipio")
    private Integer idmunicipio;
    @Column(name = "codigopostal")
    private String codigopostal;

    public Colonia() {
    }

    public Colonia(Integer idcolonia) {
        this.idcolonia = idcolonia;
    }

    public Colonia(Integer idcolonia, String nombre) {
        this.idcolonia = idcolonia;
        this.nombre = nombre;
    }

    public Integer getIdcolonia() {
        return idcolonia;
    }

    public void setIdcolonia(Integer idcolonia) {
        this.idcolonia = idcolonia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdmunicipio() {
        return idmunicipio;
    }

    public void setIdmunicipio(Integer idmunicipio) {
        this.idmunicipio = idmunicipio;
    }

    public String getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(String codigopostal) {
        this.codigopostal = codigopostal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcolonia != null ? idcolonia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Colonia)) {
            return false;
        }
        Colonia other = (Colonia) object;
        return !((this.idcolonia == null && other.idcolonia != null) || (this.idcolonia != null && !this.idcolonia.equals(other.idcolonia)));
    }

    @Override
    public String toString() {
        return "Colonias{" + "idcolonia=" + idcolonia + ", nombre=" + nombre + ", idmunicipio=" + idmunicipio + ", codigopostal=" + codigopostal + '}';
    }

   

}


