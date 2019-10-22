package com.davidjanzen.crudtodo.controller;

import com.davidjanzen.crudtodo.model.Todo;
import com.davidjanzen.crudtodo.repository.TodoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class TodoControllerTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoControllerTests.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepository;

    // CREATE

    @Test
    public void givenNoBody_whenRestPostTodos_thenBadRequest() throws Exception {
        this.mockMvc.perform(post("/todos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNotJsonContentType_whenRestPostTodos_thenBadRequest() throws Exception {
        this.mockMvc.perform(post("/todos")
                .content("{\"title\":\"\"}"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void givenBlankTitle_whenRestPostTodos_thenBadRequest() throws Exception {
        this.mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    // READ

    @Test
    public void givenNotFoundId_whenRestGetTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/todos/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNotNumberId_whenRestGetTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/todos/{id}", 'a'))
                .andExpect(status().isBadRequest());
    }

    // UPDATE

    @Test
    public void givenNoBody_whenRestPutTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(put("/todos/{id}", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNotJsonContentType_whenRestPutTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(put("/todos/{id}", 1)
                .content("{\"title\":\"\"}"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void givenNotFoundIdAndTitle_whenRestPutTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(put("/todos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"title\":\"Title\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNotNumberIdAndTitle_whenRestPutTodos_thenBadRequest() throws Exception {
        this.mockMvc.perform(put("/todos/{id}", 'a')
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"title\":\"Title\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenIdAndBlankTitle_whenRestPutTodos_thenBadRequest() throws Exception {
        when(this.todoRepository.findById(1L)).thenReturn(Optional.of(new Todo(1L, "Title")));
        this.mockMvc.perform(put("/todos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    // DELETE

    @Test
    public void givenNotFoundId_whenRestDeleteTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(delete("/todos/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNotNumberId_whenRestDeleteTodos_thenNotFound() throws Exception {
        this.mockMvc.perform(delete("/todos/{id}", 'a'))
                .andExpect(status().isBadRequest());
    }

    // UPDATE SPECIFIC

    @Test
    public void givenNotFoundId_whenRestPutTodosCompleteTodo() throws Exception {
        this.mockMvc.perform(put("/todos/{id}/complete-todo", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNotNumberId_whenRestPutTodosCompleteTodo() throws Exception {
        this.mockMvc.perform(put("/todos/{id}/complete-todo", '@'))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNotFoundId_whenRestPutTodosIncompleteTodo() throws Exception {
        this.mockMvc.perform(put("/todos/{id}/incomplete-todo", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNotNumberId_whenRestPutTodosIncompleteTodo() throws Exception {
        this.mockMvc.perform(put("/todos/{id}/incomplete-todo", 'a'))
                .andExpect(status().isBadRequest());
    }

}
