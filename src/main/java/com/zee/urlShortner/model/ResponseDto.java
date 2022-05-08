package com.zee.urlShortner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String trueUrl;
    private String shortLink;
    private LocalDateTime expiryDate;
}
