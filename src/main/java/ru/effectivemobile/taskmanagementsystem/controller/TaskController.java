package ru.effectivemobile.taskmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanagementsystem.service.TaskService;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

//    @GetMapping("/myTasks")
//    public ResponseEntity<List<Task>> getMyTasks() {
//
//    }
//
//    @GetMapping("/assignedTasks")
//    public ResponseEntity<List<Task>> getAssignedTasks() {
//
//    }
}
