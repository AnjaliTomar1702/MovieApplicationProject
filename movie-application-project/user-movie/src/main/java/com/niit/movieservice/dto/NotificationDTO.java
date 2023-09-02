package com.niit.movieservice.dto;

import com.niit.movieservice.model.Movie;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationDTO {
    private String emailId;
    private Movie movie;
}
