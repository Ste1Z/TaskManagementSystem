package ru.effective_mobile.taskmanagementsystem;

import org.springframework.boot.SpringApplication;

public class TestTaskManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(TaskManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
