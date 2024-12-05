package ru.effectivemobile.taskmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.domain.dto.CommentDto;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskCommentsDto;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.service.impl.TaskServiceImpl;
import ru.effectivemobile.taskmanagementsystem.service.impl.UserServiceImpl;

import java.util.UUID;

import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserAdmin;
import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserOwner;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto taskDto) {
        User currentUser = userService.getCurrentUser();
        if (isCurrentUserAdmin(currentUser)) {
            Task task = taskService.taskDtoToTask(taskDto);
            taskService.createTask(task);
            TaskDto createdTask = taskService.taskToTaskDto(taskService.getTaskById(task.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to access this resource"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable UUID id) {
        User currentUser = userService.getCurrentUser();
        Task task = taskService.getTaskById(id);
        if (isCurrentUserOwner(currentUser, task) || isCurrentUserAdmin(currentUser)) {
            return ResponseEntity.ok(taskService.taskToTaskDto(task));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to access this resource"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskDto taskForUpdate) {
        taskService.updateTask(id, taskForUpdate);
        TaskDto createdTask = taskService.taskToTaskDto(taskService.getTaskById(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID id) {
        User currentUser = userService.getCurrentUser();
        if (isCurrentUserAdmin(currentUser)) {
            taskService.deleteTask(id);
            return ResponseEntity.ok(new ErrorMessage(HttpStatus.NO_CONTENT.value(), "Task successfully deleted"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to access this resource"));
        }
    }

    @GetMapping("/myTasks")
    public ResponseEntity<?> getMyTasksWithFilters(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(required = false) String priority,
                                                   @RequestParam(required = false) String author,
                                                   @RequestParam(required = false) String executor,
                                                   Pageable pageable) {
        return ResponseEntity.ok(taskService.getMyTasksWithFilters(title, status, priority, author, executor, pageable));
    }

    @GetMapping("/assignedTasks")
    public ResponseEntity<?> getAssignedTasksWithFilters(@RequestParam(required = false) String title,
                                                         @RequestParam(required = false) String status,
                                                         @RequestParam(required = false) String priority,
                                                         @RequestParam(required = false) String author,
                                                         @RequestParam(required = false) String executor,
                                                         Pageable pageable) {
        return ResponseEntity.ok(taskService.getAssignedTasksWithFilters(title, status, priority, author, executor, pageable));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getTaskComments(@PathVariable UUID id) {
        User currentUser = userService.getCurrentUser();
        Task task = taskService.getTaskById(id);
        if (isCurrentUserOwner(currentUser, task) || isCurrentUserAdmin(currentUser)) {
            return ResponseEntity.ok(new TaskCommentsDto(task.getId(), task.getComments()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to access this resource"));
        }
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addCommentToTask(@PathVariable UUID id, @Valid @RequestBody CommentDto comment) {
        User currentUser = userService.getCurrentUser();
        Task task = taskService.getTaskById(id);
        task.getComments().add(comment.getComment());
        taskService.updateTaskComments(task);
        if (isCurrentUserOwner(currentUser, task) || isCurrentUserAdmin(currentUser)) {
            return ResponseEntity.ok(new TaskCommentsDto(task.getId(), task.getComments()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                    "You do not have permission to access this resource"));
        }
    }
}
