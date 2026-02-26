package com.bjdj.facade;

import com.bjdj.dto.req.TestReq;
import com.bjdj.dto.res.TestRes;
import com.bjdj.service.TestDemoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class TestServiceAFacadeImpl implements TestServiceAFacade {

    @Autowired
    private TestDemoService testDemoService;

    @Override
    public TestRes methodTest(TestReq param) {
        return testDemoService.methodTest(param);
    }

    @Override
    public String methodTest2(String param) {
        return "";
    }
}
