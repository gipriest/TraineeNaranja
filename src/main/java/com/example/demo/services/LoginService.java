package com.example.demo.services;

import com.example.demo.exceptions.RepositoryException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.UsersEntity;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    private UserRepository userRepo;


    @Autowired
    public LoginService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    public Optional<UsersEntity> getUserByCredentials(String user, String password) {
        log.debug("Login Service: Get user id by credentials");
        try{
            Optional<UsersEntity> usersEntity = userRepo.getUser(user);

            if(usersEntity.isEmpty()){
                throw new UserNotFoundException();
            }

            if (!password.equals(usersEntity.get().getPassword())) {
                throw new WrongPasswordException();
            }

            return usersEntity;

        }catch(UserNotFoundException e){
            log.error("Login Service: User not found (getIdByCredentials)");
            throw new UserNotFoundException();
        }catch(WrongPasswordException e){
            log.error("Login Service: Wrong password (getIdByCredentials)");
            throw new WrongPasswordException();
        }catch(RuntimeException e){
            log.error("Login Service: Error from repository (getIdByCredentials)");
            throw new RepositoryException(e.getMessage(), e);
        }
    }



}
