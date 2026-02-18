package com.example.archtst.mapper;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

/*    // List<Entity> -> List<ResponseDTO>
    public List<ResponseDTO> listToResponseDTO(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::userToResponseDTO)
                .collect(Collectors.toList());
    }*/

    // Page<Entity> -> Page<ResponseDTO>
    public Page<ResponseDTO> pageToResponseDTO(Page<User> usersPage) {
        return usersPage.map(this::userToResponseDTO);
    }

    // RequestDTO -> Entity
    public User requestToEntity(RequestDTO dto) {
        return modelMapper.map(dto, User.class);
    }
}