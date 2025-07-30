package Data;

import java.sql.*;

public class SQLDatabase {
    public String count;

    public Statement retriveDataFromSQLDB() throws SQLException {

        String url = "jdbc:mysql://empsg-uat-aocm-sql-db.cg8qhq5ec18a.ap-southeast-1.rds.amazonaws.com:3306/cm?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC"; // or jdbc:postgresql://...
        String user = "admin";
        String password = "jHt03$pQ5f";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPersonIdCount(String id) throws SQLException {

        Statement stmt = retriveDataFromSQLDB();
        try {

            ResultSet resultSet = stmt.executeQuery("SELECT Count(*) FROM cm.person where person_id_num = '"+id+"'");
            while (resultSet.next()) {
                return resultSet.getString("Count(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
