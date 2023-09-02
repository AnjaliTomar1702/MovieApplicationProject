package com.niit.movieservice.dto;

import com.niit.movieservice.model.Movie;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDeliveredDTO {
    private String emailId;

    private Movie movie;
}
