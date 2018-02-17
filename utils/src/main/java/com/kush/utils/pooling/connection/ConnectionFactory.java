package com.kush.utils.pooling.connection;

import java.sql.Connection;

public interface ConnectionFactory {

    Connection create();
}
