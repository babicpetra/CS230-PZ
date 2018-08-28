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
import entities.DodatneInfo;
import entities.Karta;
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
public class KorisnikJpaController implements Serializable {

    public KorisnikJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Korisnik korisnik) throws RollbackFailureException, Exception {
        if (korisnik.getKartaCollection() == null) {
            korisnik.setKartaCollection(new ArrayList<Karta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DodatneInfo idDodatneInfo = korisnik.getIdDodatneInfo();
            if (idDodatneInfo != null) {
                idDodatneInfo = em.getReference(idDodatneInfo.getClass(), idDodatneInfo.getIdDodatneInfo());
                korisnik.setIdDodatneInfo(idDodatneInfo);
            }
            Collection<Karta> attachedKartaCollection = new ArrayList<Karta>();
            for (Karta kartaCollectionKartaToAttach : korisnik.getKartaCollection()) {
                kartaCollectionKartaToAttach = em.getReference(kartaCollectionKartaToAttach.getClass(), kartaCollectionKartaToAttach.getIdKarta());
                attachedKartaCollection.add(kartaCollectionKartaToAttach);
            }
            korisnik.setKartaCollection(attachedKartaCollection);
            em.persist(korisnik);
            if (idDodatneInfo != null) {
                idDodatneInfo.getKorisnikCollection().add(korisnik);
                idDodatneInfo = em.merge(idDodatneInfo);
            }
            for (Karta kartaCollectionKarta : korisnik.getKartaCollection()) {
                Korisnik oldIdKorisnikOfKartaCollectionKarta = kartaCollectionKarta.getIdKorisnik();
                kartaCollectionKarta.setIdKorisnik(korisnik);
                kartaCollectionKarta = em.merge(kartaCollectionKarta);
                if (oldIdKorisnikOfKartaCollectionKarta != null) {
                    oldIdKorisnikOfKartaCollectionKarta.getKartaCollection().remove(kartaCollectionKarta);
                    oldIdKorisnikOfKartaCollectionKarta = em.merge(oldIdKorisnikOfKartaCollectionKarta);
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

    public void edit(Korisnik korisnik) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Korisnik persistentKorisnik = em.find(Korisnik.class, korisnik.getIdKorisnik());
            DodatneInfo idDodatneInfoOld = persistentKorisnik.getIdDodatneInfo();
            DodatneInfo idDodatneInfoNew = korisnik.getIdDodatneInfo();
            Collection<Karta> kartaCollectionOld = persistentKorisnik.getKartaCollection();
            Collection<Karta> kartaCollectionNew = korisnik.getKartaCollection();
            if (idDodatneInfoNew != null) {
                idDodatneInfoNew = em.getReference(idDodatneInfoNew.getClass(), idDodatneInfoNew.getIdDodatneInfo());
                korisnik.setIdDodatneInfo(idDodatneInfoNew);
            }
            Collection<Karta> attachedKartaCollectionNew = new ArrayList<Karta>();
            for (Karta kartaCollectionNewKartaToAttach : kartaCollectionNew) {
                kartaCollectionNewKartaToAttach = em.getReference(kartaCollectionNewKartaToAttach.getClass(), kartaCollectionNewKartaToAttach.getIdKarta());
                attachedKartaCollectionNew.add(kartaCollectionNewKartaToAttach);
            }
            kartaCollectionNew = attachedKartaCollectionNew;
            korisnik.setKartaCollection(kartaCollectionNew);
            korisnik = em.merge(korisnik);
            if (idDodatneInfoOld != null && !idDodatneInfoOld.equals(idDodatneInfoNew)) {
                idDodatneInfoOld.getKorisnikCollection().remove(korisnik);
                idDodatneInfoOld = em.merge(idDodatneInfoOld);
            }
            if (idDodatneInfoNew != null && !idDodatneInfoNew.equals(idDodatneInfoOld)) {
                idDodatneInfoNew.getKorisnikCollection().add(korisnik);
                idDodatneInfoNew = em.merge(idDodatneInfoNew);
            }
            for (Karta kartaCollectionOldKarta : kartaCollectionOld) {
                if (!kartaCollectionNew.contains(kartaCollectionOldKarta)) {
                    kartaCollectionOldKarta.setIdKorisnik(null);
                    kartaCollectionOldKarta = em.merge(kartaCollectionOldKarta);
                }
            }
            for (Karta kartaCollectionNewKarta : kartaCollectionNew) {
                if (!kartaCollectionOld.contains(kartaCollectionNewKarta)) {
                    Korisnik oldIdKorisnikOfKartaCollectionNewKarta = kartaCollectionNewKarta.getIdKorisnik();
                    kartaCollectionNewKarta.setIdKorisnik(korisnik);
                    kartaCollectionNewKarta = em.merge(kartaCollectionNewKarta);
                    if (oldIdKorisnikOfKartaCollectionNewKarta != null && !oldIdKorisnikOfKartaCollectionNewKarta.equals(korisnik)) {
                        oldIdKorisnikOfKartaCollectionNewKarta.getKartaCollection().remove(kartaCollectionNewKarta);
                        oldIdKorisnikOfKartaCollectionNewKarta = em.merge(oldIdKorisnikOfKartaCollectionNewKarta);
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
                Integer id = korisnik.getIdKorisnik();
                if (findKorisnik(id) == null) {
                    throw new NonexistentEntityException("The korisnik with id " + id + " no longer exists.");
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
            Korisnik korisnik;
            try {
                korisnik = em.getReference(Korisnik.class, id);
                korisnik.getIdKorisnik();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The korisnik with id " + id + " no longer exists.", enfe);
            }
            DodatneInfo idDodatneInfo = korisnik.getIdDodatneInfo();
            if (idDodatneInfo != null) {
                idDodatneInfo.getKorisnikCollection().remove(korisnik);
                idDodatneInfo = em.merge(idDodatneInfo);
            }
            Collection<Karta> kartaCollection = korisnik.getKartaCollection();
            for (Karta kartaCollectionKarta : kartaCollection) {
                kartaCollectionKarta.setIdKorisnik(null);
                kartaCollectionKarta = em.merge(kartaCollectionKarta);
            }
            em.remove(korisnik);
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

    public List<Korisnik> findKorisnikEntities() {
        return findKorisnikEntities(true, -1, -1);
    }

    public List<Korisnik> findKorisnikEntities(int maxResults, int firstResult) {
        return findKorisnikEntities(false, maxResults, firstResult);
    }

    private List<Korisnik> findKorisnikEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Korisnik.class));
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

    public Korisnik findKorisnik(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Korisnik.class, id);
        } finally {
            em.close();
        }
    }

    public int getKorisnikCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Korisnik> rt = cq.from(Korisnik.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
