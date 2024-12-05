package ru.effectivemobile.taskmanagementsystem.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    void createTask(Task task);

    Task getTaskById(UUID id);

    void updateTask(UUID id, TaskDto taskForUpdate);

    void deleteTask(UUID id);

    Task taskDtoToTask(TaskDto taskDto);

    TaskDto taskToTaskDto(Task task);

    List<TaskDto> taskListToDtoList(List<Task> taskList);

    void updateTaskComments(Task task);

    Page<TaskDto> getMyTasksWithFilters(String title, String status, String priority,
                                        String author, String executor, Pageable pageable);

    Page<TaskDto> getAssignedTasksWithFilters(String title, String status, String priority,
                                              String author, String executor, Pageable pageable);
}
