package ru.effectivemobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.exception.TaskNotFoundException;
import ru.effectivemobile.taskmanagementsystem.repository.TaskRepository;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id %s not found", id)));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
