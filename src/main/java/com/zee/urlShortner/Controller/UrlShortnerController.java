package com.zee.urlShortner.Controller;

import com.zee.urlShortner.model.ErrorResponseDto;
import com.zee.urlShortner.model.ResponseDto;
import com.zee.urlShortner.model.Url;
import com.zee.urlShortner.model.UrlDto;
import com.zee.urlShortner.service.UrlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;


@RestController
public class UrlShortnerController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/create")
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        Url generatedUrlDto = urlService.generateShortLink(urlDto);
//        System.out.println(generatedUrlDto);
        if(generatedUrlDto != null){
            ResponseDto responseDto = new ResponseDto();
            responseDto.setTrueUrl(generatedUrlDto.getTrueUrl());
            responseDto.setShortLink(generatedUrlDto.getShortLink());
            responseDto.setExpiryDate(generatedUrlDto.getExpireAt());
            return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
        }

        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus("404");
        errorResponseDto.setError("There was an error processing the request. Please check again later.");
        return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.OK);
    }


    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToActualUrl(@PathVariable String shortLink) {

        String message;
        if(StringUtils.isEmpty(shortLink)) {
            message = "Invalid Url passed.";
            return errorBodyGenerator("400", message);
        }
        Url redirectUrl = urlService.getEncodedUrl(shortLink);
        if (redirectUrl == null) {
            message = "ShortLink for the Url does not exists or might have expired.";
            return errorBodyGenerator("404", message);
        }
        if(redirectUrl.getExpireAt().isBefore(LocalDateTime.now())) {
            urlService.deleteShortLink(redirectUrl);
            message = "The short link for the URL has been expired. Please renew it.";
            return errorBodyGenerator("200", message);
        }
        String redirectToUrl = redirectUrl.getTrueUrl();
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectToUrl)).build();
    }

    public ResponseEntity<?> errorBodyGenerator(String status, String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setStatus(status);
        errorResponseDto.setError(message);
        return new ResponseEntity<ErrorResponseDto>(errorResponseDto, HttpStatus.OK);
    }

}
