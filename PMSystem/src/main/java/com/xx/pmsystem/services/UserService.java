package com.xx.pmsystem.services;

import com.xx.pmsystem.domain.User;
import com.xx.pmsystem.exceptions.UsernameAlreadyExistsException;
import com.xx.pmsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){


        //user name should be unique
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");

            return userRepository.save(newUser);
        }catch(Exception e){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername()+ "' already exists!");
        }

        //make sure that password and confirmPassword match

        //we dont persist or show the confirmPassword

    }



}
