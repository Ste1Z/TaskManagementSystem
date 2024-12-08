package ru.effectivemobile.taskmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import ru.effectivemobile.taskmanagementsystem.domain.dto.RegistrationUserDto;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskCommentsDto;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.exception.ErrorMessage;
import ru.effectivemobile.taskmanagementsystem.service.impl.TaskServiceImpl;
import ru.effectivemobile.taskmanagementsystem.service.impl.UserServiceImpl;

import java.util.List;
import java.util.UUID;

import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserAdmin;
import static ru.effectivemobile.taskmanagementsystem.util.UserUtil.isCurrentUserOwner;

/**
 * Контроллер для обработки запросов связанных с задачами.
 */
@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;

    /**
     * Создает новую задачу.
     *
     * @param taskDto DTO объекта задачи.
     * @return {@link ResponseEntity<TaskDto>} или сообщение об ошибке.
     */
    @Operation(summary = "Create a new task", description = "Allows an admin to create a new task",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task successfully created",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(responseCode = "200", description = "Invalid creation data",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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

    /**
     * Получает задачу по идентификатору.
     *
     * @param id идентификатор задачи.
     * @return {@link ResponseEntity<TaskDto>} или сообщение об ошибке.
     */
    @Operation(summary = "Get task by ID", description = "Allows the owner or an admin to retrieve a task by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully retrieved",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Task with id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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

    /**
     * Обновляет задачу.
     *
     * @param id            идентификатор задачи.
     * @param taskForUpdate DTO объекта задачи для обновления.
     * @return {@link ResponseEntity<TaskDto>}.
     */
    @Operation(summary = "Update task", description = "Allows an admin or owner to update task details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Task successfully updated",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(responseCode = "404", description = "Task with id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskDto taskForUpdate) {
        taskService.updateTask(id, taskForUpdate);
        TaskDto createdTask = taskService.taskToTaskDto(taskService.getTaskById(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Удаляет задачу.
     *
     * @param id идентификатор задачи.
     * @return сообщение об успешном удалении или ошибке.
     */
    @Operation(summary = "Delete task", description = "Allows an admin to delete a task by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task successfully deleted",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Task with id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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

    /**
     * Получает список задач текущего пользователя с фильтрацией.
     *
     * @param title    фильтр по заголовку.
     * @param status   фильтр по статусу.
     * @param priority фильтр по приоритету.
     * @param author   фильтр по автору.
     * @param executor фильтр по исполнителю.
     * @param pageable объект для пагинации.
     * @return {@link ResponseEntity<Page>}.
     */
    @Operation(summary = "Get tasks created by the current user",
            description = "Retrieve a paginated and filtered list of tasks assigned to the current user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/myTasks")
    public ResponseEntity<?> getMyTasksWithFilters(@RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(required = false) String priority,
                                                   @RequestParam(required = false) String author,
                                                   @RequestParam(required = false) String executor,
                                                   Pageable pageable) {
        return ResponseEntity.ok(taskService.getMyTasksWithFilters(title, status, priority, author, executor, pageable));
    }

    /**
     * Получает список задач, назначенных текущему пользователю, с фильтрацией.
     *
     * @param title    фильтр по заголовку.
     * @param status   фильтр по статусу.
     * @param priority фильтр по приоритету.
     * @param author   фильтр по автору.
     * @param executor фильтр по исполнителю.
     * @param pageable объект для пагинации.
     * @return {@link ResponseEntity<Page>}.
     */
    @Operation(summary = "Get tasks assigned to the current user",
            description = "Retrieve a paginated and filtered list of tasks assigned to the current user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Page.class)))
            }
    )
    @GetMapping("/assignedTasks")
    public ResponseEntity<?> getAssignedTasksWithFilters(@RequestParam(required = false) String title,
                                                         @RequestParam(required = false) String status,
                                                         @RequestParam(required = false) String priority,
                                                         @RequestParam(required = false) String author,
                                                         @RequestParam(required = false) String executor,
                                                         Pageable pageable) {
        return ResponseEntity.ok(taskService.getAssignedTasksWithFilters(title, status, priority, author, executor, pageable));
    }

    /**
     * Получает комментарии задачи.
     *
     * @param id идентификатор задачи.
     * @return {@link ResponseEntity<List>}.
     */
    @Operation(summary = "Get comments of a task", description = "Retrieve comments for a specific task by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskCommentsDto.class))),
                    @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Task with id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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

    /**
     * Добавляет комментарий к задаче.
     *
     * @param id      идентификатор задачи.
     * @param comment DTO объекта комментария.
     * @return обновленный список комментариев или сообщение об ошибке.
     */
    @Operation(summary = "Add comment to a task", description = "Allows the owner or an admin to add a comment to a task.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment added successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskCommentsDto.class))),
                    @ApiResponse(responseCode = "403", description = "You do not have permission to access this resource",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Task with id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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
