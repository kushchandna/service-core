package com.kush.servicegen.javapoet;

import java.util.List;
import java.util.Set;

import com.kush.lib.service.server.api.BaseService;

public class DummyService extends BaseService {

    public void aVoidMethodWithNoParams() {
    }

    public int bIntMethodWithTwoPrimitiveParams(int param1, double param2) {
        return 0;
    }

    public String cStringMethodWithTwoNonPrimitiveParams(Integer param1, Double param2) {
        return null;
    }

    public int[] dIntArrayMethodWithTwoArrayParams(int[] param1, Double[] param2) {
        return null;
    }

    public List<String> eStringListMethodWithTwoGenericParams(List<Integer> param1, Set<Double> param2) {
        return null;
    }
}
