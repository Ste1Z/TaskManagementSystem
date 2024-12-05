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
import ru.effectivemobile.taskmanagementsystem.security.Role;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserAdmin;
import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserOwner;

/**
 * Реализация сервиса для работы с задачами.
 * Предоставляет методы для управления задачами, их преобразования и получения списка задач с фильтрацией.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserServiceImpl userService;

    /**
     * Создает новую задачу, устанавливая текущего пользователя как автора.
     *
     * @param task задача для создания.
     */
    @Override
    @Transactional
    public void createTask(Task task) {
        task.setAuthor(userService.getCurrentUser());
        taskRepository.save(task);
    }

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return {@link Task}.
     * @throws TaskNotFoundException если задача не найдена.
     */
    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id %s not found", id)));
    }

    /**
     * Обновляет задачу по идентификатору.
     * Обновление полей зависит от роли {@link Role} или авторства
     *
     * @param id            идентификатор задачи.
     * @param taskForUpdate объект с новыми данными для обновления задачи.
     */
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

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     */
    @Override
    @Transactional
    public void deleteTask(UUID id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    /**
     * Преобразует объект {@link TaskDto} в {@link Task}.
     *
     * @param taskDto DTO задачи.
     * @return {@link Task}.
     */
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

    /**
     * Преобразует объект {@link Task} в {@link TaskDto}.
     *
     * @param task сущность задачи.
     * @return {@link TaskDto}.
     */
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

    /**
     * Преобразует список задач в список DTO задач.
     *
     * @param taskList список задач.
     * @return {@link List<TaskDto>}.
     */
    @Override
    public List<TaskDto> taskListToDtoList(List<Task> taskList) {
        if (taskList == null || taskList.isEmpty()) {
            return new ArrayList<>();
        }
        return taskList.stream()
                .map(this::taskToTaskDto)
                .toList();
    }

    /**
     * Обновляет комментарии задачи.
     *
     * @param task задача с обновленными комментариями.
     */
    @Override
    @Transactional
    public void updateTaskComments(Task task) {
        taskRepository.save(task);
    }

    /**
     * Возвращает список задач текущего пользователя с фильтрацией.
     *
     * @param title    название задачи.
     * @param status   статус задачи.
     * @param priority приоритет задачи.
     * @param author   автор задачи.
     * @param executor исполнитель задачи.
     * @param pageable параметры пагинации.
     * @return {@link Page<TaskDto>}.
     */
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

    /**
     * Возвращает список задач, назначенных текущему пользователю, с фильтрацией.
     *
     * @param title    название задачи.
     * @param status   статус задачи.
     * @param priority приоритет задачи.
     * @param author   автор задачи.
     * @param executor исполнитель задачи.
     * @param pageable параметры пагинации.
     * @return {@link Page<TaskDto>}.
     */
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

    /**
     * Проверяет и преобразует строку в {@link Status}.
     *
     * @param status строковое представление статуса.
     * @return {@link Status} или {@code null}, если строка пустая.
     */
    private Status checkAndGetStatus(String status) {
        return (status != null) ? Status.valueOf(status) : null;
    }

    /**
     * Проверяет и преобразует строку в {@link Priority}.
     *
     * @param priority строковое представление приоритета.
     * @return {@link Priority} или {@code null}, если строка пустая.
     */
    private Priority checkAndGetPriority(String priority) {
        return (priority != null) ? Priority.valueOf(priority) : null;
    }

    /**
     * Проверяет и преобразует строку в {@link User} (автор).
     *
     * @param author строковое представление имени автора.
     * @return {@link User} или {@code null}, если строка пустая.
     */
    private User checkAndGetAuthor(String author) {
        return (author != null) ? userService.getUserByUsername(author) : null;
    }

    /**
     * Проверяет и преобразует строку в {@link User} (исполнитель).
     *
     * @param executor строковое представление имени исполнителя.
     * @return {@link User} или {@code null}, если строка пустая.
     */
    private User checkAndGetExecutor(String executor) {
        return (executor != null) ? userService.getUserByUsername(executor) : null;
    }
}
