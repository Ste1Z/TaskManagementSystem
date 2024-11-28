package ru.effectivemobile.taskmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.domain.dto.TaskDto;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.service.impl.TaskServiceImpl;
import ru.effectivemobile.taskmanagementsystem.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;

    @GetMapping("/myTasks")
    public ResponseEntity<List<TaskDto>> getMyTasks() {
        User user = userService.getUserByUsername(userService.getUsernameOfCurrentUser());
        List<TaskDto> dtoList = taskService.taskListToDtoList(user.getMyTasks());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/assignedTasks")
    public ResponseEntity<List<TaskDto>> getAssignedTasks() {
        User user = userService.getUserByUsername(userService.getUsernameOfCurrentUser());
        List<TaskDto> dtoList = taskService.taskListToDtoList(user.getAssignedTasks());
        return ResponseEntity.ok(dtoList);
    }
}
