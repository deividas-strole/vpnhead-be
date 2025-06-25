package com.vpnhead.api.controller;

import com.vpnhead.api.dto.ArticleDto;
import com.vpnhead.api.model.Article;
import com.vpnhead.api.repository.ArticleRepository;
import com.vpnhead.api.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final ArticleService service;
    private final ArticleRepository articleRepository;

    public AdminController(ArticleService service, ArticleRepository articleRepository) {
        this.service = service;
        this.articleRepository = articleRepository;
    }

    // GET endpoint for login check — returns 200 OK if authenticated
    @GetMapping
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok("Admin authenticated");
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody ArticleDto articleDto) {
        Article saved = service.createArticle(articleDto);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/save-article")
    public ResponseEntity<Void> saveArticle(@RequestBody ArticleDto dto) {
        if (dto.getTitle() == null || dto.getContent() == null) {
            return ResponseEntity.badRequest().build();
        }

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setAuthor("Deividas"); // static for now

        articleRepository.save(article);
        return ResponseEntity.ok().build();
    }


}
