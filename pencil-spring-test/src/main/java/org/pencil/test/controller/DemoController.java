package org.pencil.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.pencil.anno.ReqStatistics;
import org.pencil.entity.resp.Result;
import org.pencil.context.RequestContext;
import org.pencil.test.entity.dto.UserDto;
import org.pencil.test.entity.resp.BiliNewsResp;
import org.pencil.test.feign.BiliFeignClient;
import org.pencil.test.feign.LocalFeignClient;
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
@Slf4j
@RestController
@RequestMapping("demo")
public class DemoController {

    @Resource
    private BiliFeignClient biliFeignClient;

    @Resource
    private UserService userService;

    @Resource
    private LocalFeignClient localFeignClient;


    @GetMapping("index")
    public Result<BiliNewsResp> index() {
        BiliNewsResp newsList = biliFeignClient.getNewsList();
        return Result.success(newsList);
    }

    @GetMapping("test/feign")
    public Result<UserDto> getUserDto() {
        return localFeignClient.getUserDto("1");
    }

    @GetMapping("test")
    @ReqStatistics(value = "请求数据", req = true, resp = true, costTime = true)
    public Result<String> test(@RequestParam("name") String name) {
        String requestIp = RequestContext.get().getRequestIp();
        log.info("requestIp:{}", requestIp);
        return Result.success(name + "ok");
    }

    @GetMapping("mock")
    public Result<UserDto> getUserDto(@RequestParam("id") String id) {
        return Result.success(userService.getUserById(id));
    }



}
