package fr.elecmove.api.controller.dto;

import fr.elecmove.api.controller.dto.user.UserConnectedDTO;

public class LoginResponseDTO {

    private String token;
    private UserConnectedDTO user;

    public LoginResponseDTO(String token, UserConnectedDTO user) {
        this.token = token;
        this.user = user;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserConnectedDTO getUser() {
        return user;
    }
    public void setUser(UserConnectedDTO  user) {
        this.user = user;
    }

}
