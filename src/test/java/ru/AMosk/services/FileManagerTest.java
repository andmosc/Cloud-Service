package ru.AMosk.services;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.io.Resource;
import ru.AMosk.services.impl.FileManagerImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileManagerTest {
    private static final String HASH_ID = "1234554321";
    private static final String PATH_TEST_FILE = "src\\test\\resources\\";
    private FileManagerImpl fileManager;

    @BeforeAll
    void init() throws IOException {
        fileManager = new FileManagerImpl();
        fileManager.setStorage(PATH_TEST_FILE);
    }

    @Test
    void addFile_shouldCreateFilesInStorage() throws IOException {
        Path checkFile = Paths.get(PATH_TEST_FILE, HASH_ID);

        fileManager.addFile(new byte[]{1, 2, 3}, HASH_ID);

        assertTrue(Files.exists(checkFile));
        Files.delete(checkFile);
    }

    @Test
    void dellFile_shouldFileFromStorage() throws IOException {
        Path checkFile = Paths.get(PATH_TEST_FILE, HASH_ID);
        Files.createFile(checkFile);

        fileManager.delFile(HASH_ID);

        assertFalse(Files.exists(checkFile));
    }

    @Test
    void download_shouldGetResourceFromStorage() throws IOException {
        Path path = Paths.get(PATH_TEST_FILE, HASH_ID);
        Files.createFile(path);

        Resource download = fileManager.download(HASH_ID);

        assertNotNull(download);
        assertThrows(IOException.class,() -> fileManager.download(HASH_ID+"1"));
        Files.delete(path);
    }
}