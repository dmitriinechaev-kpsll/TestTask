package com.example.archtst.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
    private String email;
    private Set<String> names;
    private Integer minAge;
    private Integer maxAge;
}
