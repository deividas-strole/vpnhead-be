package com.vpnhead.api.controller;

import com.vpnhead.api.dto.ArticleTitleDTO;
import com.vpnhead.api.model.Article;
import com.vpnhead.api.service.ArticleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.vpnhead.api.dto.ArticleDto;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/api/articles/titles")
    public List<ArticleTitleDTO> getArticleTitles() {
        return service.getAllArticleTitles();
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return service.getArticle(id);
    }

    @PostMapping("/api/articles")
    public ResponseEntity<Article> saveArticle(@RequestBody ArticleDto articleDto) {
        Article article = service.createArticle(articleDto);
        return ResponseEntity.ok(article);
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        boolean deleted = service.deleteArticle(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
