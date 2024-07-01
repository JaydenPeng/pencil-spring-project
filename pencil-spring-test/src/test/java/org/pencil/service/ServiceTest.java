package org.pencil.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pencil.test.TestApplication;
import org.pencil.test.beans.resp.BiliNewsResp;
import org.pencil.test.feign.BiliFeignClient;
import org.pencil.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

/**
 * @author pencil
 * @Date 24/07/01
 */
@SpringBootTest(classes = TestApplication.class)
public class ServiceTest {

    @MockBean
    private BiliFeignClient mockBiliFeignClient;

    /**
     * 在同一个test类中,
     * 不能既有Autowired的bean,又有MockBean的bean, 否则会报错
     */
    @Autowired
    private UserService userService;

    @Test
    @DirtiesContext
    void getService() {

        BiliNewsResp resp = BiliNewsResp.builder().code(0).msg("success").time("20240601").data(null).build();

        Mockito.when(mockBiliFeignClient.getNewsList()).thenReturn(resp);

        BiliNewsResp biliNews = userService.getBiliNews("20240701");

        System.out.println(biliNews);

        // 使用Junit 提供的断言校验结果
        Assertions.assertEquals(0, biliNews.getCode());
        Assertions.assertEquals("success", biliNews.getMsg());
        Assertions.assertEquals("20240701", biliNews.getTime());
    }
}
