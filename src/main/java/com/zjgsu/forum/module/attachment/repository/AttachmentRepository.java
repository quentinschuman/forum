package com.zjgsu.forum.module.attachment.repository;

import com.zjgsu.forum.module.attachment.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by qianshu on 2018/6/28.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Integer> {

    Attachment findByMd5(String md5);
}
