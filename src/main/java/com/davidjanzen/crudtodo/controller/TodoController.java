package com.davidjanzen.crudtodo.controller;

import com.davidjanzen.crudtodo.exception.ResourceNotFoundException;
import com.davidjanzen.crudtodo.model.Todo;
import com.davidjanzen.crudtodo.payload.TodoRequest;
import com.davidjanzen.crudtodo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository todoRepository;

    @PostMapping
    public ResponseEntity<Todo> insert(@Valid @RequestBody TodoRequest todoRequest) {
        final Todo todo = new Todo(todoRequest.getTitle());
        todoRepository.insert(todo);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(todo.getId()).toUri();

        return ResponseEntity.created(location)
                .body(todo);
    }

    @GetMapping
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Todo findById(@PathVariable Long id) {
        return todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody TodoRequest todoRequest) {
        todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));

        final Todo todo = new Todo(id, todoRequest.getTitle());
        todoRepository.update(todo);

        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));

        todoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete-todo")
    public ResponseEntity<Todo> completeTodo(@PathVariable Long id) {
        final Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));

        todo.setCompleted(true);
        todoRepository.updateCompleted(todo);

        return ResponseEntity.ok(todo);
    }

    @PutMapping("/{id}/incomplete-todo")
    public ResponseEntity<Todo> incompleteTodo(@PathVariable Long id) {
        final Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo", "id", id));

        todo.setCompleted(false);
        todoRepository.updateCompleted(todo);

        return ResponseEntity.ok(todo);
    }

}
