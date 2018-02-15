package com.kush.utils.pooling;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

class PooledConnection implements Connection {

    private final Connection connection;

    private boolean closed = false;

    public PooledConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.isWrapperFor(iface);
    }

    @Override
    public Statement createStatement() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        throwSQLExceptionIfClosed();
        connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        throwSQLExceptionIfClosed();
        connection.rollback();
    }

    @Override
    public void close() throws SQLException {
        closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        throwSQLExceptionIfClosed();
        connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throwSQLClientInfoExceptionIfClosed();
        connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throwSQLClientInfoExceptionIfClosed();
        connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throwSQLExceptionIfClosed();
        connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throwSQLExceptionIfClosed();
        return connection.getNetworkTimeout();
    }

    private void throwSQLExceptionIfClosed() throws SQLException {
        if (closed) {
            throw new SQLException("Connection already closed");
        }
    }

    private void throwSQLClientInfoExceptionIfClosed() throws SQLClientInfoException {
        try {
            throwSQLExceptionIfClosed();
        } catch (SQLException e) {
            throw new SQLClientInfoException(e.getMessage(), Collections.emptyMap());
        }
    }
}
