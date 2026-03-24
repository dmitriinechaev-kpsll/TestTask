package com.example.archtst.mapper;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import com.example.archtst.entity.Role;
import com.example.archtst.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    // Spring автоматически внедрит бин ModelMapper
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Entity -> ResponseDTO
    public UserResponseDTO userToResponseDTO(User user) {
        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
        if (user.getRoles() != null) {
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }
        return dto;
    }

    // Page<Entity> -> Page<ResponseDTO>
    public Page<UserResponseDTO> pageToResponseDTO(Page<User> usersPage) {
        return usersPage.map(this::userToResponseDTO);
    }

    // RequestDTO -> Entity
    public User requestToEntity(UserRequestDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}