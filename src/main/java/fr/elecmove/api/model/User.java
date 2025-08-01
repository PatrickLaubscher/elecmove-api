package fr.elecmove.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_table", schema = "elecmove")
public class User {

    @Id
    @UuidGenerator
    private String id;

    private String firstname;
    private String lastname;

    private LocalDate birthdate;
    private String mobile;
    private String email;
    private String address;
    private String zipcode;
    private String city;
    private String pwd;

    @Column(name = "is_validated")
    private Boolean isValidated;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    List<Station> stations = new ArrayList<>();

    public User(String id, String firstname, String lastname, LocalDate birthdate, String mobile, String email, String address, String zipcode, String city, String pwd, Boolean isValidated, LocalDateTime createdAt, LocalDateTime updatedAt, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.pwd = pwd;
        this.isValidated = isValidated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    public User() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }

    public Boolean getIsValidated() { return isValidated; }
    public void setIsValidated(Boolean isValidated) { this.isValidated = isValidated; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
