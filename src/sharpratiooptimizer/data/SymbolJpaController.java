/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.data;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sharpratiooptimizer.data.exceptions.IllegalOrphanException;
import sharpratiooptimizer.data.exceptions.NonexistentEntityException;
import sharpratiooptimizer.data.exceptions.PreexistingEntityException;

/**
 *
 * @author axjyb
 */
public class SymbolJpaController implements Serializable {

    public SymbolJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Symbol symbol) throws PreexistingEntityException, Exception {
        if (symbol.getStockCollection() == null) {
            symbol.setStockCollection(new ArrayList<Stock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Market market = symbol.getMarket();
            if (market != null) {
                market = em.getReference(market.getClass(), market.getId());
                symbol.setMarket(market);
            }
            Collection<Stock> attachedStockCollection = new ArrayList<Stock>();
            for (Stock stockCollectionStockToAttach : symbol.getStockCollection()) {
                stockCollectionStockToAttach = em.getReference(stockCollectionStockToAttach.getClass(), stockCollectionStockToAttach.getId());
                attachedStockCollection.add(stockCollectionStockToAttach);
            }
            symbol.setStockCollection(attachedStockCollection);
            em.persist(symbol);
            if (market != null) {
                market.getSymbolCollection().add(symbol);
                market = em.merge(market);
            }
            for (Stock stockCollectionStock : symbol.getStockCollection()) {
                Symbol oldSymbolOfStockCollectionStock = stockCollectionStock.getSymbol();
                stockCollectionStock.setSymbol(symbol);
                stockCollectionStock = em.merge(stockCollectionStock);
                if (oldSymbolOfStockCollectionStock != null) {
                    oldSymbolOfStockCollectionStock.getStockCollection().remove(stockCollectionStock);
                    oldSymbolOfStockCollectionStock = em.merge(oldSymbolOfStockCollectionStock);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSymbol(symbol.getId()) != null) {
                throw new PreexistingEntityException("Symbol " + symbol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Symbol symbol) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Symbol persistentSymbol = em.find(Symbol.class, symbol.getId());
            Market marketOld = persistentSymbol.getMarket();
            Market marketNew = symbol.getMarket();
            Collection<Stock> stockCollectionOld = persistentSymbol.getStockCollection();
            Collection<Stock> stockCollectionNew = symbol.getStockCollection();
            List<String> illegalOrphanMessages = null;
            for (Stock stockCollectionOldStock : stockCollectionOld) {
                if (!stockCollectionNew.contains(stockCollectionOldStock)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Stock " + stockCollectionOldStock + " since its symbol field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (marketNew != null) {
                marketNew = em.getReference(marketNew.getClass(), marketNew.getId());
                symbol.setMarket(marketNew);
            }
            Collection<Stock> attachedStockCollectionNew = new ArrayList<Stock>();
            for (Stock stockCollectionNewStockToAttach : stockCollectionNew) {
                stockCollectionNewStockToAttach = em.getReference(stockCollectionNewStockToAttach.getClass(), stockCollectionNewStockToAttach.getId());
                attachedStockCollectionNew.add(stockCollectionNewStockToAttach);
            }
            stockCollectionNew = attachedStockCollectionNew;
            symbol.setStockCollection(stockCollectionNew);
            symbol = em.merge(symbol);
            if (marketOld != null && !marketOld.equals(marketNew)) {
                marketOld.getSymbolCollection().remove(symbol);
                marketOld = em.merge(marketOld);
            }
            if (marketNew != null && !marketNew.equals(marketOld)) {
                marketNew.getSymbolCollection().add(symbol);
                marketNew = em.merge(marketNew);
            }
            for (Stock stockCollectionNewStock : stockCollectionNew) {
                if (!stockCollectionOld.contains(stockCollectionNewStock)) {
                    Symbol oldSymbolOfStockCollectionNewStock = stockCollectionNewStock.getSymbol();
                    stockCollectionNewStock.setSymbol(symbol);
                    stockCollectionNewStock = em.merge(stockCollectionNewStock);
                    if (oldSymbolOfStockCollectionNewStock != null && !oldSymbolOfStockCollectionNewStock.equals(symbol)) {
                        oldSymbolOfStockCollectionNewStock.getStockCollection().remove(stockCollectionNewStock);
                        oldSymbolOfStockCollectionNewStock = em.merge(oldSymbolOfStockCollectionNewStock);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = symbol.getId();
                if (findSymbol(id) == null) {
                    throw new NonexistentEntityException("The symbol with id " + id + " no longer exists.");
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
            Symbol symbol;
            try {
                symbol = em.getReference(Symbol.class, id);
                symbol.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The symbol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Stock> stockCollectionOrphanCheck = symbol.getStockCollection();
            for (Stock stockCollectionOrphanCheckStock : stockCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Symbol (" + symbol + ") cannot be destroyed since the Stock " + stockCollectionOrphanCheckStock + " in its stockCollection field has a non-nullable symbol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Market market = symbol.getMarket();
            if (market != null) {
                market.getSymbolCollection().remove(symbol);
                market = em.merge(market);
            }
            em.remove(symbol);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

//    public List<Symbol> findSymbolEntities() {
//        return findSymbolEntities(true, -1, -1);
//    }
//
//    public List<Symbol> findSymbolEntities(int maxResults, int firstResult) {
//        return findSymbolEntities(false, maxResults, firstResult);
//    }

//    private List<Symbol> findSymbolEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Symbol.class));
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

    public Symbol findSymbol(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Symbol.class, id);
        } finally {
            em.close();
        }
    }

//    public int getSymbolCount() {
//        EntityManager em = getEntityManager();
//        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            Root<Symbol> rt = cq.from(Symbol.class);
//            cq.select(em.getCriteriaBuilder().count(rt));
//            Query q = em.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
//        } finally {
//            em.close();
//        }
//    }
    
}
