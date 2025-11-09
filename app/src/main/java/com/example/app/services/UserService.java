package com.example.app.services;


import com.example.app.dtos.UserDto;
import com.example.app.exceptions.ResourceNotFoundException;
import com.example.app.mappers.UserMapper;
import com.example.app.models.User;
import com.example.app.repositories.RoleRepository;
import com.example.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserDto> getUsers(){
        return userRepository.findAll().stream().map(UserMapper::userToUserDto).toList();
    }

    public User getByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new ResourceNotFoundException("User not found with username: " + username));

        return user;
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
}
