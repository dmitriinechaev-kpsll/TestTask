package com.example.archtst.mapper;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.entity.User;
import org.modelmapper.ModelMapper;
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
        if (user == null) return null;
        return modelMapper.map(user, ResponseDTO.class);
    }

    // List<Entity> -> List<ResponseDTO>
    public List<ResponseDTO> listToResponseDTO(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::userToResponseDTO)
                .collect(Collectors.toList());
    }

    // RequestDTO -> Entity
    public User requestToEntity(RequestDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, User.class);
    }
}
/*
package com.example.archtst.mapper;

import com.example.archtst.dto.RequestDTO;
import com.example.archtst.dto.ResponseDTO;
import com.example.archtst.entity.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    // Entity -> ResponseDTO
    public ResponseDTO userToResponseDTO(User user) {
        if  (user == null) return null;

        return ResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                //.formattedCreatedAt(user.getCreatedAt() != null ?
                //        user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : null)
                .build();
    }

    public List<ResponseDTO> listToResponseDTO(List<User> users) {
        return users.stream()
                .map(this::userToResponseDTO)
                .collect(Collectors.toList());
    }

    // RequestDTO -> Entity
    public User requestToEntity(RequestDTO dto) {
        if  (dto == null) return null;

        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();
    }

}*/
