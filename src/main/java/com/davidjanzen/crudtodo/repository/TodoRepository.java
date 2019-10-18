package com.davidjanzen.crudtodo.repository;

import com.davidjanzen.crudtodo.model.Todo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TodoRepository {

    @Insert("INSERT INTO todos(title, is_completed) " +
            " VALUES (#{title}, #{isCompleted})")
    Integer insert(Todo employee);

    @Select("SELECT * FROM todos")
    List<Todo> findAll();

    @Select("SELECT * FROM todos WHERE id = #{id}")
    Todo findById(Long id);

    @Update("UPDATE todos SET title=#{title} where id=#{id}")
    Integer update(Todo employee);

    @Delete("DELETE FROM todos WHERE id = #{id}")
    void deleteById(Long id);

}
