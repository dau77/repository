package com.equator.config.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.equator.model.MUser;
import com.equator.service.muser.MUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService { //自定义UserDetailsService 接口
    @Autowired
    MUserService mUserService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return mUserService.getOne(new QueryWrapper<MUser>().eq("username", username));
    }

}
