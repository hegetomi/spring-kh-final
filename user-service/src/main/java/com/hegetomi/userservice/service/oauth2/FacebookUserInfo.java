package com.hegetomi.userservice.service.oauth2;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacebookUserInfo {

    private String id;
    private String name;
    private String email;
    private String imageUrl;

}