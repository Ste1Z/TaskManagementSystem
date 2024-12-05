package ru.effectivemobile.taskmanagementsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления задачами.
 * Предоставляет методы для создания, обновления, удаления и получения задач, а также преобразования задач в DTO и обратно.
 */
public interface TaskService {

    /**
     * Создает новую задачу.
     *
     * @param task задача для создания.
     */
    void createTask(Task task);

    /**
     * Возвращает задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return {@link Task}.
     */
    Task getTaskById(UUID id);

    /**
     * Обновляет задачу по идентификатору.
     *
     * @param id            идентификатор задачи.
     * @param taskForUpdate объект с новыми данными для обновления задачи.
     */
    void updateTask(UUID id, TaskDto taskForUpdate);

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     */
    void deleteTask(UUID id);

    /**
     * Преобразует объект {@link TaskDto} в {@link Task}.
     *
     * @param taskDto DTO задачи.
     * @return {@link Task}.
     */
    Task taskDtoToTask(TaskDto taskDto);

    /**
     * Преобразует объект {@link Task} в {@link TaskDto}.
     *
     * @param task сущность задачи.
     * @return {@link TaskDto}.
     */
    TaskDto taskToTaskDto(Task task);

    /**
     * Преобразует список задач в список DTO задач.
     *
     * @param taskList список задач.
     * @return {@link List<Task>}.
     */
    List<TaskDto> taskListToDtoList(List<Task> taskList);

    /**
     * Обновляет комментарии задачи.
     *
     * @param task задача с обновленными комментариями.
     */
    void updateTaskComments(Task task);

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
    Page<TaskDto> getMyTasksWithFilters(String title, String status, String priority,
                                        String author, String executor, Pageable pageable);

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
    Page<TaskDto> getAssignedTasksWithFilters(String title, String status, String priority,
                                              String author, String executor, Pageable pageable);
}
