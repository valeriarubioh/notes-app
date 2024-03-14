package com.spring.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class NoteResponse {
    private Long id;
    private String content;
    private Boolean archived;
    private String title;
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<CategoryResponse> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
