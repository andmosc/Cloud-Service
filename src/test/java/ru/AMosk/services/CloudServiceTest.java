package ru.AMosk.services;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.AMosk.dto.FileDto;
import ru.AMosk.entity.FileEntity;
import ru.AMosk.exception.UploadException;
import ru.AMosk.repository.CloudRepository;
import ru.AMosk.services.impl.CloudServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CloudServiceTest {
    private static final String FILE_NAME = "file.txt";
    private MultipartFile multipartFile;
    private FileEntity file;
    @Mock
    private CloudRepository cloudRepository;
    @Mock
    private FileManager fileManager;
    @Mock
    private Resource resource;
    @InjectMocks
    private CloudServiceImpl cloudService;

    @Before
    public void init() {
        multipartFile = new MockMultipartFile("file.txt", new byte[]{1, 2, 3, 4});
        file = new FileEntity("123", FILE_NAME, 10_000L, LocalDateTime.now());
        resource = new PathResource(FILE_NAME);
    }

    @Test
    public void addFile_notAttachedRequest_ShouldReturnThrowEx() {
        assertThatThrownBy(() -> cloudService.addFile(FILE_NAME, null))
                .isInstanceOf(UploadException.class)
                .hasMessage("File is not attached to request");
    }

    @Test
    public void addFile_fileAlreadyExist_ShouldReturnThrowEx() {
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.ofNullable(file));

        assertThatThrownBy(() -> cloudService.addFile(FILE_NAME, multipartFile))
                .isInstanceOf(UploadException.class)
                .hasMessage(String.format("File with name %s already exist", multipartFile.getName()));
    }

    @Test
    public void addFile_correctData_isOK() throws IOException {
        when(cloudRepository.findByFilename(FILE_NAME))
                .thenReturn(Optional.empty());

        cloudService.addFile(FILE_NAME,multipartFile);

        verify(fileManager, times(1))
                .addFile(eq(multipartFile.getBytes()), anyString());
        verify(cloudRepository, times(1))
                .save(any(FileEntity.class));
    }

    @Test
    public void generateHash_HashId_ShouldDifferent() {
        assertThat(cloudService.generateHash("test"))
                .isNotEqualTo(cloudService.generateHash("test"));
    }

    @Test
    public void dellFile_fileNotFound_shouldReturnThrowEx() {
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cloudService.delFile(FILE_NAME))
                .isInstanceOf(UploadException.class)
                .hasMessage(String.format("File %s not found", FILE_NAME));
    }

    @Test
    public void dellFile_correctData_isOK() throws IOException {
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.ofNullable(file));

        cloudService.delFile(FILE_NAME);

        verify(cloudRepository, times(1)).delete(file);
        verify(fileManager, times(1)).delFile(file.getHash());
    }

    @Test
    public void getFile_fileNotFound_shouldReturnThrowEx() {
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cloudService.getFile(FILE_NAME))
                .isInstanceOf(UploadException.class)
                .hasMessage(String.format("File %s not found", FILE_NAME));
    }

    @Test
    public void getFile_correctData_isOK() throws IOException {
        FileDto fileExpected = new FileDto("123",resource.toString());
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.ofNullable(file));
        when(fileManager.download(file.getHash())).thenReturn(resource);

        FileDto fileActual = cloudService.getFile(FILE_NAME);

        assertThat(fileActual).isEqualTo(fileExpected);
        verify(fileManager,times(1)).download(file.getHash());
     }

    @Test
    public void renameFile_fileNotFound_shouldReturnThrowEx() {
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cloudService.renameFile(FILE_NAME,anyString()))
                .isInstanceOf(UploadException.class)
                .hasMessage(String.format("File %s not found", FILE_NAME));
    }

    @Test
    public void renameFile_correctData_isOK() {
        String newFileName = "newFileName.txt";
        when(cloudRepository.findByFilename(FILE_NAME)).thenReturn(Optional.ofNullable(file));

        cloudService.renameFile(FILE_NAME,newFileName);

        assertThat(file.getFilename()).isEqualTo(newFileName);
        verify(cloudRepository,times(1)).save(file);
    }
}
