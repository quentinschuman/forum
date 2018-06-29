package com.zjgsu.forum.module.log.repository;

import com.zjgsu.forum.module.log.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by qianshu on 2018/6/29.
 */
@Repository
public interface LogRepository extends JpaRepository<Log,Integer> {

    Page<Log> findByUserId(Integer userId, Pageable pageable);
    void deleteByUserId(Integer userId);
    @Query(value = "select l as log, u as user from Log l, User u where l.userId = u.id",
            countQuery = "select count(1) from Log l, User u where l.userId = u.id")
    Page<Map> findAllForAdmin(Pageable pageable);
}
