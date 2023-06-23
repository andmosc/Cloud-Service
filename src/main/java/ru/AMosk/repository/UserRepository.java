package ru.AMosk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.AMosk.entity.UserCloud;

@Repository
public interface UserRepository extends JpaRepository<UserCloud, String> {
    UserCloud findByEmail(String email);
}
