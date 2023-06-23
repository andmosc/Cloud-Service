package ru.AMosk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "authorities", schema = "cloud")
public class Authorities implements GrantedAuthority {

    @EmbeddedId
    private AuthoritiesData authoritiesData;

    @Override
    public String getAuthority() {
        return authoritiesData.getAuthority();
    }
}
