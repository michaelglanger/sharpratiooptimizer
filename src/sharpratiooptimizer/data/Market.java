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
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "MARKET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Market.findAll", query = "SELECT m FROM Market m"),
    @NamedQuery(name = "Market.findById", query = "SELECT m FROM Market m WHERE m.id = :id"),
    @NamedQuery(name = "Market.findByMarketName", query = "SELECT m FROM Market m WHERE m.marketName = :marketName")})
public class Market implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_market")
    private Long id;
    @Column(name = "MARKET_NAME")
    private String marketName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "market")
    private Collection<Symbol> symbolCollection;

    public Market() {
    }

    public Market(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @XmlTransient
    public Collection<Symbol> getSymbolCollection() {
        return symbolCollection;
    }

    public void setSymbolCollection(Collection<Symbol> symbolCollection) {
        this.symbolCollection = symbolCollection;
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
        if (!(object instanceof Market)) {
            return false;
        }
        Market other = (Market) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sharpratiooptimizer.data.Market[ id=" + id + " ]";
    }
    
}
