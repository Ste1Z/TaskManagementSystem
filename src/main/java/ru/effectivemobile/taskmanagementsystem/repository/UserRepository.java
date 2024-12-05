package ru.effectivemobile.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностями {@link User}.
 * Предоставляет методы для взаимодействия с базой данных пользователей.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Ищет пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска.
     * @return {@link Optional}, содержащий найденного пользователя, или пустой, если пользователь не найден.
     */
    Optional<User> findByUsername(String username);
}
