package org.pencil.test.service.impl;

import org.pencil.test.entity.dto.UserDto;
import org.pencil.test.entity.resp.BiliNewsResp;
import org.pencil.test.feign.BiliFeignClient;
import org.pencil.test.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pencil
 * @Date 24/07/01
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private BiliFeignClient biliFeignClient;

    @Override
    public UserDto getUserById(String userId) {
        return UserDto.builder().id("1").name("lisi").age(20).phone("13899999999").build();
    }

    @Override
    public UserDto getUserInfoById(String userId) {
        if ("1".equals(userId)) {
            return UserDto.builder().id("1").name("pencil").age(18).phone("13888888888").build();
        }

        return UserDto.builder().id("1").name("lisi").age(20).phone("13888888888").build();
    }

    /*
     * @param time the time
     * @return
     */
    @Override
    public BiliNewsResp getBiliNews(String time) {

        BiliNewsResp resp = biliFeignClient.getNewsList();

        resp.setTime(time);

        return resp;
    }

}
