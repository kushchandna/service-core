package com.kush.utils.async.remoting.connection;

public interface ConnectionFactory {

    Connection createConnection() throws ConnectionFailedException;
}
