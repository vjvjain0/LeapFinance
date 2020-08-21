package com.leapfinance.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String url;
    private String content;

    public Content(String _username, String _url,String _content) {
        username = _username;
        url = _url;
        content = _content;
    }
}
