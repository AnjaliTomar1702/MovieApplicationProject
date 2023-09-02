package com.niit.userauthentication.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserRoleDTO {
    private String email;
    private String roleName;
}
