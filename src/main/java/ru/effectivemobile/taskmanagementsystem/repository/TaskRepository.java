package ru.effectivemobile.taskmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Priority;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Status;
import ru.effectivemobile.taskmanagementsystem.domain.entity.Task;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью {@link Task}.
 * Предоставляет методы для выполнения запросов и фильтрации задач.
 */
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    /**
     * Находит задачи, созданные текущим пользователем, с учетом заданных фильтров.
     *
     * @param username имя пользователя (автора задач).
     * @param title    название задачи (может быть {@code null}).
     * @param status   статус задачи (может быть {@code null}).
     * @param priority приоритет задачи (может быть {@code null}).
     * @param author   автор задачи (может быть {@code null}).
     * @param executor исполнитель задачи (может быть {@code null}).
     * @param pageable параметры постраничного вывода.
     * @return страница задач, соответствующих указанным критериям.
     */
    @Query(value = """
                SELECT t FROM Task t
                JOIN User u ON t.author = u
                WHERE u.username = :username
                  AND (:title IS NULL OR t.title =:title)
                  AND (:status IS NULL OR t.status = :status)
                  AND (:priority IS NULL OR t.priority = :priority)
                  AND (:author IS NULL OR t.author = :author)
                  AND (:executor IS NULL OR t.executor = :executor)
            """)
    Page<Task> findTasksOfCurrentUserWithFilters(
            @Param("username") String username, @Param("title") String title, @Param("status") Status status,
            @Param("priority") Priority priority, @Param("author") User author, @Param("executor") User executor,
            Pageable pageable
    );

    /**
     * Находит задачи, назначенные текущему пользователю, с учетом заданных фильтров.
     *
     * @param username имя пользователя (исполнителя задач).
     * @param title    название задачи (может быть {@code null}).
     * @param status   статус задачи (может быть {@code null}).
     * @param priority приоритет задачи (может быть {@code null}).
     * @param author   автор задачи (может быть {@code null}).
     * @param executor исполнитель задачи (может быть {@code null}).
     * @param pageable параметры постраничного вывода.
     * @return страница задач, соответствующих указанным критериям.
     */
    @Query(value = """
                SELECT t FROM Task t
                JOIN User u ON t.executor = u
                WHERE u.username = :username
                  AND (:title IS NULL OR t.title =:title)
                  AND (:status IS NULL OR t.status = :status)
                  AND (:priority IS NULL OR t.priority = :priority)
                  AND (:author IS NULL OR t.author = :author)
                  AND (:executor IS NULL OR t.executor = :executor)
            """)
    Page<Task> findTasksAssignedToCurrentUserWithFilters(
            @Param("username") String username, @Param("title") String title, @Param("status") Status status,
            @Param("priority") Priority priority, @Param("author") User author, @Param("executor") User executor,
            Pageable pageable
    );
}
