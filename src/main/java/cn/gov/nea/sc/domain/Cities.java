package cn.gov.nea.sc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Cities.
 */
@Entity
@Table(name = "cities")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "city_name", length = 30, nullable = false)
    private String cityName;

    @ManyToOne(optional = false)
    @NotNull
    private Provinces provinceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public Cities cityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Provinces getProvinceName() {
        return provinceName;
    }

    public Cities provinceName(Provinces provinces) {
        this.provinceName = provinces;
        return this;
    }

    public void setProvinceName(Provinces provinces) {
        this.provinceName = provinces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cities cities = (Cities) o;
        if (cities.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cities.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cities{" +
            "id=" + getId() +
            ", cityName='" + getCityName() + "'" +
            "}";
    }
}
