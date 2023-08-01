package ru.AMosk.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "files",
        schema = "cloud")
@Builder
public class FileEntity {

    @Id
    @Column(name = "hash_id")
    private String hash;

    @Column(name = "filename")
    private String filename;

    @Column(name = "size")
    private Long size;

    @Column(name = "created_time")
    private LocalDateTime createdTime;
}
