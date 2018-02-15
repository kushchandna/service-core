package com.kush.utils.pooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FixedSizeConnectionPool implements ConnectionProvider, AutoCloseable {

    private final BlockingQueue<Connection> availableConnections;

    public FixedSizeConnectionPool(ConnectionFactory connectionFactory, int size) {
        availableConnections = new ArrayBlockingQueue<>(size);
        initializeConnections(connectionFactory, size);
    }

    @Override
    public Connection getConnection() throws InterruptedException {
        Connection connection = availableConnections.take();
        return new PooledConnection(connection) {

            @Override
            public void close() throws SQLException {
                super.close();
                availableConnections.add(connection);
            }
        };
    }

    @Override
    public void close() throws Exception {
        for (Iterator<Connection> iterator = availableConnections.iterator(); iterator.hasNext();) {
            Connection connection = iterator.next();
            closeQuietly(connection);
            iterator.remove();
        }
    }

    private void initializeConnections(ConnectionFactory connectionFactory, int size) {
        for (int i = 0; i < size; i++) {
            Connection connection = connectionFactory.create();
            availableConnections.add(connection);
        }
    }

    private void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }
}
