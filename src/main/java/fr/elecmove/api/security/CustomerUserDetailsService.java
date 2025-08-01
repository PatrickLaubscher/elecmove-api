package fr.elecmove.api.security;

import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;



@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l’email : " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPwd()) // ATTENTION : ce doit être le mot de passe hashé
                .roles(user.getRole().getName().toUpperCase()) // ex: ADMIN, CUSTOMER
                .build();
    }
}
