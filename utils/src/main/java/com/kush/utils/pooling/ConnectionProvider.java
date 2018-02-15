package com.kush.utils.pooling;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection getConnection() throws InterruptedException;
}
