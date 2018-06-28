package com.zjgsu.forum.module.code.service;

import com.zjgsu.forum.core.util.DateUtil;
import com.zjgsu.forum.core.util.StrUtil;
import com.zjgsu.forum.module.code.model.Code;
import com.zjgsu.forum.module.code.model.CodeEnum;
import com.zjgsu.forum.module.code.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by qianshu on 2018/6/28.
 */
@Service
@Transactional
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    public Code findByEmailAndCodeAndType(String email, String code, CodeEnum type){
        List<Code> codes = codeRepository.findByEmailAndCodeAndType(email,code,type.name());
        if (codes.size() > 0)
            return codes.get(0);
        return null;
    }

    public void save(Code code){
        codeRepository.save(code);
    }

    public String genEmailCode(String email){
        String genCode = StrUtil.randomString();
        Code code = findByEmailAndCodeAndType(email,genCode,CodeEnum.EMAIL);
        if (code != null){
            return genEmailCode(email);
        }else {
            code = new Code();
            code.setCode(genCode);
            code.setExpireTime(DateUtil.getMinuteAfter(new Date(),10));
            code.setType(CodeEnum.EMAIL.name());
            code.setEmail(email);
            code.setIsUsed(false);
            save(code);
            return genCode;
        }
    }

    public int validateCode(String email,String code,CodeEnum type){
        Code code1 = findByEmailAndCodeAndType(email,code,type);
        if (code1 == null)
            return 1;
        if (DateUtil.isExpire(code1.getExpireTime()))
            return 2;
        if (code1.getIsUsed())
            return 3;
        code1.setIsUsed(true);
        save(code1);
        return 0;
    }
}
