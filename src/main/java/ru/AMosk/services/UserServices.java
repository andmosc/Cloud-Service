package ru.AMosk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServices {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        UserCloud user = userRepository.findByEmail(email);
//        if (user == null) {
//            throw new UsernameNotFoundException("unknown user " + email);
//        }
//
//        Set<Authorities> authority = user.getAuthority();
//
//        UserDetails userDetails = User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .roles(String.valueOf(authority))
//                .build();
//        return userDetails;
//    }
}
