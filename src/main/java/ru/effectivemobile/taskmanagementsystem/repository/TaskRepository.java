package ru.effectivemobile.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
