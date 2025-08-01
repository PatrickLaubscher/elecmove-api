package fr.elecmove.api.controller.dto.user;

import java.util.List;

public class UserListDTO {

    private List<UserSingleDTO> user;

    public UserListDTO(List<UserSingleDTO> user) {
        this.user = user;
    }

    public List<UserSingleDTO> getUser() {
        return user;
    }

    public void setUser(List<UserSingleDTO> user) {
        this.user = user;
    }
}
