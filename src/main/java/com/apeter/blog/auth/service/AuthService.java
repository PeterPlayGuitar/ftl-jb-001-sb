package com.apeter.blog.auth.service;

import com.apeter.blog.auth.api.request.AuthRequest;
import com.apeter.blog.auth.entity.CustomUserDetails;
import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.security.JwtProvider;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.model.UserDoc;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public CustomUserDetails loadUserByEmail(String email) throws UserNoExistException {
        UserDoc userDoc = userRepository.findByEmail(email).orElseThrow(UserNoExistException::new);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userDoc);
    }

    public String auth(AuthRequest authRequest) throws AuthException, UserNoExistException {
        UserDoc userDoc = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(UserNoExistException::new);
        if (userDoc.getPassword().equals((UserDoc.hexPassword(authRequest.getPassword()))) == false) {
            userDoc.setFailLogin(userDoc.getFailLogin() + 1);
            userRepository.save(userDoc);

            throw new AuthException();
        }

        if (userDoc.getFailLogin() > 0) {
            userDoc.setFailLogin(0);
            userRepository.save(userDoc);
        }

        String token = jwtProvider.generateToken(authRequest.getEmail());
        return token;
    }

}
