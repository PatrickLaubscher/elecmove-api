package fr.elecmove.api.controller.dto.user_address;

import java.util.List;

public class UserAddressListDTO {

    public UserAddressListDTO(List<UserAddressDTO> userAddressList) {
        this.userAddressList = userAddressList;
    }

    public List<UserAddressDTO> getUserAddressList() {
        return userAddressList;
    }

    public void setUserAddressList(List<UserAddressDTO> userAddressList) {
        this.userAddressList = userAddressList;
    }

    private List<UserAddressDTO> userAddressList;

}
