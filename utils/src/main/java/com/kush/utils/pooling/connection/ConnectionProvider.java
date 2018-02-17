package com.kush.utils.pooling.connection;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection getConnection() throws InterruptedException;
}
