package org.jhipster.health.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import org.jhipster.health.domain.enumeration.Units;

/**
 * A Settings.
 */
@Entity
@Table(name = "settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "settings")
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "weeklygoal")
    private Integer weeklygoal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weightuntits", nullable = false)
    private Units weightuntits;

    @OneToOne
    @JoinColumn(unique = true)
    private User settings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeeklygoal() {
        return weeklygoal;
    }

    public Settings weeklygoal(Integer weeklygoal) {
        this.weeklygoal = weeklygoal;
        return this;
    }

    public void setWeeklygoal(Integer weeklygoal) {
        this.weeklygoal = weeklygoal;
    }

    public Units getWeightuntits() {
        return weightuntits;
    }

    public Settings weightuntits(Units weightuntits) {
        this.weightuntits = weightuntits;
        return this;
    }

    public void setWeightuntits(Units weightuntits) {
        this.weightuntits = weightuntits;
    }

    public User getSettings() {
        return settings;
    }

    public Settings settings(User user) {
        this.settings = user;
        return this;
    }

    public void setSettings(User user) {
        this.settings = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Settings settings = (Settings) o;
        if (settings.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, settings.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Settings{" +
            "id=" + id +
            ", weeklygoal='" + weeklygoal + "'" +
            ", weightuntits='" + weightuntits + "'" +
            '}';
    }
}
