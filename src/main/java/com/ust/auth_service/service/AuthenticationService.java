package com.ust.auth_service.service;

import com.ust.auth_service.dto.JwtToken;
import com.ust.auth_service.dto.UserCredentials;
import com.ust.auth_service.model.UserModel;
import com.ust.auth_service.repository.UserRepository;
import com.ust.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public JwtToken loginUser(UserCredentials userCredentials){

        UserModel userModel = userRepository.findByUsername(userCredentials.username())
                .orElseThrow(()-> new UsernameNotFoundException("User with name " + userCredentials.username() + " not found"));


        if(!passwordEncoder.matches(userCredentials.password(), userModel.getPassword())){
            throw new RuntimeException("Password doesn't match");
        }

        String jwt = jwtUtil.generateToken(userCredentials.username());
        return new JwtToken(jwt);
    }

}
