package ru.AMosk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.AMosk.entity.FileEntity;

import java.util.Optional;

public interface CloudRepository extends JpaRepository<FileEntity,String> {
    Optional<FileEntity> findByFilename(String fileName);

}
