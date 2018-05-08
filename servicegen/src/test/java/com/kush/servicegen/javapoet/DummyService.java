package com.kush.servicegen.javapoet;

import java.util.List;
import java.util.Set;

import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service
public class DummyService extends BaseService {

    @ServiceMethod(name = "A Void Method With No Params")
    public void aVoidMethodWithNoParams() {
    }

    @ServiceMethod(name = "B Int Method With Two Primitive Params")
    public int bIntMethodWithTwoPrimitiveParams(int param1, double param2) {
        return 0;
    }

    @ServiceMethod(name = "C String Method With Two Non Primitive Params")
    public String cStringMethodWithTwoNonPrimitiveParams(Integer param1, Double param2) {
        return null;
    }

    @ServiceMethod(name = "D Int Array Method With Two Array Params")
    public int[] dIntArrayMethodWithTwoArrayParams(int[] param1, Double[] param2) {
        return null;
    }

    @ServiceMethod(name = "E String List Method With Two Generic Params")
    public List<String> eStringListMethodWithTwoGenericParams(List<Integer> param1, Set<Double> param2) {
        return null;
    }

    @AuthenticationRequired
    @ServiceMethod(name = "F Void Method With Authentication")
    public void fVoidMethodWithAuthentication() {
    }

    @AuthenticationRequired
    @ServiceMethod(name = "G Int Method With Authentication")
    public int gIntMethodWithAuthentication(int param1, double param2) {
        return 0;
    }
}
