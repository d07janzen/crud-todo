package com.davidjanzen.crudtodo;


import com.davidjanzen.crudtodo.model.Todo;
import com.davidjanzen.crudtodo.payload.TodoRequest;
import com.davidjanzen.crudtodo.repository.TodoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoIntegrationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoIntegrationTests.class);

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TodoRepository todoRepository;

    private static long counter;

    @Test
    public void givenTodoRequest_whenRestPostTodos_thenCorrect() {
        restInsert();
    }

    @Test
    public void given_whenRestGetTodos_thenCorrect() {
        // Given
        // When
        final ResponseEntity<List> responseEntity = testRestTemplate.getForEntity("/todos", List.class);
        LOGGER.info(responseEntity.toString());
        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenId_whenRestGetTodos_thenCorrect() {
        // Given
        final Long id = restInsert().getId();
        // When
        final ResponseEntity<Todo> responseEntity = testRestTemplate.getForEntity("/todos/{id}", Todo.class, id);
        LOGGER.info(responseEntity.toString());
        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenIdAndTitle_whenRestPutTodos_thenCorrect() {
        // Given
        final String newTitle = "Updated title";
        final Todo insertedTodo = restInsert();
        insertedTodo.setTitle(newTitle);
        // When
        final HttpEntity<Todo> request = new HttpEntity<>(insertedTodo);
        final ResponseEntity<Todo> responseEntity = testRestTemplate.exchange("/todos/" + insertedTodo.getId(), HttpMethod.PUT, request, Todo.class);
        LOGGER.info(responseEntity.toString());
        // Then
        final Todo updatedTodo = todoRepository.findById(counter).get();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedTodo.getTitle()).isEqualTo(newTitle);
    }

    @Test
    public void givenId_whenRestDeleteTodos_thenCorrect() {
        // Given
        final Long id = restInsert().getId();
        // When
        final ResponseEntity<?> responseEntity = testRestTemplate.exchange("/todos/" + id, HttpMethod.DELETE, null, ResponseEntity.class);
        LOGGER.info(responseEntity.toString());
        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenId_whenRestPutTodosCompleteTodo_thenCorrect() {
        // Given
        final Long id = restInsert().getId();
        // When
        final ResponseEntity<Todo> responseEntity = testRestTemplate.exchange("/todos/" + id + "/complete-todo", HttpMethod.PUT, null, Todo.class);
        LOGGER.info(responseEntity.toString());
        // Then
        final Todo updatedTodo = todoRepository.findById(counter).get();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedTodo.isCompleted()).isEqualTo(true);
    }

    @Test
    public void givenId_whenRestPutTodosIncompleteTodo_thenCorrect() {
        // Given
        final Long id = restInsert().getId();
        // When
        final ResponseEntity<Todo> responseEntity = testRestTemplate.exchange("/todos/" + id + "/incomplete-todo", HttpMethod.PUT, null, Todo.class);
        LOGGER.info(responseEntity.toString());
        // Then
        final Todo updatedTodo = todoRepository.findById(counter).get();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedTodo.isCompleted()).isEqualTo(false);
    }

    private TodoRequest createTodoRequest() {
        counter++;
        return new TodoRequest("Title" + counter);
    }

    private Todo restInsert() {
        // Given
        final TodoRequest todoRequest = createTodoRequest();
        // When
        final ResponseEntity<Todo> responseEntity = testRestTemplate.postForEntity("/todos", todoRequest, Todo.class);
        LOGGER.info(responseEntity.toString());
        // Then
        final Todo insertedTodo = todoRepository.findById(counter).get();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(insertedTodo.isCompleted()).isEqualTo(false);
        assertThat(insertedTodo.getTitle()).isEqualTo(todoRequest.getTitle());

        return insertedTodo;
    }

}
