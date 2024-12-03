package ru.effectivemobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Priority;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Status;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.exception.TaskNotFoundException;
import ru.effectivemobile.taskmanagementsystem.repository.TaskRepository;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserAdmin;
import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserOwner;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserServiceImpl userService;

    @Override
    public void createTask(Task task) {
        task.setAuthor(userService.getCurrentUser());
        taskRepository.save(task);
    }

    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id %s not found", id)));
    }

    @Override
    public void updateTask(UUID id, TaskDto taskForUpdate) {
        User currentUser = userService.getCurrentUser();
        Task task = getTaskById(id);
        if (isCurrentUserAdmin(currentUser)) {
            task.setStatus(Status.valueOf(taskForUpdate.getStatus()));
            task.setPriority(Priority.valueOf(taskForUpdate.getPriority()));
            task.setExecutor(userService.getUserByUsername(taskForUpdate.getExecutor()));
            task.setComment(taskForUpdate.getComment());
            taskRepository.save(task);
        }
        if (isCurrentUserOwner(currentUser, task)) {
            task.setStatus(Status.valueOf(taskForUpdate.getStatus()));
            task.setComment(taskForUpdate.getComment());
            taskRepository.save(task);
        }
    }

    @Override
    public void deleteTask(UUID id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    @Override
    public Task taskDtoToTask(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(Status.valueOf(taskDto.getStatus()))
                .priority(Priority.valueOf(taskDto.getPriority()))
                .comment(taskDto.getComment())
                .executor(userService.getUserByUsername(taskDto.getExecutor()))
                .build();
    }

    @Override
    public TaskDto taskToTaskDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getComment(),
                task.getExecutor().getUsername()
        );
    }

    @Override
    public List<TaskDto> taskListToDtoList(List<Task> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            return new ArrayList<>();
        }
        return taskList.stream()
                .map(task -> new TaskDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus().name(),
                        task.getPriority().name(),
                        task.getComment(),
                        task.getExecutor().getUsername()
                ))
                .toList();
    }
}
