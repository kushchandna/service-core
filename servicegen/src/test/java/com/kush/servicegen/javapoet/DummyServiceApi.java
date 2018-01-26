package com.kush.servicegen.javapoet;

import java.util.List;
import java.util.Set;

import com.kush.lib.service.remoting.api.ServiceApi;

public interface DummyServiceApi extends ServiceApi {

    void aVoidMethodWithNoParams();

    int bIntMethodWithTwoPrimitiveParams(int param1, double param2);

    String cStringMethodWithTwoNonPrimitiveParams(Integer param1, Double param2);

    int[] dIntArrayMethodWithTwoArrayParams(int[] param1, Double[] param2);

    List<String> eStringListMethodWithTwoGenericParams(List<Integer> param1, Set<Double> param2);
}
