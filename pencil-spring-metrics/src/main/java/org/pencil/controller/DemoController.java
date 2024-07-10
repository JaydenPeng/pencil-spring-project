package org.pencil.controller;

import io.prometheus.client.Counter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pencil
 * @Date 24/07/10
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Resource
    private Counter requestCounter;

    @GetMapping("hello")
    public String hello() {
        requestCounter.labels("/demo/hello", "success").inc();
        return "hello";
    }
}
