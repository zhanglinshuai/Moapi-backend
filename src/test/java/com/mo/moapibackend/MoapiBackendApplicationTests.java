package com.mo.moapibackend;

import com.mo.moapisdk.client.MoapiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MoapiBackendApplicationTests {


    @Resource
    private MoapiClient moapiClient;


    @Test
    void contextLoads() {
        String userNamePost = moapiClient.getUserNamePost("zhang");
        System.out.println(userNamePost);
    }

}
