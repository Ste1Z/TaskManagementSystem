package ru.effectivemobile.taskmanagementsystem.service;

import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    void createTask(Task task);

    Task getTaskById(UUID id);

    List<Task> getAllTasks();
}
