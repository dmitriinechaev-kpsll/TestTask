package com.example.archtst.mapper;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.model.UserModel;
import com.example.archtst.persistence.projection.UserWithRolesProjection;
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

    public UserSearchCriteria toCriteria(UserSearchRequestDTO dto) {
        if (dto == null) return null;
        return new UserSearchCriteria(
                dto.getEmail(),
                dto.getNames(),
                dto.getMinAge(),
                dto.getMaxAge()
        );
    }

    public UserResponseDTO toResponseDTO(UserWithRolesProjection projection) {
        if (projection == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(projection.getId().toString());
        dto.setName(projection.getName());
        dto.setEmail(projection.getEmail());
        dto.setAge(projection.getAge());
        dto.setShoeSize(projection.getShoeSize());
        dto.setRoles(projection.getRoles());
        return dto;
    }

    public Page<UserResponseDTO> pageToResponseDTO(Page<UserWithRolesProjection> page) {
        return page.map(this::toResponseDTO);
    }
}