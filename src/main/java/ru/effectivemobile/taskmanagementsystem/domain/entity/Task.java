package ru.effectivemobile.taskmanagementsystem.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Сущность задачи.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * Id задачи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Заголовок задачи.
     */
    private String title;

    /**
     * Описание задачи.
     */
    private String description;

    /**
     * {@link Status} задачи.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * {@link Priority} задачи.
     */
    @Enumerated(EnumType.STRING)
    private Priority priority;

    /**
     * {@link List<String>} с комментариями к задаче.
     */
    @ElementCollection
    @CollectionTable(name = "task_comments", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "comment")
    private List<String> comments;

    /**
     * {@link User}, являющийся автором задачи.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "author")
    private User author;

    /**
     * {@link User}, являющийся исполнителем задачи.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "executor")
    private User executor;
}
