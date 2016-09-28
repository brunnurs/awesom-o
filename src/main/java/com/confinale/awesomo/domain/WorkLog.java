package com.confinale.awesomo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WorkLog.
 */
@Entity
@Table(name = "work_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "approved", nullable = false)
    private Boolean approved;

    @NotNull
    @Column(name = "work_from", nullable = false)
    private ZonedDateTime workFrom;

    @NotNull
    @Column(name = "work_to", nullable = false)
    private ZonedDateTime workTo;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isApproved() {
        return approved;
    }

    public WorkLog approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public ZonedDateTime getWorkFrom() {
        return workFrom;
    }

    public WorkLog workFrom(ZonedDateTime workFrom) {
        this.workFrom = workFrom;
        return this;
    }

    public void setWorkFrom(ZonedDateTime workFrom) {
        this.workFrom = workFrom;
    }

    public ZonedDateTime getWorkTo() {
        return workTo;
    }

    public WorkLog workTo(ZonedDateTime workTo) {
        this.workTo = workTo;
        return this;
    }

    public void setWorkTo(ZonedDateTime workTo) {
        this.workTo = workTo;
    }

    public User getUser() {
        return user;
    }

    public WorkLog user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public WorkLog project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkLog workLog = (WorkLog) o;
        if(workLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, workLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WorkLog{" +
            "id=" + id +
            ", approved='" + approved + "'" +
            ", workFrom='" + workFrom + "'" +
            ", workTo='" + workTo + "'" +
            '}';
    }
}
