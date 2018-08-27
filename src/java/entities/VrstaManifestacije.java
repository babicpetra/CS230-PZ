/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Petra
 */
@Entity
@Table(name = "vrsta_manifestacije")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VrstaManifestacije.findAll", query = "SELECT v FROM VrstaManifestacije v")
    , @NamedQuery(name = "VrstaManifestacije.findByIdVrstaManifestacije", query = "SELECT v FROM VrstaManifestacije v WHERE v.idVrstaManifestacije = :idVrstaManifestacije")
    , @NamedQuery(name = "VrstaManifestacije.findByNaziv", query = "SELECT v FROM VrstaManifestacije v WHERE v.naziv = :naziv")})
public class VrstaManifestacije implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_VRSTA_MANIFESTACIJE")
    private Integer idVrstaManifestacije;
    @Size(max = 100)
    @Column(name = "NAZIV")
    private String naziv;
    @OneToMany(mappedBy = "idVrstaManifestacije")
    private Collection<Manifestacija> manifestacijaCollection;

    public VrstaManifestacije() {
    }

    public VrstaManifestacije(Integer idVrstaManifestacije) {
        this.idVrstaManifestacije = idVrstaManifestacije;
    }

    public Integer getIdVrstaManifestacije() {
        return idVrstaManifestacije;
    }

    public void setIdVrstaManifestacije(Integer idVrstaManifestacije) {
        this.idVrstaManifestacije = idVrstaManifestacije;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public Collection<Manifestacija> getManifestacijaCollection() {
        return manifestacijaCollection;
    }

    public void setManifestacijaCollection(Collection<Manifestacija> manifestacijaCollection) {
        this.manifestacijaCollection = manifestacijaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVrstaManifestacije != null ? idVrstaManifestacije.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VrstaManifestacije)) {
            return false;
        }
        VrstaManifestacije other = (VrstaManifestacije) object;
        if ((this.idVrstaManifestacije == null && other.idVrstaManifestacije != null) || (this.idVrstaManifestacije != null && !this.idVrstaManifestacije.equals(other.idVrstaManifestacije))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return naziv.toString();
    }
    
}
