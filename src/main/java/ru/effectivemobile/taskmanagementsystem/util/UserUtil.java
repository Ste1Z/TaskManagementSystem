package ru.effectivemobile.taskmanagementsystem.util;

import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;
import ru.effectivemobile.taskmanagementsystem.security.Role;

public class UserUtil {

    public static boolean isCurrentUserAdmin(User user) {
        return user.getRoles().contains(Role.ROLE_ADMIN);
    }

    public static boolean isCurrentUserOwner(User user, Task task) {
        return user.getUsername().equals(task.getAuthor().getUsername());
    }
}
