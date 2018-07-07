package com.zjgsu.forum.web.api;

import com.zjgsu.forum.core.base.BaseController;
import com.zjgsu.forum.core.bean.Result;
import com.zjgsu.forum.module.es.service.TagSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianshu on 2018/7/7.
 */
@RestController
@RequestMapping("/api/tag")
public class TagApiController extends BaseController {

    @Autowired
    private TagSearchService tagSearchService;

    @GetMapping("/autocomplete")
    public Result autocomplete(String keyword){
        return Result.success(tagSearchService.query(keyword,7));
    }

}
