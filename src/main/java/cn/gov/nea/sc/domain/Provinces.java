package cn.gov.nea.sc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Provinces.
 */
@Entity
@Table(name = "provinces")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Provinces implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "province_name", length = 30, nullable = false)
    private String provinceName;

    @ManyToOne(optional = false)
    @NotNull
    private Countries countryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public Provinces provinceName(String provinceName) {
        this.provinceName = provinceName;
        return this;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Countries getCountryName() {
        return countryName;
    }

    public Provinces countryName(Countries countries) {
        this.countryName = countries;
        return this;
    }

    public void setCountryName(Countries countries) {
        this.countryName = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Provinces provinces = (Provinces) o;
        if (provinces.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), provinces.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Provinces{" +
            "id=" + getId() +
            ", provinceName='" + getProvinceName() + "'" +
            "}";
    }
}
