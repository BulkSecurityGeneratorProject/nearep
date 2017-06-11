package cn.gov.nea.sc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Districts.
 */
@Entity
@Table(name = "districts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Districts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "district_name", length = 30, nullable = false)
    private String districtName;

    @NotNull
    @Size(max = 10)
    @Column(name = "post_code", length = 10, nullable = false)
    private String postCode;

    @ManyToOne(optional = false)
    @NotNull
    private Cities cityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public Districts districtName(String districtName) {
        this.districtName = districtName;
        return this;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getPostCode() {
        return postCode;
    }

    public Districts postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Cities getCityName() {
        return cityName;
    }

    public Districts cityName(Cities cities) {
        this.cityName = cities;
        return this;
    }

    public void setCityName(Cities cities) {
        this.cityName = cities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Districts districts = (Districts) o;
        if (districts.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), districts.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Districts{" +
            "id=" + getId() +
            ", districtName='" + getDistrictName() + "'" +
            ", postCode='" + getPostCode() + "'" +
            "}";
    }
}
