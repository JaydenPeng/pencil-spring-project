package org.pencil.test.controller;

import org.pencil.anno.ReqStatistics;
import org.pencil.beans.resp.Result;
import org.pencil.test.beans.resp.BiliNewsResp;
import org.pencil.test.beans.resp.dto.UserDto;
import org.pencil.test.feign.BiliFeignClient;
import org.pencil.test.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author pencil
 * @Date 24/06/29
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Resource
    private BiliFeignClient biliFeignClient;

    @Resource
    private UserService userService;

    @GetMapping("index")
    public Result<BiliNewsResp> index() {
        BiliNewsResp newsList = biliFeignClient.getNewsList();
        return Result.success(newsList);
    }


    @GetMapping("test")
    @ReqStatistics(value = "请求数据", req = true, resp = true, costTime = true)
    public Result<String> test(@RequestParam("name") String name) {
        return Result.success(name + "ok");
    }

    @GetMapping("mock")
    public Result<UserDto> getUserDto(@RequestParam("id") String id) {
        return Result.success(userService.getUserById(id));
    }

}
