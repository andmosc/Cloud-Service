package ru.AMosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class FileDto {
    private String hash;
    private String file;
}
