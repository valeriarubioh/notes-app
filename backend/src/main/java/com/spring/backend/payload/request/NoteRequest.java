package com.spring.backend.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteRequest {

    @NotBlank(message = "Content field required")
    private String content;
    @NotNull(message = "archived field required")
    private Boolean archived;
    @NotBlank(message = "title field required")
    private String title;
    private CategoryRequest category;


}
