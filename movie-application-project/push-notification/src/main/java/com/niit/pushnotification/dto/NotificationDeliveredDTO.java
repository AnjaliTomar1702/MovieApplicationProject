package com.niit.pushnotification.dto;

import com.niit.pushnotification.domain.Movie;
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
