package com.confinale.awesomo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Subsidiary.
 */
@Entity
@Table(name = "subsidiary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Subsidiary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User parent;

    @OneToOne
    @JoinColumn(unique = true)
    private User child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getParent() {
        return parent;
    }

    public Subsidiary parent(User user) {
        this.parent = user;
        return this;
    }

    public void setParent(User user) {
        this.parent = user;
    }

    public User getChild() {
        return child;
    }

    public Subsidiary child(User user) {
        this.child = user;
        return this;
    }

    public void setChild(User user) {
        this.child = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subsidiary subsidiary = (Subsidiary) o;
        if(subsidiary.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, subsidiary.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Subsidiary{" +
            "id=" + id +
            '}';
    }
}
