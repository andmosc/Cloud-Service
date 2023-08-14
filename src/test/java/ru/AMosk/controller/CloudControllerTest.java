package ru.AMosk.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.AMosk.dto.FileInfoDto;
import ru.AMosk.services.CloudService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class CloudControllerTest {

    @Mock
    private CloudService cloudService;

    @InjectMocks
    private CloudController cloudController;

    @Test
    public void getFile_returnIsOk() {
        List<FileInfoDto> files = List.of(new FileInfoDto("file", "1111"));
        doReturn(files).when(this.cloudService).getFiles(1);

        List<FileInfoDto> file = cloudController.getFile(1);

        assertNotNull(file);
        assertEquals(files, file);
    }
}
