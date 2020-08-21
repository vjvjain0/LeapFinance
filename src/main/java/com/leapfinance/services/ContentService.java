package com.leapfinance.services;

import com.leapfinance.exceptions.BadCredentialsException;
import com.leapfinance.models.Content;
import com.leapfinance.models.ContentRequest;
import com.leapfinance.repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public CompletableFuture<Content> saveContent(ContentRequest contentRequest) {
        String url = contentRequest.getUrl();
        String username = contentRequest.getUsername();
        RestTemplate restTemplate = new RestTemplate();
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            try {
                return restTemplate.getForObject(url, String.class);
            }
            catch (IllegalArgumentException ex) {
                throw new BadCredentialsException("url provided is not valid");
            }
            catch (Exception ex) {
                throw ex;
            }
        });

        return cf.thenApply(content -> {
            Content c = new Content(username, url, content);
            return contentRepository.save(c);
        });
    }

    public List<Content> getContents(String username) {
        return contentRepository.findAllByUsername(username);
    }
}
