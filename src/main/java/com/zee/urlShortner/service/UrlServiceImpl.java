package com.zee.urlShortner.service;

import com.google.common.hash.Hashing;
import com.zee.urlShortner.model.Url;
import com.zee.urlShortner.model.UrlDto;
import com.zee.urlShortner.repository.UrlRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {

        if(StringUtils.isNotEmpty(urlDto.getOriginalUrl())) {

            String encodedUrl = hashedUrl(urlDto.getOriginalUrl());
            Url urlToPersist = new Url();
            urlToPersist.setCreatedAt(LocalDateTime.now());
            urlToPersist.setTrueUrl(urlDto.getOriginalUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpireAt(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreatedAt()));
            Url urlResponse = persistShortLink(urlToPersist);
            if(urlResponse != null)
                return urlResponse;
        }
        return null;
    }

    /* Calculates the expiration date for the short url, based on the user input
    else returns the default 30 days expiration. */
    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime createdAt) {

        if(StringUtils.isBlank(expirationDate)) {
            return createdAt.plusDays(30);
        }
        LocalDateTime expireAt = LocalDateTime.parse(expirationDate);
        return expireAt;
    }


    /* Encodes the URL into a hash using murmur3 hashing algorithm and returns the hashed URL. */
    private String hashedUrl(String originalUrl) {
        String hashedUrl = "";
        LocalDateTime time = LocalDateTime.now();
        hashedUrl = Hashing.murmur3_32_fixed()
                .hashString(originalUrl.concat(time.toString()), StandardCharsets.UTF_8)
                .toString();
        return hashedUrl;
    }

    /* Saves the url object to the database. */
    @Override
    public Url persistShortLink(Url url) {
        Url urlResp = urlRepository.save(url);
        return urlResp;
    }

    /* Gets the URL data from database using the short link. */
    @Override
    public Url getEncodedUrl(String shortLink) {
        Url urlResp = urlRepository.getByShortLink(shortLink);
        return urlResp;
    }

    /* Deletes URL data from the database */
    @Override
    public void deleteShortLink(Url url) {

        urlRepository.delete(url);
    }
}
