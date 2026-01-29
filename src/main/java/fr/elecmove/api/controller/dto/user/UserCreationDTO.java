package fr.elecmove.api.controller.dto.user;

import jakarta.validation.constraints.*;

public class UserCreationDTO {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Pattern(regexp = "^\\d{10}$")
    private String mobile;

    @NotBlank
    @Email
    private String email;

    private Boolean isValidated = false;

    @NotBlank
    private String password;



    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getValidated() {
        return isValidated;
    }

    public void setValidated(Boolean validated) {
        isValidated = validated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
