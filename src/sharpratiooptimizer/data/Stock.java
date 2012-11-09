/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sharpratiooptimizer.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author axjyb
 */
@Entity
@Table(name = "STOCK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s"),
    @NamedQuery(name = "Stock.findById", query = "SELECT s FROM Stock s WHERE s.id = :id"),
    @NamedQuery(name = "Stock.findByDate", query = "SELECT s FROM Stock s WHERE s.date = :date"),
    @NamedQuery(name = "Stock.findByMktopen", query = "SELECT s FROM Stock s WHERE s.mktopen = :mktopen"),
    @NamedQuery(name = "Stock.findByHigh", query = "SELECT s FROM Stock s WHERE s.high = :high"),
    @NamedQuery(name = "Stock.findByLow", query = "SELECT s FROM Stock s WHERE s.low = :low"),
    @NamedQuery(name = "Stock.findByMktclose", query = "SELECT s FROM Stock s WHERE s.mktclose = :mktclose"),
    @NamedQuery(name = "Stock.findByVolume", query = "SELECT s FROM Stock s WHERE s.volume = :volume"),
    @NamedQuery(name = "Stock.findByAdjClose", query = "SELECT s FROM Stock s WHERE s.adjClose = :adjClose")})
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue 
    private Long id;
    @Basic(optional = false)
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "MKTOPEN")
    private BigDecimal mktopen;
    @Basic(optional = false)
    @Column(name = "HIGH")
    private BigDecimal high;
    @Basic(optional = false)
    @Column(name = "LOW")
    private BigDecimal low;
    @Basic(optional = false)
    @Column(name = "MKTCLOSE")
    private BigDecimal mktclose;
    @Column(name = "VOLUME")
    private Long volume;
    @Basic(optional = false)
    @Column(name = "ADJ_CLOSE")
    private BigDecimal adjClose;
    @JoinColumn(name = "SYMBOL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Symbol symbol;

    public Stock() {
    }

    public Stock(Long id) {
        this.id = id;
    }

    public Stock(Long id, Date date, BigDecimal mktopen, BigDecimal high, BigDecimal low, BigDecimal mktclose, BigDecimal adjClose) {
        this.id = id;
        this.date = date;
        this.mktopen = mktopen;
        this.high = high;
        this.low = low;
        this.mktclose = mktclose;
        this.adjClose = adjClose;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getMktopen() {
        return mktopen;
    }

    public void setMktopen(BigDecimal mktopen) {
        this.mktopen = mktopen;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getMktclose() {
        return mktclose;
    }

    public void setMktclose(BigDecimal mktclose) {
        this.mktclose = mktclose;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(BigDecimal adjClose) {
        this.adjClose = adjClose;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
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
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sharpratiooptimizer.data.Stock[ id=" + id + " ]";
    }
    
}
