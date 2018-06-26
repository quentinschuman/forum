package com.zjgsu.forum.module.user.repository;

import com.zjgsu.forum.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * ProjectName: forum
 * User: quent
 * Date: 2018/6/26
 * Time: 22:23
 */
public interface UserRepository extends JpaRepository<User,Integer> {

    User findById(int id);
    User findByUsername(String username);
    void deleteById(int id);
    User findByToken(String token);
    User findByEmail(String email);
}
