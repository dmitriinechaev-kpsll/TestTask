package com.example.archtst.dto;

import lombok.Data;

@Data
public class UserSearchRequestDTO {
    private String  email;
    private String  name;
    private Integer olderThan;
    private Integer minAge;
    private Integer maxAge;
}
