package ru.effectivemobile.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanagementsystem.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
}
