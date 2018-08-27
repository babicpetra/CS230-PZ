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
@Table(name = "dodatne_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DodatneInfo.findAll", query = "SELECT d FROM DodatneInfo d")
    , @NamedQuery(name = "DodatneInfo.findByIdDodatneInfo", query = "SELECT d FROM DodatneInfo d WHERE d.idDodatneInfo = :idDodatneInfo")
    , @NamedQuery(name = "DodatneInfo.findByUlica", query = "SELECT d FROM DodatneInfo d WHERE d.ulica = :ulica")
    , @NamedQuery(name = "DodatneInfo.findByBroj", query = "SELECT d FROM DodatneInfo d WHERE d.broj = :broj")
    , @NamedQuery(name = "DodatneInfo.findByGrad", query = "SELECT d FROM DodatneInfo d WHERE d.grad = :grad")
    , @NamedQuery(name = "DodatneInfo.findByPostanskiBroj", query = "SELECT d FROM DodatneInfo d WHERE d.postanskiBroj = :postanskiBroj")
    , @NamedQuery(name = "DodatneInfo.findByEmail", query = "SELECT d FROM DodatneInfo d WHERE d.email = :email")
    , @NamedQuery(name = "DodatneInfo.findByBrojTelefon", query = "SELECT d FROM DodatneInfo d WHERE d.brojTelefon = :brojTelefon")})
public class DodatneInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_DODATNE_INFO")
    private Integer idDodatneInfo;
    @Size(max = 100)
    @Column(name = "ULICA")
    private String ulica;
    @Size(max = 10)
    @Column(name = "BROJ")
    private String broj;
    @Size(max = 50)
    @Column(name = "GRAD")
    private String grad;
    @Column(name = "POSTANSKI_BROJ")
    private Integer postanskiBroj;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 10)
    @Column(name = "BROJ_TELEFON")
    private String brojTelefon;
    @OneToMany(mappedBy = "idDodatneInfo")
    private Collection<Korisnik> korisnikCollection;

    public DodatneInfo() {
    }

    public DodatneInfo(Integer idDodatneInfo) {
        this.idDodatneInfo = idDodatneInfo;
    }

    public Integer getIdDodatneInfo() {
        return idDodatneInfo;
    }

    public void setIdDodatneInfo(Integer idDodatneInfo) {
        this.idDodatneInfo = idDodatneInfo;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public Integer getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(Integer postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrojTelefon() {
        return brojTelefon;
    }

    public void setBrojTelefon(String brojTelefon) {
        this.brojTelefon = brojTelefon;
    }

    @XmlTransient
    public Collection<Korisnik> getKorisnikCollection() {
        return korisnikCollection;
    }

    public void setKorisnikCollection(Collection<Korisnik> korisnikCollection) {
        this.korisnikCollection = korisnikCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDodatneInfo != null ? idDodatneInfo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DodatneInfo)) {
            return false;
        }
        DodatneInfo other = (DodatneInfo) object;
        if ((this.idDodatneInfo == null && other.idDodatneInfo != null) || (this.idDodatneInfo != null && !this.idDodatneInfo.equals(other.idDodatneInfo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DodatneInfo[ idDodatneInfo=" + idDodatneInfo + " ]";
    }
    
}
