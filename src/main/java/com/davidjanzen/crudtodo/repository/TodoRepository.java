package com.davidjanzen.crudtodo.repository;

import com.davidjanzen.crudtodo.model.Todo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface TodoRepository {

    @Insert("INSERT INTO todos(title, completed) " +
            " VALUES (#{title}, #{completed})")
    @Options(useGeneratedKeys=true, keyProperty = "id")
    Integer insert(Todo todo);

    @Select("SELECT * FROM todos")
    List<Todo> findAll();

    @Select("SELECT * FROM todos WHERE id = #{id}")
    Optional<Todo> findById(Long id);

    @Update("UPDATE todos SET title=#{title} WHERE id=#{id}")
    Integer update(Todo todo);

    @Delete("DELETE FROM todos WHERE id = #{id}")
    void deleteById(Long id);

    @Update("UPDATE todos SET completed=#{completed} WHERE id=#{id}")
    Integer updateCompleted(Todo todo);

}
