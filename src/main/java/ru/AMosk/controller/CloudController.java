package ru.AMosk.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.AMosk.dto.FileInfoDto;
import ru.AMosk.services.CloudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CloudController {

    private final CloudService cloudService;


    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<FileInfoDto> user(Authentication authentication) {
        cloudService.getFile();
        return null;
    }
}
