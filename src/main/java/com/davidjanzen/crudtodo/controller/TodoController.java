package com.davidjanzen.crudtodo.controller;

import com.davidjanzen.crudtodo.model.Todo;
import com.davidjanzen.crudtodo.payload.TodoRequest;
import com.davidjanzen.crudtodo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoRepository todoRepository;

    @PostMapping
    public Integer insert(@RequestBody TodoRequest todoRequest){
        return todoRepository.insert(new Todo(todoRequest.getTitle()));
    }

    @GetMapping
    public List<Todo> findAll(){
        return todoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Todo findById(@PathVariable Long id){
        return todoRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Integer update(@PathVariable Long id, @RequestBody TodoRequest todoRequest){
        return todoRepository.update(new Todo(id, todoRequest.getTitle()));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        todoRepository.deleteById(id);
    }

}
