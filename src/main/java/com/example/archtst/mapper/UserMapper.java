package com.example.archtst.mapper;

import com.example.archtst.dto.UserRequestDTO;
import com.example.archtst.dto.UserResponseDTO;
import com.example.archtst.dto.UserSearchCriteria;
import com.example.archtst.dto.UserSearchRequestDTO;
import com.example.archtst.entity.User;
import com.example.archtst.enums.RoleName;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Entity -> UserResponseDTO
    public UserResponseDTO userToResponseDTO(User user) {
        UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
        if (user.getUserRoles() != null) {
            Set<RoleName> roleNames = user.getUserRoles().stream()
                    .map(ur -> ur.getRole().getName())
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

    public UserSearchCriteria toCriteria(UserSearchRequestDTO dto){
        if (dto == null) {
            return null;
        }

        return new UserSearchCriteria(
                dto.getEmail(),
                dto.getNames(),
                dto.getMinAge(),
                dto.getMaxAge()
        );
    }
}