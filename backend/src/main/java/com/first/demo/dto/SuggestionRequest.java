package com.first.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuggestionRequest {
    private String category;
    private String suggestionText;
}

