package fr.elecmove.api.controller.dto.user;

import fr.elecmove.api.controller.dto.role.RoleDTO;

public class UserConnectedDTO {

    private String id;
    private String email;
    private RoleDTO role;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }
}
