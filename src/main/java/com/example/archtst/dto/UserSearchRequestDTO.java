package com.example.archtst.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserSearchRequestDTO { // reqFilter
    private String  email;
    private Set<String> names;
    private Integer olderThan;
    private Integer minAge;
    private Integer maxAge;
}
