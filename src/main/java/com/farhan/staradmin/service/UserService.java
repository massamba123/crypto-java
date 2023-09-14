package com.farhan.staradmin.service;

import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User login(String username,String password){
        User user = this.userRepository.findByUsername(username).orElse(null);
        if (null != user && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
    public void createUser(User user){
        this.userRepository.save(user);
    }
    public List<User> getAllUser(){
        return this.userRepository.findAll();
    }
    public User getUser(int id){
        return this.userRepository.findById(id).orElse(null);
    }
    public void deleteUser(int id){
        this.userRepository.deleteById(id);
    }
}
