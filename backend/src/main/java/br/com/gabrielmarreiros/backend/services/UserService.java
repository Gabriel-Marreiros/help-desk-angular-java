package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        return this.userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public boolean verifyUserAlreadyRegistered(String email){
        return this.userRepository.findByEmail(email).isPresent();
    }

    public boolean isUserHimselfOrAdmin(UUID userId){
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(loggedUser.isAdmin()){
            return true;
        }

        if(loggedUser.getId().equals(userId)){
            return true;
        }

        return false;
    }
}
