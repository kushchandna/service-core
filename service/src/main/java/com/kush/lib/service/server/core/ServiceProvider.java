package com.kush.lib.service.server.core;

import com.kush.lib.service.server.api.BaseService;

interface ServiceProvider {

    BaseService getService(String serviceName);
}
