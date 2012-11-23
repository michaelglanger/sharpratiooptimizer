/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sharpratiooptimizer.data.exceptions.IllegalOrphanException;
import sharpratiooptimizer.data.exceptions.NonexistentEntityException;
import sharpratiooptimizer.data.exceptions.PreexistingEntityException;

/**
 *
 * @author axjyb
 */
public class MarketJpaController implements Serializable {

    public MarketJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Market market) throws PreexistingEntityException, Exception {
        if (market.getSymbolCollection() == null) {
            market.setSymbolCollection(new ArrayList<Symbol>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Symbol> attachedSymbolCollection = new ArrayList<Symbol>();
            for (Symbol symbolCollectionSymbolToAttach : market.getSymbolCollection()) {
                symbolCollectionSymbolToAttach = em.getReference(symbolCollectionSymbolToAttach.getClass(), symbolCollectionSymbolToAttach.getId());
                attachedSymbolCollection.add(symbolCollectionSymbolToAttach);
            }
            market.setSymbolCollection(attachedSymbolCollection);
            em.persist(market);
            for (Symbol symbolCollectionSymbol : market.getSymbolCollection()) {
                Market oldMarketOfSymbolCollectionSymbol = symbolCollectionSymbol.getMarket();
                symbolCollectionSymbol.setMarket(market);
                symbolCollectionSymbol = em.merge(symbolCollectionSymbol);
                if (oldMarketOfSymbolCollectionSymbol != null) {
                    oldMarketOfSymbolCollectionSymbol.getSymbolCollection().remove(symbolCollectionSymbol);
                    oldMarketOfSymbolCollectionSymbol = em.merge(oldMarketOfSymbolCollectionSymbol);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMarket(market.getId()) != null) {
                throw new PreexistingEntityException("Market " + market + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Market market) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Market persistentMarket = em.find(Market.class, market.getId());
            Collection<Symbol> symbolCollectionOld = persistentMarket.getSymbolCollection();
            Collection<Symbol> symbolCollectionNew = market.getSymbolCollection();
            List<String> illegalOrphanMessages = null;
            for (Symbol symbolCollectionOldSymbol : symbolCollectionOld) {
                if (!symbolCollectionNew.contains(symbolCollectionOldSymbol)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Symbol " + symbolCollectionOldSymbol + " since its market field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Symbol> attachedSymbolCollectionNew = new ArrayList<Symbol>();
            for (Symbol symbolCollectionNewSymbolToAttach : symbolCollectionNew) {
                symbolCollectionNewSymbolToAttach = em.getReference(symbolCollectionNewSymbolToAttach.getClass(), symbolCollectionNewSymbolToAttach.getId());
                attachedSymbolCollectionNew.add(symbolCollectionNewSymbolToAttach);
            }
            symbolCollectionNew = attachedSymbolCollectionNew;
            market.setSymbolCollection(symbolCollectionNew);
            market = em.merge(market);
            for (Symbol symbolCollectionNewSymbol : symbolCollectionNew) {
                if (!symbolCollectionOld.contains(symbolCollectionNewSymbol)) {
                    Market oldMarketOfSymbolCollectionNewSymbol = symbolCollectionNewSymbol.getMarket();
                    symbolCollectionNewSymbol.setMarket(market);
                    symbolCollectionNewSymbol = em.merge(symbolCollectionNewSymbol);
                    if (oldMarketOfSymbolCollectionNewSymbol != null && !oldMarketOfSymbolCollectionNewSymbol.equals(market)) {
                        oldMarketOfSymbolCollectionNewSymbol.getSymbolCollection().remove(symbolCollectionNewSymbol);
                        oldMarketOfSymbolCollectionNewSymbol = em.merge(oldMarketOfSymbolCollectionNewSymbol);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = market.getId();
                if (findMarket(id) == null) {
                    throw new NonexistentEntityException("The market with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Market market;
            try {
                market = em.getReference(Market.class, id);
                market.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The market with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Symbol> symbolCollectionOrphanCheck = market.getSymbolCollection();
            for (Symbol symbolCollectionOrphanCheckSymbol : symbolCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Market (" + market + ") cannot be destroyed since the Symbol " + symbolCollectionOrphanCheckSymbol + " in its symbolCollection field has a non-nullable market field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(market);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

//    public List<Market> findMarketEntities() {
//        return findMarketEntities(true, -1, -1);
//    }
//
//    public List<Market> findMarketEntities(int maxResults, int firstResult) {
//        return findMarketEntities(false, maxResults, firstResult);
//    }

//    private List<Market> findMarketEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Market.class));
//            Query q = em.createQuery(cq);
//            if (!all) {
//                q.setMaxResults(maxResults);
//                q.setFirstResult(firstResult);
//            }
//            return q.getResultList();
//        } finally {
//            em.close();
//        }
//    }

    public Market findMarket(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Market.class, id);
        } finally {
            em.close();
        }
    }

//    public int getMarketCount() {
//        EntityManager em = getEntityManager();
//        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            Root<Market> rt = cq.from(Market.class);
//            cq.select(em.getCriteriaBuilder().count(rt));
//            Query q = em.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
//        } finally {
//            em.close();
//        }
//    }
    
}
