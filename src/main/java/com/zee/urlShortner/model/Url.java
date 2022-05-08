package com.zee.urlShortner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "url_data")
public class Url {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "true_url", columnDefinition = "text")
    private String trueUrl;
    @Column(name = "generated_short_link", unique = true)
    private String shortLink;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "expire_at")
    private LocalDateTime expireAt;

}
