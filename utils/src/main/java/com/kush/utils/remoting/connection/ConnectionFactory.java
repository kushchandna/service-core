package com.kush.utils.remoting.connection;

public interface ConnectionFactory {

    Connection createConnection() throws ConnectionFailedException;
}
