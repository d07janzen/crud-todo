package com.davidjanzen.crudtodo.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class TodoRequest {

    public TodoRequest() {
    }

    public TodoRequest(@NotBlank String title) {
        this.title = title;
    }

    @NotBlank
    private String title;

    public String getTitle() {
        return title;
    }

}
