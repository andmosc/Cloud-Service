package ru.AMosk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.AMosk.dto.FileDto;
import ru.AMosk.dto.FileInfoDto;
import ru.AMosk.services.CloudService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CloudController {

    private final CloudService cloudService;
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<FileInfoDto> getFile(@RequestParam int limit) {
        return  cloudService.getFiles(limit);
    }

    @PostMapping("/file")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(@RequestParam String filename, @RequestBody MultipartFile file) {
        cloudService.addFile(filename,file);
    }

    //todo query
    @DeleteMapping("/file")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(@RequestParam String filename) {
        cloudService.delFile(filename);
    }

    @GetMapping("/file")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public FileDto getFile(@RequestParam String filename) {
        return cloudService.getFile(filename);
    }

    @PutMapping("/file")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void renameFile(@RequestParam String filename, @RequestBody Map<String,String> newName) {
        cloudService.renameFile(filename,newName.get("filename"));
    }
}
