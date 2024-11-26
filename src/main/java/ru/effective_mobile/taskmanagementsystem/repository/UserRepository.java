package ru.effective_mobile.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effective_mobile.taskmanagementsystem.domain.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
