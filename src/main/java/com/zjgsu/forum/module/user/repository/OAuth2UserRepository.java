package com.zjgsu.forum.module.user.repository;

import com.zjgsu.forum.module.user.model.OAuth2User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:26
 */
public interface OAuth2UserRepository extends JpaRepository<OAuth2User,Integer> {

    OAuth2User findByOauthUserIdAndType(String oauth2UserId,String type);
}
