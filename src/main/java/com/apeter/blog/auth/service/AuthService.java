package com.apeter.blog.auth.service;

import com.apeter.blog.auth.api.request.AuthRequest;
import com.apeter.blog.auth.entity.CustomUserDetails;
import com.apeter.blog.auth.exceptions.AuthException;
import com.apeter.blog.base.service.EmailSenderService;
import com.apeter.blog.security.JwtFiler;
import com.apeter.blog.security.JwtProvider;
import com.apeter.blog.user.exception.UserNoExistException;
import com.apeter.blog.user.model.UserDoc;
import com.apeter.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;

    public CustomUserDetails loadUserByEmail(String email) throws UserNoExistException {
        UserDoc userDoc = userRepository.findByEmail(email).orElseThrow(UserNoExistException::new);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userDoc);
    }

    public String auth(AuthRequest authRequest) throws AuthException, UserNoExistException {
        UserDoc userDoc = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(UserNoExistException::new);
        if (userDoc.getPassword().equals((UserDoc.hexPassword(authRequest.getPassword()))) == false) {
            userDoc.setFailLogin(userDoc.getFailLogin() + 1);
            userRepository.save(userDoc);

            if (userDoc.getFailLogin() >= 5) {
                emailSenderService.sendEmailAlert(userDoc.getEmail());
            }

            throw new AuthException();
        }

        if (userDoc.getFailLogin() > 0) {
            userDoc.setFailLogin(0);
            userRepository.save(userDoc);
        }

        String token = jwtProvider.generateToken(authRequest.getEmail());
        return token;
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    public UserDoc currentUser() throws AuthException {
        try {
            String email = jwtProvider.getEmailFromToken(JwtFiler.getTokenFromRequest(getCurrentHttpRequest()));
            UserDoc userDoc = userRepository.findByEmail(email).orElseThrow(UserNoExistException::new);
            return userDoc;
        } catch (Exception e) {
            throw new AuthException();
        }
    }

}
