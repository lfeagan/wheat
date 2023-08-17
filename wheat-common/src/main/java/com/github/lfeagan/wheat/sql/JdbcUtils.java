package com.github.lfeagan.wheat.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public final class JdbcUtils {

    /**
     * Closes the specified non-null connection and suppresses any thrown exceptions.
     * @param connection the connection to close, which can be <code>null</code>
     */
    public static void closeWithoutException(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    /**
     * Closes the specified non-null statement and suppresses any thrown exceptions.
     * @param statement the statement to close, which can be <code>null</code>
     */
    public static void closeWithoutException(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    /**
     * Closes the specified non-null result set and suppresses any thrown exceptions.
     * @param rs the result set to close, which can be <code>null</code>
     */
    public static void closeWithoutException(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
