package com.vpnhead.api.controller;

import com.vpnhead.api.dto.ArticleDto;
import com.vpnhead.api.model.Article;
import com.vpnhead.api.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final ArticleService service;

    public AdminController(ArticleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody ArticleDto articleDto) {
        Article saved = service.createArticle(articleDto);
        return ResponseEntity.ok(saved);
    }
}
