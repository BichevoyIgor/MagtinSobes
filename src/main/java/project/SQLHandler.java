package project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
Сlass that execute methods for working with a database
 */

public class SQLHandler {

    private PreparedStatement statement;

    // inserting values into the database
    public void insertDb(Connection connection, int count) {
        try {
            statement = connection.prepareStatement("TRUNCATE TABLE test");
            statement.executeUpdate();
            int currentDigit = 1;
            connection.setAutoCommit(false);
            while (currentDigit < count) {
                statement = connection.prepareStatement("INSERT INTO test (field) values(?)");
                for (int i = 0; i < 100; i++, currentDigit++) {
                    statement.setInt(1, currentDigit);
                    statement.addBatch();
                    if (currentDigit == count) break;
                }
                statement.executeBatch();
            }
            connection.commit();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // getting values to the database
    public List<String> selectDb(Connection connection) {
        List<String> listResult = new ArrayList<>();
        try {
            statement = connection.prepareStatement("select field from test");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                listResult.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listResult;
    }
}
