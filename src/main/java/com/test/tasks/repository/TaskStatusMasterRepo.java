package com.test.tasks.repository;

import com.test.tasks.model.TaskStatusMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusMasterRepo extends JpaRepository<TaskStatusMaster,Integer> {
}
