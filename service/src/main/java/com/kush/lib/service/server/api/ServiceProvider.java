package com.kush.lib.service.server.api;

interface ServiceProvider {

    BaseService getService(String serviceName);
}
