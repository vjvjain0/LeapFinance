package com.leapfinance.controllers;

import com.leapfinance.exceptions.BadCredentialsException;
import com.leapfinance.models.Content;
import com.leapfinance.models.ContentRequest;
import com.leapfinance.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/saveContent")
    public CompletableFuture<Content> saveContent(@RequestBody ContentRequest contentRequest, HttpServletRequest request) {
        contentRequest.setUsername(request.getHeader("username"));
        return contentService.saveContent(contentRequest);
    }

    @GetMapping("/getContents")
    public List<Content> getContents(HttpServletRequest request) {
        return contentService.getContents(request.getHeader("username"));
    }
}
