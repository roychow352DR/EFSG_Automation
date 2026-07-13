package Data;

import utils.SQLConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static utils.BaseTest.productEnv;

public class SQLDatabase {

    private final SQLConnection sqlConnection;
    private final List<QueryShape> allowedQueries;

    /**
     * Constructs a new SQLDatabaseOptimized instance.
     * Initializes the SQL connection using the product environment configuration
     * and builds the list of allowed query patterns for security validation.
     */
    public SQLDatabase() {
        this.sqlConnection = new SQLConnection(productEnv);
        this.allowedQueries = buildAllowedQueries();
    }

    /**
     * Retrieves the count of persons with the specified person ID number.

     */
    public Optional<String> getPersonIdCount(String id) throws SQLException {
        String sql = "SELECT COUNT(*) AS cnt FROM cm.person WHERE person_id_num = ?";
        return queryForSingleValue(sql, id, "cnt");
    }

    /**
     * Retrieves the profile ID associated with the given email address.
     * Usage: Use this method to get the profile ID when you only have the email address.
     * This is typically the first step in retrieving other user-related information.
     */
    public Optional<String> getPersonProfileId(String email) throws SQLException {
        return retrieveValueFromDb("profile_id", "person_email", "email_addr", email);
    }

    /**
     * Retrieves the account ID associated with the given email address.
     * Usage: Use this method to get the account ID for a user by their email.
     */
    public Optional<String> getAccountId(String email) throws SQLException {
        return retrieveValueFromDb("account_id", "product_user", "profile_id", getPersonProfileId(email).orElse(null));
    }

    /**
     * Retrieves the person ID associated with the given email address.
     * 
     * Usage: Use this method to get the person ID for a user by their email.
     * Internally, it first retrieves the profile ID, then uses it to get the person ID.
     */
    public Optional<String> getPersonId(String email) throws SQLException {
        return retrieveValueFromDb("person_id", "product_user", "profile_id", getPersonProfileId(email).orElse(null));
    }

    /**
     * Retrieves a value based on the specified type and email address.
     * This is a convenience method that routes to the appropriate getter method.
     */
    public Optional<String> getValueBasedOnEmail(String retrievedVal, String email) throws SQLException {
        return switch (retrievedVal) {
            case "Profile ID" -> getPersonProfileId(email);
            case "Account ID" -> getAccountId(email);
            case "Person ID" -> getPersonId(email);
            default -> Optional.empty();
        };
    }

    /**
     * Generic method to retrieve a single value from the database based on a filter condition.
     * This method validates the query pattern against allowed queries for security.
     * 
     * Usage: This is an internal helper method used by public methods to perform database queries.
     * It should not be called directly unless you need to query a pre-validated table/column combination.
     * 
     * @param selectColumn The column name to retrieve from the database
     * @param tableName The table name to query (without schema prefix, will be prefixed with "cm.")
     * @param filterCol The column name to use in the WHERE clause
     * @param value The value to match in the WHERE clause
     * @return Optional containing the retrieved value as a String if found, empty Optional if not found or value is null
     * @throws SQLException if a database access error occurs
     * @throws IllegalArgumentException if the query pattern (table, selectColumn, filterCol) is not in the allowed list
     * 
     * Note: Only pre-defined query patterns are allowed for security reasons.
     */
    public Optional<String> retrieveValueFromDb(String selectColumn, String tableName, String filterCol, String value) throws SQLException {
        if (value == null) {
            return Optional.empty();
        }

        if (!isAllowedQuery(tableName, selectColumn, filterCol)) {
            throw new IllegalArgumentException("Unexpected query shape");
        }

        String sql = "SELECT " + selectColumn + " FROM cm." + tableName + " WHERE " + filterCol + " = ?" ;
        if (tableName.equalsIgnoreCase("person_email")) {
            sql += " order by created_date desc";
        }

        return queryForSingleValue(sql, value, selectColumn);
    }

    /**
     * Executes a parameterized SQL query and returns a single value from the result set.
     * This method uses try-with-resources to ensure proper resource cleanup (connection, statement, result set).
     * 
     * Usage: This is a low-level helper method that handles the actual database query execution.
     * It uses PreparedStatement to prevent SQL injection attacks and automatically closes resources.
     * 
     * @param sql The SQL query string with a single parameter placeholder (?)
     * @param param The parameter value to bind to the query
     * @param columnAlias The column name or alias to retrieve from the result set
     * @return Optional containing the value as a String if a row is found, empty Optional if no rows found
     * @throws SQLException if a database access error occurs
     * 
     * Note: The connection is opened and closed for each query. For high-frequency usage,
     * consider implementing connection pooling in a future enhancement.
     */
    private Optional<String> queryForSingleValue(String sql, String param, String columnAlias) throws SQLException {
        try (Connection conn = openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getString(columnAlias));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Opens a new database connection using the configured SQL connection parameters.
     */
    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(sqlConnection.getUrl(), sqlConnection.getUsername(), sqlConnection.getPassword());
    }

    /**
     * Validates whether a query pattern (table, selectColumn, filterColumn) is in the allowed list.
     * This is a security measure to prevent unauthorized database queries.
     * 
     * Usage: This method is used internally to validate query patterns before execution.
     * Only pre-defined query patterns are allowed to prevent SQL injection and unauthorized access.
     * 
     * @param table The table name to validate
     * @param selectColumn The column name to select
     * @param filterColumn The column name used in the WHERE clause
     * @return true if the query pattern is allowed, false otherwise
     */
    private boolean isAllowedQuery(String table, String selectColumn, String filterColumn) {
        return allowedQueries.stream().anyMatch(q ->
                q.table.equals(table) && q.selectColumn.equals(selectColumn) && q.filterColumn.equals(filterColumn)
        );
    }

    /**
     * Builds and returns the list of allowed query patterns for security validation.
     * 
     * Usage: This method is called during initialization to set up the whitelist of allowed queries.
     * Only queries matching these patterns will be executed by retrieveValueFromDb().
     * 
     * @return A list of QueryShape objects representing allowed query patterns
     * 
     * Note: To add new allowed query patterns, add new QueryShape entries to this list.
     * The current allowed patterns are:
     * - person_email table: select profile_id where email_addr = ?
     */
    private List<QueryShape> buildAllowedQueries() {
        List<QueryShape> queries = new ArrayList<>();
        queries.add(new QueryShape("person_email", "profile_id", "email_addr"));
        queries.add(new QueryShape("product_user", "account_id", "profile_id"));
        queries.add(new QueryShape("product_user", "person_id", "profile_id"));
        queries.add(new QueryShape("trade","status","account_id"));
        queries.add(new QueryShape("authentication","username","person_id"));
        queries.add(new QueryShape("trade","settlement_currency","account_id"));
        queries.add(new QueryShape("person_phone","phone_num","profile_id"));
        return queries;
    }

    /**
     * Immutable data class representing a valid query pattern.
     * Used to whitelist allowed database queries for security purposes.
     * 
     * This class encapsulates the three components of a query pattern:
     * - table: The database table name
     * - selectColumn: The column to retrieve
     * - filterColumn: The column used in the WHERE clause
     */
    private static final class QueryShape {
        private final String table;
        private final String selectColumn;
        private final String filterColumn;

        /**
         * Constructs a new QueryShape with the specified table, select column, and filter column.
         * 
         * @param table The database table name
         * @param selectColumn The column name to retrieve
         * @param filterColumn The column name to use in the WHERE clause
         */
        private QueryShape(String table, String selectColumn, String filterColumn) {
            this.table = table;
            this.selectColumn = selectColumn;
            this.filterColumn = filterColumn;
        }
    }
}

