package dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import exceptions.NonexistentEntityException;
import exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.VrstaManifestacije;
import entities.Karta;
import entities.Manifestacija;
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
public class ManifestacijaJpaController implements Serializable {

    public ManifestacijaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Manifestacija manifestacija) throws RollbackFailureException, Exception {
        if (manifestacija.getKartaCollection() == null) {
            manifestacija.setKartaCollection(new ArrayList<Karta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            VrstaManifestacije idVrstaManifestacije = manifestacija.getIdVrstaManifestacije();
            if (idVrstaManifestacije != null) {
                idVrstaManifestacije = em.getReference(idVrstaManifestacije.getClass(), idVrstaManifestacije.getIdVrstaManifestacije());
                manifestacija.setIdVrstaManifestacije(idVrstaManifestacije);
            }
            Collection<Karta> attachedKartaCollection = new ArrayList<Karta>();
            for (Karta kartaCollectionKartaToAttach : manifestacija.getKartaCollection()) {
                kartaCollectionKartaToAttach = em.getReference(kartaCollectionKartaToAttach.getClass(), kartaCollectionKartaToAttach.getIdKarta());
                attachedKartaCollection.add(kartaCollectionKartaToAttach);
            }
            manifestacija.setKartaCollection(attachedKartaCollection);
            em.persist(manifestacija);
            if (idVrstaManifestacije != null) {
                idVrstaManifestacije.getManifestacijaCollection().add(manifestacija);
                idVrstaManifestacije = em.merge(idVrstaManifestacije);
            }
            for (Karta kartaCollectionKarta : manifestacija.getKartaCollection()) {
                Manifestacija oldIdManifestacijaOfKartaCollectionKarta = kartaCollectionKarta.getIdManifestacija();
                kartaCollectionKarta.setIdManifestacija(manifestacija);
                kartaCollectionKarta = em.merge(kartaCollectionKarta);
                if (oldIdManifestacijaOfKartaCollectionKarta != null) {
                    oldIdManifestacijaOfKartaCollectionKarta.getKartaCollection().remove(kartaCollectionKarta);
                    oldIdManifestacijaOfKartaCollectionKarta = em.merge(oldIdManifestacijaOfKartaCollectionKarta);
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

    public void edit(Manifestacija manifestacija) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Manifestacija persistentManifestacija = em.find(Manifestacija.class, manifestacija.getIdManifestacija());
            VrstaManifestacije idVrstaManifestacijeOld = persistentManifestacija.getIdVrstaManifestacije();
            VrstaManifestacije idVrstaManifestacijeNew = manifestacija.getIdVrstaManifestacije();
            Collection<Karta> kartaCollectionOld = persistentManifestacija.getKartaCollection();
            Collection<Karta> kartaCollectionNew = manifestacija.getKartaCollection();
            if (idVrstaManifestacijeNew != null) {
                idVrstaManifestacijeNew = em.getReference(idVrstaManifestacijeNew.getClass(), idVrstaManifestacijeNew.getIdVrstaManifestacije());
                manifestacija.setIdVrstaManifestacije(idVrstaManifestacijeNew);
            }
            Collection<Karta> attachedKartaCollectionNew = new ArrayList<Karta>();
            for (Karta kartaCollectionNewKartaToAttach : kartaCollectionNew) {
                kartaCollectionNewKartaToAttach = em.getReference(kartaCollectionNewKartaToAttach.getClass(), kartaCollectionNewKartaToAttach.getIdKarta());
                attachedKartaCollectionNew.add(kartaCollectionNewKartaToAttach);
            }
            kartaCollectionNew = attachedKartaCollectionNew;
            manifestacija.setKartaCollection(kartaCollectionNew);
            manifestacija = em.merge(manifestacija);
            if (idVrstaManifestacijeOld != null && !idVrstaManifestacijeOld.equals(idVrstaManifestacijeNew)) {
                idVrstaManifestacijeOld.getManifestacijaCollection().remove(manifestacija);
                idVrstaManifestacijeOld = em.merge(idVrstaManifestacijeOld);
            }
            if (idVrstaManifestacijeNew != null && !idVrstaManifestacijeNew.equals(idVrstaManifestacijeOld)) {
                idVrstaManifestacijeNew.getManifestacijaCollection().add(manifestacija);
                idVrstaManifestacijeNew = em.merge(idVrstaManifestacijeNew);
            }
            for (Karta kartaCollectionOldKarta : kartaCollectionOld) {
                if (!kartaCollectionNew.contains(kartaCollectionOldKarta)) {
                    kartaCollectionOldKarta.setIdManifestacija(null);
                    kartaCollectionOldKarta = em.merge(kartaCollectionOldKarta);
                }
            }
            for (Karta kartaCollectionNewKarta : kartaCollectionNew) {
                if (!kartaCollectionOld.contains(kartaCollectionNewKarta)) {
                    Manifestacija oldIdManifestacijaOfKartaCollectionNewKarta = kartaCollectionNewKarta.getIdManifestacija();
                    kartaCollectionNewKarta.setIdManifestacija(manifestacija);
                    kartaCollectionNewKarta = em.merge(kartaCollectionNewKarta);
                    if (oldIdManifestacijaOfKartaCollectionNewKarta != null && !oldIdManifestacijaOfKartaCollectionNewKarta.equals(manifestacija)) {
                        oldIdManifestacijaOfKartaCollectionNewKarta.getKartaCollection().remove(kartaCollectionNewKarta);
                        oldIdManifestacijaOfKartaCollectionNewKarta = em.merge(oldIdManifestacijaOfKartaCollectionNewKarta);
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
                Integer id = manifestacija.getIdManifestacija();
                if (findManifestacija(id) == null) {
                    throw new NonexistentEntityException("The manifestacija with id " + id + " no longer exists.");
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
            Manifestacija manifestacija;
            try {
                manifestacija = em.getReference(Manifestacija.class, id);
                manifestacija.getIdManifestacija();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The manifestacija with id " + id + " no longer exists.", enfe);
            }
            VrstaManifestacije idVrstaManifestacije = manifestacija.getIdVrstaManifestacije();
            if (idVrstaManifestacije != null) {
                idVrstaManifestacije.getManifestacijaCollection().remove(manifestacija);
                idVrstaManifestacije = em.merge(idVrstaManifestacije);
            }
            Collection<Karta> kartaCollection = manifestacija.getKartaCollection();
            for (Karta kartaCollectionKarta : kartaCollection) {
                kartaCollectionKarta.setIdManifestacija(null);
                kartaCollectionKarta = em.merge(kartaCollectionKarta);
            }
            em.remove(manifestacija);
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

    public List<Manifestacija> findManifestacijaEntities() {
        return findManifestacijaEntities(true, -1, -1);
    }

    public List<Manifestacija> findManifestacijaEntities(int maxResults, int firstResult) {
        return findManifestacijaEntities(false, maxResults, firstResult);
    }

    private List<Manifestacija> findManifestacijaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Manifestacija.class));
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

    public Manifestacija findManifestacija(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Manifestacija.class, id);
        } finally {
            em.close();
        }
    }

    public int getManifestacijaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Manifestacija> rt = cq.from(Manifestacija.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
