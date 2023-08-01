package ru.AMosk.mapper;

import org.mapstruct.Mapper;
import ru.AMosk.dto.FileInfoDto;
import ru.AMosk.entity.FileEntity;

@Mapper(componentModel = "spring")
public interface FileMapper extends Mappable<FileEntity, FileInfoDto> {
}
