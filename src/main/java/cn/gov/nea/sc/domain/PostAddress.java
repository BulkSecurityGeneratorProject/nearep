package cn.gov.nea.sc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PostAddress.
 */
@Entity
@Table(name = "post_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "street_address", length = 200, nullable = false)
    private String streetAddress;

    @Size(max = 20)
    @Column(name = "office_tel", length = 20)
    private String officeTel;

    @Size(max = 20)
    @Column(name = "office_fax", length = 20)
    private String officeFax;

    @Size(max = 100)
    @Column(name = "office_email", length = 100)
    private String officeEmail;

    @ManyToOne(optional = false)
    @NotNull
    private Districts districtName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public PostAddress streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public PostAddress officeTel(String officeTel) {
        this.officeTel = officeTel;
        return this;
    }

    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getOfficeFax() {
        return officeFax;
    }

    public PostAddress officeFax(String officeFax) {
        this.officeFax = officeFax;
        return this;
    }

    public void setOfficeFax(String officeFax) {
        this.officeFax = officeFax;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public PostAddress officeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
        return this;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public Districts getDistrictName() {
        return districtName;
    }

    public PostAddress districtName(Districts districts) {
        this.districtName = districts;
        return this;
    }

    public void setDistrictName(Districts districts) {
        this.districtName = districts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostAddress postAddress = (PostAddress) o;
        if (postAddress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postAddress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostAddress{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", officeTel='" + getOfficeTel() + "'" +
            ", officeFax='" + getOfficeFax() + "'" +
            ", officeEmail='" + getOfficeEmail() + "'" +
            "}";
    }
}
