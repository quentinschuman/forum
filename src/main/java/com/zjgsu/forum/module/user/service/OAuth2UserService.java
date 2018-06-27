package com.zjgsu.forum.module.user.service;

import com.zjgsu.forum.module.user.model.OAuth2User;
import com.zjgsu.forum.module.user.repository.OAuth2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/27
 * Time: 21:01
 */
@Service
@Transactional
public class OAuth2UserService {
    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    public OAuth2User save(OAuth2User oAuth2User) {
        return oAuth2UserRepository.save(oAuth2User);
    }

    public OAuth2User createOAuth2User(String nickName,String avatar,Integer userId,String oauth2UserId,String accessToken,String type){
        OAuth2User oAuth2User = new OAuth2User();
        oAuth2User.setNickName(nickName);
        oAuth2User.setUserId(userId);
        oAuth2User.setType(type);
        oAuth2User.setInTime(new Date());
        oAuth2User.setOauthUserId(oauth2UserId);
        oAuth2User.setAccessToken(accessToken);
        oAuth2User.setAvatar(avatar);
        return this.save(oAuth2User);
    }

    public OAuth2User findByOAuthUserIdAndType(String oauthUserId,String type){
        return oAuth2UserRepository.findByOauthUserIdAndType(oauthUserId, type);
    }
}
