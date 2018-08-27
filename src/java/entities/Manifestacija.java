/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Petra
 */
@Entity
@Table(name = "manifestacija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Manifestacija.findAll", query = "SELECT m FROM Manifestacija m")
    , @NamedQuery(name = "Manifestacija.findByIdManifestacija", query = "SELECT m FROM Manifestacija m WHERE m.idManifestacija = :idManifestacija")
    , @NamedQuery(name = "Manifestacija.findByNaslov", query = "SELECT m FROM Manifestacija m WHERE m.naslov = :naslov")
    , @NamedQuery(name = "Manifestacija.findByCena", query = "SELECT m FROM Manifestacija m WHERE m.cena = :cena")
    , @NamedQuery(name = "Manifestacija.findByMesto", query = "SELECT m FROM Manifestacija m WHERE m.mesto = :mesto")
    , @NamedQuery(name = "Manifestacija.findByDatum", query = "SELECT m FROM Manifestacija m WHERE m.datum = :datum")
    , @NamedQuery(name = "Manifestacija.findByVreme", query = "SELECT m FROM Manifestacija m WHERE m.vreme = :vreme")})
public class Manifestacija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MANIFESTACIJA")
    private Integer idManifestacija;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NASLOV")
    private String naslov;
    @Lob
    @Size(max = 16777215)
    @Column(name = "OPIS")
    private String opis;
    @Column(name = "CENA")
    private Long cena;
    @Size(max = 100)
    @Column(name = "MESTO")
    private String mesto;
    @Column(name = "DATUM")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Column(name = "VREME")
    @Temporal(TemporalType.TIME)
    private Date vreme;
    @JoinColumn(name = "ID_VRSTA_MANIFESTACIJE", referencedColumnName = "ID_VRSTA_MANIFESTACIJE")
    @ManyToOne
    private VrstaManifestacije idVrstaManifestacije;
    @OneToMany(mappedBy = "idManifestacija")
    private Collection<Karta> kartaCollection;

    public Manifestacija() {
    }

    public Manifestacija(Integer idManifestacija) {
        this.idManifestacija = idManifestacija;
    }

    public Manifestacija(Integer idManifestacija, String naslov) {
        this.idManifestacija = idManifestacija;
        this.naslov = naslov;
    }

    public Integer getIdManifestacija() {
        return idManifestacija;
    }

    public void setIdManifestacija(Integer idManifestacija) {
        this.idManifestacija = idManifestacija;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Long getCena() {
        return cena;
    }

    public void setCena(Long cena) {
        this.cena = cena;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public VrstaManifestacije getIdVrstaManifestacije() {
        return idVrstaManifestacije;
    }

    public void setIdVrstaManifestacije(VrstaManifestacije idVrstaManifestacije) {
        this.idVrstaManifestacije = idVrstaManifestacije;
    }

    @XmlTransient
    public Collection<Karta> getKartaCollection() {
        return kartaCollection;
    }

    public void setKartaCollection(Collection<Karta> kartaCollection) {
        this.kartaCollection = kartaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idManifestacija != null ? idManifestacija.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Manifestacija)) {
            return false;
        }
        Manifestacija other = (Manifestacija) object;
        if ((this.idManifestacija == null && other.idManifestacija != null) || (this.idManifestacija != null && !this.idManifestacija.equals(other.idManifestacija))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Manifestacija[ idManifestacija=" + idManifestacija + " ]";
    }
    
}
