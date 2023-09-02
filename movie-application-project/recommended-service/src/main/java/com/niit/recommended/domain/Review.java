package com.niit.recommended.domain;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String id;
    private String author;
    private String content;
    private String createdAt;
}
