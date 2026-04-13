package com.example.archtst.dto;

import java.util.Set;

public record UserSearchCriteria(
        String email,
        Set<String> names,
        Integer minAge,
        Integer maxAge
){
}
