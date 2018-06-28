package com.zjgsu.forum.module.code.repository;

import com.zjgsu.forum.module.code.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by qianshu on 2018/6/28.
 */
@Repository
public interface CodeRepository extends JpaRepository<Code,Integer> {

    List<Code> findByEmailAndCodeAndType(String email,String code,String type);
}
