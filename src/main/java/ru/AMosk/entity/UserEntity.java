package ru.AMosk.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@Table(name = "users", schema = "cloud")
public class UserEntity {

    @Id
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private short enabled;

    @OneToMany(targetEntity = Authorities.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private Set<Authorities> authority;

}
