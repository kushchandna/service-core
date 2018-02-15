package com.kush.utils.pooling;

import java.sql.Connection;

public interface ConnectionFactory {

    Connection create();
}
