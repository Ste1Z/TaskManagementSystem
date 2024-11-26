package ru.effective_mobile.taskmanagementsystem.service;

import ru.effective_mobile.taskmanagementsystem.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    void createTask(Task task);

    Task getTaskById(UUID id);

    List<Task> getAllTasks();
}
