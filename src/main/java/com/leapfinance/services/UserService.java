package com.leapfinance.services;

import com.leapfinance.repositories.UserRepository;
import com.leapfinance.exceptions.BadCredentialsException;
import com.leapfinance.models.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User newUser) {
        String username = newUser.getUsername().toLowerCase();
        validatePassword(newUser.getPassword());
        String password = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt(10));
        if(userRepository.countByUsername(username) > 0) {
            throw new BadCredentialsException("username is already taken");
        }
        User user = new User(username, password);
        user = userRepository.save(user);
        user.setPassword(null);
        return user;
    }

    private static void validatePassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if(!m.matches()) {
            throw new BadCredentialsException("should be at least 8 characters long with at least one alphabetic and a numeric character");
        }
    }

    public User login(User user) {
        String username = user.getUsername().toLowerCase();
        String password = user.getPassword();

        User user1 = userRepository.findByUsername(username);
        boolean equal = user1 != null && BCrypt.checkpw(password, user1.getPassword());
        if(!equal) {
            throw new BadCredentialsException("username and password does not match");
        }

        user1.setPassword(null);

        return user1;
    }
}
