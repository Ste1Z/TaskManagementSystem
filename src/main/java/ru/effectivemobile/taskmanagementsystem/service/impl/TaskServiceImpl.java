package ru.effectivemobile.taskmanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
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
    @Transactional
    public void updateTask(UUID id, TaskDto taskForUpdate) {
        User currentUser = userService.getCurrentUser();
        Task task = getTaskById(id);
        if (isCurrentUserAdmin(currentUser)) {
            task.setStatus(Status.valueOf(taskForUpdate.getStatus()));
            task.setPriority(Priority.valueOf(taskForUpdate.getPriority()));
            task.setExecutor(userService.getUserByUsername(taskForUpdate.getExecutor()));
            task.setComments(taskForUpdate.getComments());
            taskRepository.save(task);
        }
        if (isCurrentUserOwner(currentUser, task)) {
            task.setStatus(Status.valueOf(taskForUpdate.getStatus()));
            task.setComments(taskForUpdate.getComments());
            taskRepository.save(task);
        }
    }

    @Override
    @Transactional
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
                .comments(taskDto.getComments())
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
                task.getComments(),
                task.getAuthor().getUsername(),
                task.getExecutor().getUsername()
        );
    }

    @Override
    public List<TaskDto> taskListToDtoList(List<Task> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            return new ArrayList<>();
        }
        return taskList.stream()
                .map(this::taskToTaskDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateTaskComments(Task task) {
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public Page<TaskDto> getMyTasksWithFilters(String title, String status, String priority,
                                               String author, String executor, Pageable pageable) {
        List<TaskDto> tasks = taskRepository
                .findTasksOfCurrentUserWithFilters(
                        userService.getUsernameOfCurrentUser(),
                        title,
                        checkAndGetStatus(status),
                        checkAndGetPriority(priority),
                        checkAndGetAuthor(author),
                        checkAndGetExecutor(executor),
                        pageable)
                .stream()
                .map(this::taskToTaskDto)
                .toList();
        return new PageImpl<>(tasks);
    }

    @Override
    @Transactional
    public Page<TaskDto> getAssignedTasksWithFilters(String title, String status, String priority,
                                                     String author, String executor, Pageable pageable) {
        List<TaskDto> tasks = taskRepository
                .findTasksAssignedToCurrentUserWithFilters(
                        userService.getUsernameOfCurrentUser(),
                        title,
                        checkAndGetStatus(status),
                        checkAndGetPriority(priority),
                        checkAndGetAuthor(author),
                        checkAndGetExecutor(executor),
                        pageable)
                .stream()
                .map(this::taskToTaskDto)
                .toList();
        return new PageImpl<>(tasks);
    }

    private Status checkAndGetStatus(String status) {
        return (status != null) ? Status.valueOf(status) : null;
    }

    private Priority checkAndGetPriority(String priority) {
        return (priority != null) ? Priority.valueOf(priority) : null;
    }

    private User checkAndGetAuthor(String author) {
        return (author != null) ? userService.getUserByUsername(author) : null;
    }

    private User checkAndGetExecutor(String executor) {
        return (executor != null) ? userService.getUserByUsername(executor) : null;
    }
}
