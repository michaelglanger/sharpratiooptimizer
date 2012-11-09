/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.data;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author axjyb
 */
@Entity
@Table(name = "SYMBOL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Symbol.findAll", query = "SELECT s FROM Symbol s"),
    @NamedQuery(name = "Symbol.findById", query = "SELECT s FROM Symbol s WHERE s.id = :id"),
    @NamedQuery(name = "Symbol.findBySymbol", query = "SELECT s FROM Symbol s WHERE s.symbol = :symbol")})
public class Symbol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue 
    private Long id;
    @Basic(optional = false)
    @Column(name = "SYMBOL")
    private String symbol;
    @JoinColumn(name = "MARKET", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Market market;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "symbol")
    private Collection<Stock> stockCollection;

    public Symbol() {
    }

    public Symbol(Long id) {
        this.id = id;
    }

    public Symbol(Long id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @XmlTransient
    public Collection<Stock> getStockCollection() {
        return stockCollection;
    }

    public void setStockCollection(Collection<Stock> stockCollection) {
        this.stockCollection = stockCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Symbol)) {
            return false;
        }
        Symbol other = (Symbol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sharpratiooptimizer.data.Symbol[ id=" + id + " ]";
    }
    
}
