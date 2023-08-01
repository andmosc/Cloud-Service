package ru.AMosk.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInfoDto {
    private String filename;
    private String size;
}
