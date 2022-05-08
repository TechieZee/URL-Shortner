package com.zee.urlShortner.repository;

import com.zee.urlShortner.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    public Url getByShortLink(String shortLink);
}
