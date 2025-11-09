package com.example.app.mappers;

import com.example.app.dtos.UserDto;
import com.example.app.dtos.UserLoggedDto;
import com.example.app.models.Permission;
import com.example.app.models.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto userToUserDto(User user){
        return new UserDto(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().getAuthority(),
                user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }

    public static UserLoggedDto userToUserLoggedDto(User user){
        return new UserLoggedDto(user.getUsername(),
                user.getRole().getAuthority(),
                user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));

    }
}
