package com.zee.urlShortner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UrlDto {
    private String originalUrl;
    private String expirationDate; // This is the optional input
}
