import entity.BookLocationEntity;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

public class BookLocationService {
    private final ConnectionResolver connectionResolver;
    private final Connection connection;
    private final Properties properties;

    public BookLocationService() {
        connectionResolver = new ConnectionResolver();
        this.connection = connectionResolver.getConnection();
        properties = new Properties();
        initProperties();
    }

    private void initProperties(){
        try(InputStream inputStream = connection.getClass().getResourceAsStream("/bookLocation.yml")){
            properties.load(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean create(BookLocationEntity bookLocationEntity){
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty("insertQuery"))){
            preparedStatement.setInt(1, bookLocationEntity.getFloor());
            preparedStatement.setInt(2, bookLocationEntity.getBookcase());
            preparedStatement.setInt(3, bookLocationEntity.getShelf());

            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean update(BookLocationEntity bookLocationEntity){
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty("updateQuery"))){
            preparedStatement.setInt(1, bookLocationEntity.getFloor());
            preparedStatement.setInt(2, bookLocationEntity.getBookcase());
            preparedStatement.setInt(3, bookLocationEntity.getShelf());

            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return true;
    }

    public Vector<Vector<String>> getAll(){
        try (Statement statement = connection.createStatement()) {

            if (statement.execute(properties.getProperty("selectAllQuery"))) {
                ResultSet rs = statement.getResultSet();
                Vector<Vector<String>> locations = new Vector<>();
                locations.add(new Vector<>());

                while(rs.next()) {
                    Vector<String> location = new Vector<>();

                    location.add(String.valueOf(rs.getLong(1)));
                    location.add(String.valueOf(rs.getInt(2)));
                    location.add(String.valueOf(rs.getInt(3)));
                    location.add(String.valueOf(rs.getInt(4)));

                    locations.add(location);
                }

                return locations;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public Vector<String> getColumnNames(){
        Vector<String> columns = new Vector<>(4);
        columns.add(properties.getProperty("id"));
        columns.add(properties.getProperty("floor"));
        columns.add(properties.getProperty("bookcase"));
        columns.add(properties.getProperty("shelf"));

        return columns;
    }
}
