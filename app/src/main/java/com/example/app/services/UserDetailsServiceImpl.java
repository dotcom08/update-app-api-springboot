package com.example.app.services;

import com.example.app.exceptions.ResourceNotFoundException;
import com.example.app.models.User;
import com.example.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:" + username));

        // Принудительно загружаем lazy-коллекции
        if (user.getRole() != null && user.getRole().getPermissions() != null) {
            user.getRole().getPermissions().size(); // Принудительная загрузка
        }

        return user;
    }

}
