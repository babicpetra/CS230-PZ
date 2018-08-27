/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petra
 */
@Entity
@Table(name = "karta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Karta.findAll", query = "SELECT k FROM Karta k")
    , @NamedQuery(name = "Karta.findByIdKarta", query = "SELECT k FROM Karta k WHERE k.idKarta = :idKarta")
    , @NamedQuery(name = "Karta.findByKolicina", query = "SELECT k FROM Karta k WHERE k.kolicina = :kolicina")})
public class Karta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_KARTA")
    private Integer idKarta;
    @Column(name = "KOLICINA")
    private Integer kolicina;
    @JoinColumn(name = "ID_KORISNIK", referencedColumnName = "ID_KORISNIK")
    @ManyToOne
    private Korisnik idKorisnik;
    @JoinColumn(name = "ID_MANIFESTACIJA", referencedColumnName = "ID_MANIFESTACIJA")
    @ManyToOne
    private Manifestacija idManifestacija;

    public Karta() {
    }

    public Karta(Integer idKarta) {
        this.idKarta = idKarta;
    }

    public Integer getIdKarta() {
        return idKarta;
    }

    public void setIdKarta(Integer idKarta) {
        this.idKarta = idKarta;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public Korisnik getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(Korisnik idKorisnik) {
        this.idKorisnik = idKorisnik;
    }

    public Manifestacija getIdManifestacija() {
        return idManifestacija;
    }

    public void setIdManifestacija(Manifestacija idManifestacija) {
        this.idManifestacija = idManifestacija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKarta != null ? idKarta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Karta)) {
            return false;
        }
        Karta other = (Karta) object;
        if ((this.idKarta == null && other.idKarta != null) || (this.idKarta != null && !this.idKarta.equals(other.idKarta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Karta[ idKarta=" + idKarta + " ]";
    }
    
}
