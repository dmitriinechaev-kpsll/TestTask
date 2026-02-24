package com.example.archtst.mapper;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    // Spring автоматически внедрит бин ModelMapper
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Entity -> ResponseDTO
    public ResponseDTO userToResponseDTO(User user) {
        return modelMapper.map(user, ResponseDTO.class);
    }

    // Page<Entity> -> Page<ResponseDTO>
    public Page<ResponseDTO> pageToResponseDTO(Page<User> usersPage) {
        return usersPage.map(this::userToResponseDTO);
    }

    // RequestDTO -> Entity
    public User requestToEntity(RequestDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}