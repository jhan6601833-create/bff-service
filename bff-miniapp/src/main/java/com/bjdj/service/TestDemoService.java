package com.bjdj.service;

import com.bjdj.dto.req.TestReq;
import com.bjdj.dto.res.TestRes;
import com.bjdj.entity.ShopGroupInfoEntity;
import com.bjdj.mapper.ShopGroupInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestDemoService {

    @Autowired
    private ShopGroupInfoMapper shopGroupInfoMapper;

    public TestRes methodTest(TestReq param) {
        int insert = shopGroupInfoMapper.insert(new ShopGroupInfoEntity().setAddress("232323"));

        TestRes testRes = new TestRes();
        testRes.setResult(String.valueOf(insert));
        return testRes;
    }
}
