package ru.AMosk.services;

import org.springframework.web.multipart.MultipartFile;
import ru.AMosk.dto.FileDto;
import ru.AMosk.dto.FileInfoDto;

import java.util.List;

public interface CloudService {
    List<FileInfoDto> getFiles(int limit);

    void addFile(String filename, MultipartFile file);

    void delFile(String filename);

    FileDto getFile(String filename);

    void renameFile(String filename, String newName);

}
