/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Manifestacija;
import entities.VrstaManifestacije;
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
public class VrstaManifestacijeJpaController implements Serializable {

    public VrstaManifestacijeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VrstaManifestacije vrstaManifestacije) throws RollbackFailureException, Exception {
        if (vrstaManifestacije.getManifestacijaCollection() == null) {
            vrstaManifestacije.setManifestacijaCollection(new ArrayList<Manifestacija>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Manifestacija> attachedManifestacijaCollection = new ArrayList<Manifestacija>();
            for (Manifestacija manifestacijaCollectionManifestacijaToAttach : vrstaManifestacije.getManifestacijaCollection()) {
                manifestacijaCollectionManifestacijaToAttach = em.getReference(manifestacijaCollectionManifestacijaToAttach.getClass(), manifestacijaCollectionManifestacijaToAttach.getIdManifestacija());
                attachedManifestacijaCollection.add(manifestacijaCollectionManifestacijaToAttach);
            }
            vrstaManifestacije.setManifestacijaCollection(attachedManifestacijaCollection);
            em.persist(vrstaManifestacije);
            for (Manifestacija manifestacijaCollectionManifestacija : vrstaManifestacije.getManifestacijaCollection()) {
                VrstaManifestacije oldIdVrstaManifestacijeOfManifestacijaCollectionManifestacija = manifestacijaCollectionManifestacija.getIdVrstaManifestacije();
                manifestacijaCollectionManifestacija.setIdVrstaManifestacije(vrstaManifestacije);
                manifestacijaCollectionManifestacija = em.merge(manifestacijaCollectionManifestacija);
                if (oldIdVrstaManifestacijeOfManifestacijaCollectionManifestacija != null) {
                    oldIdVrstaManifestacijeOfManifestacijaCollectionManifestacija.getManifestacijaCollection().remove(manifestacijaCollectionManifestacija);
                    oldIdVrstaManifestacijeOfManifestacijaCollectionManifestacija = em.merge(oldIdVrstaManifestacijeOfManifestacijaCollectionManifestacija);
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

    public void edit(VrstaManifestacije vrstaManifestacije) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VrstaManifestacije persistentVrstaManifestacije = em.find(VrstaManifestacije.class, vrstaManifestacije.getIdVrstaManifestacije());
            Collection<Manifestacija> manifestacijaCollectionOld = persistentVrstaManifestacije.getManifestacijaCollection();
            Collection<Manifestacija> manifestacijaCollectionNew = vrstaManifestacije.getManifestacijaCollection();
            Collection<Manifestacija> attachedManifestacijaCollectionNew = new ArrayList<Manifestacija>();
            for (Manifestacija manifestacijaCollectionNewManifestacijaToAttach : manifestacijaCollectionNew) {
                manifestacijaCollectionNewManifestacijaToAttach = em.getReference(manifestacijaCollectionNewManifestacijaToAttach.getClass(), manifestacijaCollectionNewManifestacijaToAttach.getIdManifestacija());
                attachedManifestacijaCollectionNew.add(manifestacijaCollectionNewManifestacijaToAttach);
            }
            manifestacijaCollectionNew = attachedManifestacijaCollectionNew;
            vrstaManifestacije.setManifestacijaCollection(manifestacijaCollectionNew);
            vrstaManifestacije = em.merge(vrstaManifestacije);
            for (Manifestacija manifestacijaCollectionOldManifestacija : manifestacijaCollectionOld) {
                if (!manifestacijaCollectionNew.contains(manifestacijaCollectionOldManifestacija)) {
                    manifestacijaCollectionOldManifestacija.setIdVrstaManifestacije(null);
                    manifestacijaCollectionOldManifestacija = em.merge(manifestacijaCollectionOldManifestacija);
                }
            }
            for (Manifestacija manifestacijaCollectionNewManifestacija : manifestacijaCollectionNew) {
                if (!manifestacijaCollectionOld.contains(manifestacijaCollectionNewManifestacija)) {
                    VrstaManifestacije oldIdVrstaManifestacijeOfManifestacijaCollectionNewManifestacija = manifestacijaCollectionNewManifestacija.getIdVrstaManifestacije();
                    manifestacijaCollectionNewManifestacija.setIdVrstaManifestacije(vrstaManifestacije);
                    manifestacijaCollectionNewManifestacija = em.merge(manifestacijaCollectionNewManifestacija);
                    if (oldIdVrstaManifestacijeOfManifestacijaCollectionNewManifestacija != null && !oldIdVrstaManifestacijeOfManifestacijaCollectionNewManifestacija.equals(vrstaManifestacije)) {
                        oldIdVrstaManifestacijeOfManifestacijaCollectionNewManifestacija.getManifestacijaCollection().remove(manifestacijaCollectionNewManifestacija);
                        oldIdVrstaManifestacijeOfManifestacijaCollectionNewManifestacija = em.merge(oldIdVrstaManifestacijeOfManifestacijaCollectionNewManifestacija);
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
                Integer id = vrstaManifestacije.getIdVrstaManifestacije();
                if (findVrstaManifestacije(id) == null) {
                    throw new NonexistentEntityException("The vrstaManifestacije with id " + id + " no longer exists.");
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
            VrstaManifestacije vrstaManifestacije;
            try {
                vrstaManifestacije = em.getReference(VrstaManifestacije.class, id);
                vrstaManifestacije.getIdVrstaManifestacije();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vrstaManifestacije with id " + id + " no longer exists.", enfe);
            }
            Collection<Manifestacija> manifestacijaCollection = vrstaManifestacije.getManifestacijaCollection();
            for (Manifestacija manifestacijaCollectionManifestacija : manifestacijaCollection) {
                manifestacijaCollectionManifestacija.setIdVrstaManifestacije(null);
                manifestacijaCollectionManifestacija = em.merge(manifestacijaCollectionManifestacija);
            }
            em.remove(vrstaManifestacije);
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

    public List<VrstaManifestacije> findVrstaManifestacijeEntities() {
        return findVrstaManifestacijeEntities(true, -1, -1);
    }

    public List<VrstaManifestacije> findVrstaManifestacijeEntities(int maxResults, int firstResult) {
        return findVrstaManifestacijeEntities(false, maxResults, firstResult);
    }

    private List<VrstaManifestacije> findVrstaManifestacijeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VrstaManifestacije.class));
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

    public VrstaManifestacije findVrstaManifestacije(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VrstaManifestacije.class, id);
        } finally {
            em.close();
        }
    }

    public int getVrstaManifestacijeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VrstaManifestacije> rt = cq.from(VrstaManifestacije.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
