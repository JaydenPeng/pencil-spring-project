package org.pencil.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author pencil
 * @Date 24/06/29
 */
@SpringBootApplication(scanBasePackages = "org.pencil")
@EnableFeignClients(basePackages = "org.pencil.test.feign")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
