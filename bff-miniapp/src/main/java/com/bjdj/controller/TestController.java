package com.bjdj.controller;

import com.bjdj.dto.req.TestReq;
import com.bjdj.dto.res.TestRes;
import com.bjdj.facade.TestServiceAFacade;
import com.bjdj.api.Result;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource
    private TestServiceAFacade testServiceA;

    @GetMapping("/test")
    public Result<TestRes> test() {
        TestReq testReq = new TestReq();
        TestRes testRes = testServiceA.methodTest(testReq);
        return Result.ok(testRes);
    }
}
