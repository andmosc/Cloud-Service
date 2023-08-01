package ru.AMosk.services;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface FileManager {
    void addFile(byte[] content, String hash) throws IOException;

    void delFile(String hashId) throws IOException;

    Resource download(String hashId) throws IOException;
}
