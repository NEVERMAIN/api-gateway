package io.github.NEVERMAIN.gateway.rpc;

import io.github.NEVERMAIN.gateway.rpc.dto.XReq;

public interface IActivityBooth {

    String sayHi(String str);

    String insert(XReq req);

    String test(String str, XReq req);


}
