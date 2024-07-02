package org.pencil.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pencil.test.TestApplication;
import org.pencil.test.beans.resp.dto.UserDto;
import org.pencil.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author pencil
 * @Date 24/07/01
 */
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    /**
     * 测试 mock controller方法
     * @throws Exception
     */
    @Test
    @DirtiesContext
    void getController() throws Exception {
        UserDto wangWu = UserDto.builder().id("3").name("wangwu").age(30).phone("13111111111").build();

        // mock UserService 的 getUserById 方法
        // 当调用 UserService.getUserById("3") 时，返回 wangWu
        Mockito.when(mockUserService.getUserById("3")).thenReturn(wangWu);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/demo/mock?id=3"));

        // 断言返回的状态码为 200
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());

        resultActions.andDo(result -> System.out.println("请求结果:" + result.getResponse().getContentAsString()));

        // 断言返回的内容为 "{\"id\":\"3\",\"name\":\"wangwu\",\"age\":30,\"phone\":\"13111111111\"}"
        resultActions.andExpect(MockMvcResultMatchers.content().json("{\"code\":0,\"msg\":\"success\",\"data\":{\"id\":\"3\",\"name\":\"wangwu\",\"age\":30,\"phone\":\"13111111111\"}}", true));

        // 断言返回的内容类型为 application/json
        resultActions.andExpect(MockMvcResultMatchers.content().contentType("application/json"));

        // 校验响应内容,name是否是wangwu
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("wangwu"));

    }

}
