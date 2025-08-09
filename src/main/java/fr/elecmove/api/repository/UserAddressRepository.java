package fr.elecmove.api.repository;

import fr.elecmove.api.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, String> {

    List<UserAddress> findUserAddressByUserEmail(String email);

}
