/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.RollbackFailureException;
import entities.DodatneInfo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Korisnik;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Petra
 */
public class DodatneInfoJpaController implements Serializable {

    public DodatneInfoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DodatneInfo dodatneInfo) throws RollbackFailureException, Exception {
        if (dodatneInfo.getKorisnikCollection() == null) {
            dodatneInfo.setKorisnikCollection(new ArrayList<Korisnik>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Korisnik> attachedKorisnikCollection = new ArrayList<Korisnik>();
            for (Korisnik korisnikCollectionKorisnikToAttach : dodatneInfo.getKorisnikCollection()) {
                korisnikCollectionKorisnikToAttach = em.getReference(korisnikCollectionKorisnikToAttach.getClass(), korisnikCollectionKorisnikToAttach.getIdKorisnik());
                attachedKorisnikCollection.add(korisnikCollectionKorisnikToAttach);
            }
            dodatneInfo.setKorisnikCollection(attachedKorisnikCollection);
            em.persist(dodatneInfo);
            for (Korisnik korisnikCollectionKorisnik : dodatneInfo.getKorisnikCollection()) {
                DodatneInfo oldIdDodatneInfoOfKorisnikCollectionKorisnik = korisnikCollectionKorisnik.getIdDodatneInfo();
                korisnikCollectionKorisnik.setIdDodatneInfo(dodatneInfo);
                korisnikCollectionKorisnik = em.merge(korisnikCollectionKorisnik);
                if (oldIdDodatneInfoOfKorisnikCollectionKorisnik != null) {
                    oldIdDodatneInfoOfKorisnikCollectionKorisnik.getKorisnikCollection().remove(korisnikCollectionKorisnik);
                    oldIdDodatneInfoOfKorisnikCollectionKorisnik = em.merge(oldIdDodatneInfoOfKorisnikCollectionKorisnik);
                }
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

    public void edit(DodatneInfo dodatneInfo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DodatneInfo persistentDodatneInfo = em.find(DodatneInfo.class, dodatneInfo.getIdDodatneInfo());
            Collection<Korisnik> korisnikCollectionOld = persistentDodatneInfo.getKorisnikCollection();
            Collection<Korisnik> korisnikCollectionNew = dodatneInfo.getKorisnikCollection();
            Collection<Korisnik> attachedKorisnikCollectionNew = new ArrayList<Korisnik>();
            for (Korisnik korisnikCollectionNewKorisnikToAttach : korisnikCollectionNew) {
                korisnikCollectionNewKorisnikToAttach = em.getReference(korisnikCollectionNewKorisnikToAttach.getClass(), korisnikCollectionNewKorisnikToAttach.getIdKorisnik());
                attachedKorisnikCollectionNew.add(korisnikCollectionNewKorisnikToAttach);
            }
            korisnikCollectionNew = attachedKorisnikCollectionNew;
            dodatneInfo.setKorisnikCollection(korisnikCollectionNew);
            dodatneInfo = em.merge(dodatneInfo);
            for (Korisnik korisnikCollectionOldKorisnik : korisnikCollectionOld) {
                if (!korisnikCollectionNew.contains(korisnikCollectionOldKorisnik)) {
                    korisnikCollectionOldKorisnik.setIdDodatneInfo(null);
                    korisnikCollectionOldKorisnik = em.merge(korisnikCollectionOldKorisnik);
                }
            }
            for (Korisnik korisnikCollectionNewKorisnik : korisnikCollectionNew) {
                if (!korisnikCollectionOld.contains(korisnikCollectionNewKorisnik)) {
                    DodatneInfo oldIdDodatneInfoOfKorisnikCollectionNewKorisnik = korisnikCollectionNewKorisnik.getIdDodatneInfo();
                    korisnikCollectionNewKorisnik.setIdDodatneInfo(dodatneInfo);
                    korisnikCollectionNewKorisnik = em.merge(korisnikCollectionNewKorisnik);
                    if (oldIdDodatneInfoOfKorisnikCollectionNewKorisnik != null && !oldIdDodatneInfoOfKorisnikCollectionNewKorisnik.equals(dodatneInfo)) {
                        oldIdDodatneInfoOfKorisnikCollectionNewKorisnik.getKorisnikCollection().remove(korisnikCollectionNewKorisnik);
                        oldIdDodatneInfoOfKorisnikCollectionNewKorisnik = em.merge(oldIdDodatneInfoOfKorisnikCollectionNewKorisnik);
                    }
                }
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
                Integer id = dodatneInfo.getIdDodatneInfo();
                if (findDodatneInfo(id) == null) {
                    throw new NonexistentEntityException("The dodatneInfo with id " + id + " no longer exists.");
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
            DodatneInfo dodatneInfo;
            try {
                dodatneInfo = em.getReference(DodatneInfo.class, id);
                dodatneInfo.getIdDodatneInfo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dodatneInfo with id " + id + " no longer exists.", enfe);
            }
            Collection<Korisnik> korisnikCollection = dodatneInfo.getKorisnikCollection();
            for (Korisnik korisnikCollectionKorisnik : korisnikCollection) {
                korisnikCollectionKorisnik.setIdDodatneInfo(null);
                korisnikCollectionKorisnik = em.merge(korisnikCollectionKorisnik);
            }
            em.remove(dodatneInfo);
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

    public List<DodatneInfo> findDodatneInfoEntities() {
        return findDodatneInfoEntities(true, -1, -1);
    }

    public List<DodatneInfo> findDodatneInfoEntities(int maxResults, int firstResult) {
        return findDodatneInfoEntities(false, maxResults, firstResult);
    }

    private List<DodatneInfo> findDodatneInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DodatneInfo.class));
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

    public DodatneInfo findDodatneInfo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DodatneInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getDodatneInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DodatneInfo> rt = cq.from(DodatneInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
