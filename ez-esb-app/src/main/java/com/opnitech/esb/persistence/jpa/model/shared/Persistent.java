package com.opnitech.esb.persistence.jpa.model.shared;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Rigre Gregorio Garciandia Sonora
 */
@MappedSuperclass
@GenericGenerator(name = "system-uuid", strategy = "increment")
public abstract class Persistent implements Serializable {

    private static final long serialVersionUID = 3553338608700565562L;

    private Long id;
    private long version;

    public Persistent() {

        // Default constructor
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(this.id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(Objects.equals(getClass(), obj.getClass()))) {
            return false;
        }

        Persistent other = (Persistent) obj;

        return new EqualsBuilder().append(this.id, other.getId()).isEquals();
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {

        return this.id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    @Version
    @Column(name = "version")
    public long getVersion() {

        return this.version;
    }

    public void setVersion(long version) {

        this.version = version;
    }
}
