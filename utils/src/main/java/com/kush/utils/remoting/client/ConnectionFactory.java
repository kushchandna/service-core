package com.kush.utils.remoting.client;

public interface ConnectionFactory {

    Connection createConnection() throws ConnectionFailedException;
}
