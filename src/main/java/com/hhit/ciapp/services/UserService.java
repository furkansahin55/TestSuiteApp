package com.hhit.ciapp.services;

import com.hhit.ciapp.models.User;
import com.hhit.ciapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        //First encode the the password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //Then persist
        userRepository.save(user);
    }

    public Boolean checkLogin(String email, String password){

        //Create an user object with find by email
        User userDatabase = findUserByEmail(email);

        //Check if they are equal and return Bolean
        return userDatabase != null &&
                userDatabase.getEmail().equals(email) &&
                bCryptPasswordEncoder.matches(password, userDatabase.getPassword());

    }




}
