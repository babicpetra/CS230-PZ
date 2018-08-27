/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.RollbackFailureException;
import entities.Karta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Korisnik;
import entities.Manifestacija;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Petra
 */
public class KartaJpaController implements Serializable {

    public KartaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Karta karta) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Korisnik idKorisnik = karta.getIdKorisnik();
            if (idKorisnik != null) {
                idKorisnik = em.getReference(idKorisnik.getClass(), idKorisnik.getIdKorisnik());
                karta.setIdKorisnik(idKorisnik);
            }
            Manifestacija idManifestacija = karta.getIdManifestacija();
            if (idManifestacija != null) {
                idManifestacija = em.getReference(idManifestacija.getClass(), idManifestacija.getIdManifestacija());
                karta.setIdManifestacija(idManifestacija);
            }
            em.persist(karta);
            if (idKorisnik != null) {
                idKorisnik.getKartaCollection().add(karta);
                idKorisnik = em.merge(idKorisnik);
            }
            if (idManifestacija != null) {
                idManifestacija.getKartaCollection().add(karta);
                idManifestacija = em.merge(idManifestacija);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Karta karta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Karta persistentKarta = em.find(Karta.class, karta.getIdKarta());
            Korisnik idKorisnikOld = persistentKarta.getIdKorisnik();
            Korisnik idKorisnikNew = karta.getIdKorisnik();
            Manifestacija idManifestacijaOld = persistentKarta.getIdManifestacija();
            Manifestacija idManifestacijaNew = karta.getIdManifestacija();
            if (idKorisnikNew != null) {
                idKorisnikNew = em.getReference(idKorisnikNew.getClass(), idKorisnikNew.getIdKorisnik());
                karta.setIdKorisnik(idKorisnikNew);
            }
            if (idManifestacijaNew != null) {
                idManifestacijaNew = em.getReference(idManifestacijaNew.getClass(), idManifestacijaNew.getIdManifestacija());
                karta.setIdManifestacija(idManifestacijaNew);
            }
            karta = em.merge(karta);
            if (idKorisnikOld != null && !idKorisnikOld.equals(idKorisnikNew)) {
                idKorisnikOld.getKartaCollection().remove(karta);
                idKorisnikOld = em.merge(idKorisnikOld);
            }
            if (idKorisnikNew != null && !idKorisnikNew.equals(idKorisnikOld)) {
                idKorisnikNew.getKartaCollection().add(karta);
                idKorisnikNew = em.merge(idKorisnikNew);
            }
            if (idManifestacijaOld != null && !idManifestacijaOld.equals(idManifestacijaNew)) {
                idManifestacijaOld.getKartaCollection().remove(karta);
                idManifestacijaOld = em.merge(idManifestacijaOld);
            }
            if (idManifestacijaNew != null && !idManifestacijaNew.equals(idManifestacijaOld)) {
                idManifestacijaNew.getKartaCollection().add(karta);
                idManifestacijaNew = em.merge(idManifestacijaNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = karta.getIdKarta();
                if (findKarta(id) == null) {
                    throw new NonexistentEntityException("The karta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Karta karta;
            try {
                karta = em.getReference(Karta.class, id);
                karta.getIdKarta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The karta with id " + id + " no longer exists.", enfe);
            }
            Korisnik idKorisnik = karta.getIdKorisnik();
            if (idKorisnik != null) {
                idKorisnik.getKartaCollection().remove(karta);
                idKorisnik = em.merge(idKorisnik);
            }
            Manifestacija idManifestacija = karta.getIdManifestacija();
            if (idManifestacija != null) {
                idManifestacija.getKartaCollection().remove(karta);
                idManifestacija = em.merge(idManifestacija);
            }
            em.remove(karta);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Karta> findKartaEntities() {
        return findKartaEntities(true, -1, -1);
    }

    public List<Karta> findKartaEntities(int maxResults, int firstResult) {
        return findKartaEntities(false, maxResults, firstResult);
    }

    private List<Karta> findKartaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Karta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Karta findKarta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Karta.class, id);
        } finally {
            em.close();
        }
    }

    public int getKartaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Karta> rt = cq.from(Karta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
