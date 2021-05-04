package com.kush.utils.pooling.connection;

import java.sql.Connection;

interface ConnectionProvider {

    Connection getConnection() throws InterruptedException;
}
