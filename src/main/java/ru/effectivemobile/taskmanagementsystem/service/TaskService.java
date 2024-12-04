package ru.effectivemobile.taskmanagementsystem.service;

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
}
