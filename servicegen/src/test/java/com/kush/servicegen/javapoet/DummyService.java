package com.kush.servicegen.javapoet;

import java.util.List;
import java.util.Set;

import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.AuthenticationRequired;

@Service
public class DummyService extends BaseService {

    @ServiceMethod
    public void aVoidMethodWithNoParams() {
    }

    @ServiceMethod
    public int bIntMethodWithTwoPrimitiveParams(int param1, double param2) {
        return 0;
    }

    @ServiceMethod
    public String cStringMethodWithTwoNonPrimitiveParams(Integer param1, Double param2) {
        return null;
    }

    @ServiceMethod
    public int[] dIntArrayMethodWithTwoArrayParams(int[] param1, Double[] param2) {
        return null;
    }

    @ServiceMethod
    public List<String> eStringListMethodWithTwoGenericParams(List<Integer> param1, Set<Double> param2) {
        return null;
    }

    @AuthenticationRequired
    @ServiceMethod
    public void fVoidMethodWithAuthentication() {
    }

    @AuthenticationRequired
    @ServiceMethod
    public int gIntMethodWithAuthentication(int param1, double param2) {
        return 0;
    }
}
