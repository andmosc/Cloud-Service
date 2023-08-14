package ru.AMosk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.AMosk.dto.FileDto;
import ru.AMosk.dto.FileInfoDto;
import ru.AMosk.entity.FileEntity;
import ru.AMosk.exception.UploadException;
import ru.AMosk.mapper.FileMapper;
import ru.AMosk.repository.CloudRepository;
import ru.AMosk.services.CloudService;
import ru.AMosk.services.FileManager;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CloudServiceImpl implements CloudService {

    private final CloudRepository cloudRepository;
    private final FileMapper mapper;
    private final FileManager fileManager;

    @Override
    public List<FileInfoDto> getFiles(int limit) {
        log.info("Getting file list");
        return cloudRepository.findAll(Pageable.ofSize(limit))
                .map(mapper::toDto).toList();
    }

    @Override
    public void addFile(String filename, MultipartFile multipartFile) {
        try {
            log.info("Checking the existence of file {}", filename);
            if (multipartFile == null) {
                log.warn("File is not attached to request");
                throw new UploadException("File is not attached to request", 1);
            }

            if (cloudRepository.findByFilename(filename).isPresent()) {
                log.warn("File with name {} already exist", multipartFile);
                throw new UploadException("File with name " + filename + " already exist", 1);
            }

            String generateHashId = generateHash(filename);

            FileEntity fileEntity = getFileEntity(multipartFile, generateHashId);

            //todo save minio
            fileManager.addFile(multipartFile.getBytes(), generateHashId);

            log.info("saving information about the file [ {} ] in the database", filename);
            cloudRepository.save(fileEntity);
            log.info("file info of {} saved to database", filename);
        } catch (IOException ex) {
            throw new UploadException(ex.getMessage(), 1);
        }
    }

    @Override
    public void delFile(String filename) {
        FileEntity entity = getEntity(filename);

        if (entity != null) {
            cloudRepository.delete(entity);
            log.info("file was deleted from the database");
            //todo del minio
            try {
                fileManager.delFile(entity.getHash());
                log.info("file was deleted from the storage");
            } catch (IOException ex) {
                throw new UploadException(ex.getMessage(), 1);
            }
        }
    }

    @Override
    public FileDto getFile(String filename) {
        FileEntity entity = getEntity(filename);
        try {
            Resource resource = fileManager.download(entity.getHash());
            return FileDto.builder()
                    .hash(entity.getHash())
                    .file(resource.toString())
                    .build();
        } catch (IOException ex) {
            throw new UploadException(ex.getMessage(),1);
        }
    }

    @Override
    public void renameFile(String filename, String newName) {
        FileEntity entity = getEntity(filename);
        entity.setFilename(newName);
        cloudRepository.save(entity);
        log.info("file rename {} -> {}",filename,newName);
    }

    private FileEntity getEntity(String filename) {
        log.info("get Entity {}", filename);
        return cloudRepository.findByFilename(filename).orElseThrow(() ->
                new UploadException(String.format("File %s not found", filename), 1));
    }

    private static FileEntity getFileEntity(MultipartFile multipartFile, String hashId) {
        log.info("get FileEntity");
        return FileEntity.builder()
                .hash(hashId)
                .createdTime(LocalDateTime.now())
                .filename(multipartFile.getOriginalFilename())
                .size(multipartFile.getSize())
                .build();
    }

    public String generateHash(String name) {
        log.info("Generation hash file");
        return DigestUtils.sha1DigestAsHex(name + LocalDateTime.now());
    }

}
