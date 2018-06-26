package com.zjgsu.forum.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by qianshu on 2018/6/26.
 */
@Getter
@Setter
public class OAuth2 {

    private String clientId;
    private String clientSecret;
    private String callbackUrl;
}
