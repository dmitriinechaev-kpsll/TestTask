package com.example.archtst.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserSearchRequestDTO { // reqFilter
    private String  email;
    private Set<String> names;
    //private Integer olderThan;  // перешли на minAge и maxAge
    private Integer minAge;
    private Integer maxAge;

    // для пагинации по умолчанию
    private int page = 0;       // Первая страница
    private int size = 20;      // кол-во страниц
    private String sortBy   = "name";
    private String sortDir  = "asc";
}
