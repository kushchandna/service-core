package com.kush.lib.service.remoting;

import java.util.HashMap;
import java.util.Map;

public class MapBasedRemoteServiceProvider implements RemoteServiceProvider {

    private final Map<String, ServiceApi> nameVsApi;

    public MapBasedRemoteServiceProvider(Map<String, ServiceApi> nameVsApi) {
        this.nameVsApi = new HashMap<>(nameVsApi);
    }

    @Override
    public ServiceApi getRemoteService(String serviceName) {
        return nameVsApi.get(serviceName);
    }
}
