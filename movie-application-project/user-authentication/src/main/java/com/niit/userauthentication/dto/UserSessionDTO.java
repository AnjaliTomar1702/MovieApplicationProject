package com.niit.userauthentication.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDTO {
    private String emailId;
    private String sessionId;
}
