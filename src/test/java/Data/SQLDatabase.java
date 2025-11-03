package Data;

import utils.SQLConnection;

import java.sql.*;

import static utils.BaseTest.productEnv;


public class SQLDatabase {
    public String count;
    private final SQLConnection sqlConnection;

    public SQLDatabase() {
        this.sqlConnection = new SQLConnection(productEnv);
    }

    public Statement sqlDatabaseConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(sqlConnection.getUrl(), sqlConnection.getUsername(), sqlConnection.getPassword());
            return conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPersonIdCount(String id) throws SQLException {

        Statement stmt = sqlDatabaseConnection();
        try {

            ResultSet resultSet = stmt.executeQuery("SELECT Count(*) FROM cm.person where person_id_num = '" + id + "'");
            while (resultSet.next()) {
                return resultSet.getString("Count(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPersonProfileId(String email) throws SQLException {
        return retrieveValueFromDb("profile_id", "person_email", "email_addr", email);
    }

    public String getAccountId(String email) throws SQLException {
        return retrieveValueFromDb("account_id", "product_user", "profile_id", getPersonProfileId(email));
    }

    public String getPersonId(String email) throws SQLException {
        return retrieveValueFromDb("person_id", "product_user", "profile_id", getPersonProfileId(email));
    }

    public String retrieveValueFromDb(String retrieveColumnName, String tableName, String filterCol, String value) throws SQLException {
        Statement stmt = sqlDatabaseConnection();
        try {
            ResultSet resultSet = stmt.executeQuery("SELECT " + retrieveColumnName + " FROM cm." + tableName + " where " + filterCol + " = " + "'" + value + "'");
            while (resultSet.next()) {
                return resultSet.getString(retrieveColumnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getValueBasedOnEmail(String retrievedVal, String email) throws SQLException {
        return switch (retrievedVal) {
            case "Profile ID" -> getPersonProfileId(email);
            case "Account ID" -> getAccountId(email);
            case "Person ID" -> getPersonId(email);
            default -> "";
        };
    }

}
