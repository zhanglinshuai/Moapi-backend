package com.mo.moapibackend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mo.moapibackend.service.InterfaceInfoService;
import com.mo.moapicommon.model.entity.InterfaceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MoapiBackendApplicationTests {

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @Test
    void contextLoads() {
        Page<InterfaceInfo> page = new Page<>(2,3);
        System.out.println(page);
    }

}
