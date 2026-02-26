package com.bjdj.facade;

import com.bjdj.dto.req.TestReq;
import com.bjdj.dto.res.TestRes;

/**
 * @author: yulong.zhang
 * @date: 2025/4/24
 * @description:
 */

public interface TestServiceAFacade {

    TestRes methodTest(TestReq param);

    String methodTest2(String param);
}
