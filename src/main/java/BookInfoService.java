import entity.BookInfoEntity;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class BookInfoService {
    private final ConnectionResolver connectionResolver;
    private final Connection connection;
    private final Properties properties;

    public BookInfoService() {
        connectionResolver = new ConnectionResolver();
        this.connection = connectionResolver.getConnection();
        properties = new Properties();
        initProperties();
    }

    private void initProperties(){
        try(InputStream inputStream = connection.getClass().getResourceAsStream("/bookInfo.yml")){
            properties.load(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean create(BookInfoEntity bookInfo){
        try (PreparedStatement preparedCreate = connection.prepareStatement(properties.getProperty("insertQuery"))){

            preparedCreate.setString(1, bookInfo.getAuthorName());
            preparedCreate.setString(2, bookInfo.getAuthorSurname());
            preparedCreate.setString(3, bookInfo.getAuthorPatronymic());
            preparedCreate.setString(4, bookInfo.getEdition());
            preparedCreate.setString(5, bookInfo.getPublishingHouse());
            preparedCreate.setString(6, bookInfo.getPublishingYear());
            preparedCreate.setInt(7, bookInfo.getPages());
            preparedCreate.setString(8, bookInfo.getWrittenYear());
            preparedCreate.setDouble(9, bookInfo.getWeight());

            preparedCreate.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Vector<Vector<String>> getAll(){
        try (Statement statement = connection.createStatement()) {

            if (statement.execute(properties.getProperty("selectAllQuery"))) {
                ResultSet rs = statement.getResultSet();
                Vector<Vector<String>> books = new Vector<>();
                books.add(new Vector<>());

                while(rs.next()) {
                    Vector<String> bInfo = new Vector<>();

                    bInfo.add(String.valueOf(rs.getLong(1)));
                    bInfo.add(rs.getString(2));
                    bInfo.add(rs.getString(3));
                    bInfo.add(rs.getString(4));
                    bInfo.add(rs.getString(5));
                    bInfo.add(rs.getString(6));
                    bInfo.add(rs.getString(7));
                    bInfo.add(String.valueOf(rs.getInt(8)));
                    bInfo.add(rs.getString(9));
                    bInfo.add(String.valueOf(rs.getDouble(10)));

                    books.add(bInfo);
                }

                return books;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(BookInfoEntity bookInfo){
        try (PreparedStatement preparedUpdate = connection.prepareStatement(properties.getProperty("updateQuery"))){
            preparedUpdate.setString(1, bookInfo.getAuthorName());
            preparedUpdate.setString(2, bookInfo.getAuthorSurname());
            preparedUpdate.setString(3, bookInfo.getAuthorPatronymic());
            preparedUpdate.setString(4, bookInfo.getEdition());
            preparedUpdate.setString(5, bookInfo.getPublishingHouse());
            preparedUpdate.setString(6, bookInfo.getPublishingYear());
            preparedUpdate.setInt(7, bookInfo.getPages());
            preparedUpdate.setString(8, bookInfo.getWrittenYear());
            preparedUpdate.setDouble(9, bookInfo.getWeight());
            preparedUpdate.setLong(10, bookInfo.getId());

            preparedUpdate.execute();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Vector<String> getColumnNames(){
        Vector<String> columns = new Vector<>(10);
        columns.add(properties.getProperty("id"));
        columns.add(properties.getProperty("name"));
        columns.add(properties.getProperty("surname"));
        columns.add(properties.getProperty("patronymic"));
        columns.add(properties.getProperty("edition"));
        columns.add(properties.getProperty("publishingHouse"));
        columns.add(properties.getProperty("publishingYear"));
        columns.add(properties.getProperty("pages"));
        columns.add(properties.getProperty("writtenYear"));
        columns.add(properties.getProperty("weight"));

        return columns;
    }

    public boolean delete(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty("deleteQuery"))){
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
