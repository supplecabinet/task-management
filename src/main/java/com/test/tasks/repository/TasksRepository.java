package com.test.tasks.repository;

import com.test.tasks.model.UserTasks;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<UserTasks,Integer> {

    List<UserTasks> findByUserId(String userId);
    List<UserTasks> findByUserIdAndStatus(String userId, Integer status);

}
