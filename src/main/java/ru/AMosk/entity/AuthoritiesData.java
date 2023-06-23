package ru.AMosk.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data
@NoArgsConstructor
public class AuthoritiesData implements Serializable {

    @Column(name = "email")
    private String email;

    @Column(name = "authority")
    private String authority;
}
