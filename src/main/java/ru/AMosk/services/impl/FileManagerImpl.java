package ru.AMosk.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import ru.AMosk.services.FileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileManagerImpl implements FileManager {

    @Value("${storage.path}")
    private String storage;

    public void setStorage(String storage) {
        this.storage = storage;
    }
    @Override
    public void addFile(byte[] content, String hash) throws IOException {

        if (!Files.isDirectory(Path.of(storage))) {
            Files.createDirectories(Path.of(storage));
        }

        Path path = Paths.get(storage, hash);
        if (!Files.exists(path)) {
            Path file = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(file.toString())) {
                stream.write(content);
            }
        }
    }

    @Override
    public void delFile(String hashId) throws IOException {
        Path path = Paths.get(storage + hashId);
        Files.delete(path);
    }

    @Override
    public Resource download(String hashId) throws IOException {
        Path path = Paths.get(storage + hashId);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }
}
