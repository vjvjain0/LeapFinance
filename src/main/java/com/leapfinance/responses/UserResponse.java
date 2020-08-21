package com.leapfinance.responses;

import com.leapfinance.JWTUtil;
import com.leapfinance.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String username;
    private String token;

    public static UserResponse from(User user) {
        UUID _id = user.getId();
        String _username = user.getUsername();
        String _token = JWTUtil.generateToken(user.getUsername());

        return new UserResponse(_id, _username, _token);
    }
}
