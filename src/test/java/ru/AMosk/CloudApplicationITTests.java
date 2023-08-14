package ru.AMosk;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class CloudApplicationITTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String url;
    private int mappedPort;
    private MockMultipartFile mockMultipartFile;
    @Container
    private final static GenericContainer<?> cloudService = new GenericContainer<>("cloud-service")
            .withExposedPorts(8080);

    @BeforeEach
    public void init() {
        mappedPort = cloudService.getMappedPort(8080);
        mockMultipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "file content".getBytes()
        );
        url = "http://localhost:" + mappedPort + "/file";
    }

    @Test
    @Order(1)
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void uploadFile_correctData_IsOk() throws Exception {
        mockMvc.perform(
                        multipart(url).file(mockMultipartFile)
                                .param("filename", "test.txt")

                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void uploadFile_fileAlreadyExist_ShouldReturnError() throws Exception {
        mockMvc.perform(
                        multipart(url).file(mockMultipartFile)
                                .param("filename", "test.txt")

                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.message")
                        .value(String.format("File with name %s already exist", mockMultipartFile.getOriginalFilename())));
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = {"ROLE_USER"})
    public void uploadFile_userNotRole_ShouldReturn403() throws Exception {
        mockMvc.perform(
                        multipart(url).file(mockMultipartFile)
                                .param("filename", "test.txt")

                ).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
    public void getFile_fileExist_isOK() throws Exception {
        mockMvc.perform(get(url)
                        .param("filename", "test.txt"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hash").exists())
                .andExpect(jsonPath("$.file").exists());
    }

    @Test
    @Order(5)
    @WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
    public void getFile_fileNotExist_shouldReturnError() throws Exception {
        String fileName = "testFile.txt";
        mockMvc.perform(get(url)
                        .param("filename", fileName))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.message")
                        .value(String.format("File %s not found", fileName)));
    }

    @Test
    @Order(6)
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void renameFile_fileExist_isOk() throws Exception {
        Map<String, String> newFileNAme = new HashMap<>();
        newFileNAme.put("filename", "test2.txt");

        mockMvc.perform(put(url)
                        .param("filename", "test.txt")
                        .content(objectMapper.writeValueAsString(newFileNAme))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void renameFile_fileNotFound_shouldReturnErrorEx() throws Exception {
        String fileName = "test1111.txt";
        Map<String, String> newFileNAme = new HashMap<>();
        newFileNAme.put("filename", "test2.txt");

        mockMvc.perform(put(url)
                        .param("filename", fileName)
                        .content(objectMapper.writeValueAsString(newFileNAme))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value(String.format("File %s not found", fileName)))
                .andExpect(jsonPath("$.id")
                        .value("1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @WithMockUser(authorities = {"ROLE_USER"})
    public void renameFile_userNotRole_ShouldReturn403() throws Exception {
        Map<String, String> newFileNAme = new HashMap<>();
        newFileNAme.put("filename", "test2.txt");

        mockMvc.perform(put(url)
                        .param("filename", "test.txt")
                        .content(objectMapper.writeValueAsString(newFileNAme))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    @WithMockUser(authorities = {"ROLE_USER", "ROLE_ADMIN"})
    public void getFile_uploadFileList_ShouldReturnList() throws Exception {
        url = "http://localhost:" + mappedPort + "/list";

        mockMvc.perform(get(url).param("limit", "3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].filename")
                        .value("test2.txt"))
                .andExpect(jsonPath("$[0].size")
                        .exists())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void delFile_fileExist_isOk() throws Exception {
        mockMvc.perform(delete(url).param("filename", "test2.txt"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    public void delFile_fileNotFound_shouldReturnErrorEx() throws Exception {
        String fileName = "test";
        mockMvc.perform(delete(url).param("filename", fileName))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value(String.format("File %s not found", fileName)))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(status().isBadRequest());
    }
}
