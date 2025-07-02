package com.vpnhead.api.service;

import com.vpnhead.api.dto.ArticleDto;
import com.vpnhead.api.dto.ArticleTitleDTO;
import com.vpnhead.api.model.Article;
import com.vpnhead.api.repository.ArticleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public List<ArticleTitleDTO> getAllArticleTitles() {
        List<Article> articles = repository.findAll();
        return articles.stream()
                .map(a -> new ArticleTitleDTO(a.getId(), a.getTitle()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<Article> getArticle(Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public Article createArticle(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setAuthor("Admin");
        article.setImageUrl(articleDto.getImageUrl());
        return repository.save(article);
    }
}
