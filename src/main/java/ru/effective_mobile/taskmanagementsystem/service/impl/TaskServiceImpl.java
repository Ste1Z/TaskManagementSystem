package ru.effective_mobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effective_mobile.taskmanagementsystem.domain.entity.Task;
import ru.effective_mobile.taskmanagementsystem.exception.TaskNotFoundException;
import ru.effective_mobile.taskmanagementsystem.repository.TaskRepository;
import ru.effective_mobile.taskmanagementsystem.service.TaskService;

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
                .orElseThrow(() -> new TaskNotFoundException("Task  not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
