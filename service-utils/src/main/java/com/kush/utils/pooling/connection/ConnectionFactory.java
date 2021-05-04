package com.kush.utils.pooling.connection;

import java.sql.Connection;

interface ConnectionFactory {

    Connection create();
}
