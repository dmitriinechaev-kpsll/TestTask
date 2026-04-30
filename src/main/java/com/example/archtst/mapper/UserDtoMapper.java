package com.example.archtst.mapper;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.model.UserModel;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    private final ModelMapper modelMapper;

    public UserDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserModel toModel(UserRequestDTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, UserModel.class);
    }

    public UserResponseDTO toResponseDTO(UserModel model) {
        if (model == null) return null;
        UserResponseDTO dto = modelMapper.map(model, UserResponseDTO.class);
        if (model.getUserRoles() != null) {
            Set<com.example.archtst.enums.RoleName> roleNames = model.getUserRoles().stream()
                    .map(ur -> ur.getRole().getName())
                    .collect(Collectors.toSet());
            dto.setRoles(roleNames);
        }
        return dto;
    }

    public Page<UserResponseDTO> pageToResponseDTO(Page<UserModel> usersPage) {
        return usersPage.map(this::toResponseDTO);
    }

    public UserSearchCriteria toCriteria(UserSearchRequestDTO dto) {
        if (dto == null) return null;
        return new UserSearchCriteria(
                dto.getEmail(),
                dto.getNames(),
                dto.getMinAge(),
                dto.getMaxAge()
        );
    }
}