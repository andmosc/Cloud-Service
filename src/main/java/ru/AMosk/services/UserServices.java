package ru.AMosk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.AMosk.entity.Authorities;
import ru.AMosk.entity.UserCloud;
import ru.AMosk.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServices implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserCloud user = userRepository.findByEmail(email);
        if (user == null) {
            // todo log неизвестный пользователь
            throw new UsernameNotFoundException("unknown user " + email);
        }

        Set<Authorities> authority = user.getAuthority();

        UserDetails userDetails = User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(String.valueOf(authority))
                .build();
        return userDetails;
    }
}
