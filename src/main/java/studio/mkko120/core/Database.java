package studio.mkko120.coffeeutil.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class Database {
    @Data
    public static class DatabaseCredentials {
        private final String username;
        private final String password;
        private final String hostname;
        private final Integer port;
        private final String database;

        /**
         * Represents a connection string for a database
         * @return a connection string for the database
         */
        @Override
        public String toString() {
            return "jdbc:mariadb://" + username + ":" + password + "@" + hostname + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        }
    }
    private final static String connectionString = "jdbc:mariadb://root@localhost:3306/minecraft?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private static HikariDataSource hikariDS;

    public Database() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(connectionString);

            hikariDS = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Database(DatabaseCredentials credentials) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(credentials.toString());
            hikariDS = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Executes query
     * @param query query to execute
     * @param values optional values to set
     * @return true if query was executed successfully, false otherwise
     */
    public boolean execute(String query, Object @NotNull ... values) {
        try (Connection connection = hikariDS.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                if (values.length > 0) {
                    for (int i = 0; i < values.length; i++) {
                        statement.setObject(i + 1, values[i]);
                    }
                }
                return statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Executes query and returns result
     * @param query query to execute
     * @param values optional values to set
     * @return result of query
     */
    public ResultSet query(String query, Object... values) {
        try (Connection connection = hikariDS.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                if (values.length > 0) {
                    for (int i = 0; i < values.length; i++) {
                        stmt.setObject(i + 1, values[i]);
                    }
                }
                return stmt.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes update and returns result
     * @param query query to execute
     * @param values optional values to set
     * @return result of update
     */
    public int update(String query, Object... values) {
        try (Connection connection = hikariDS.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                if (values.length > 0) {
                    for (int i = 0; i < values.length; i++) {
                        stmt.setObject(i + 1, values[i]);
                    }
                }
                return stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
