package com.example.proje.repo;

import com.example.proje.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query(value = "select * from task where user_id = :user_id order by create_date",
            nativeQuery = true)
    List<Task> findTaskFromUserId(@Param("user_id") Long user_id);

    Task findTaskByText(String text);
    @Query(value = "select * from task where user_id = :userId and finished = 1 order by create_date",
            nativeQuery = true)
    List<Task> findFinishedTask(@Param("userId") Long userId);
}
