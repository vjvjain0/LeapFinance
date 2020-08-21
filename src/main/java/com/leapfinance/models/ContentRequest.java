package com.leapfinance.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
public class ContentRequest {

    @NotEmpty
    private String url;
    @Setter private String username;

    public ContentRequest(String _username, String _url) {
        username = _username;
        url = _url;
    }
}
