import entity.BookInfoEntity;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class BookInfoService {
    private final ConnectionResolver connectionResolver;
    private Connection connection;
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
        checkConnection();
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
            preparedCreate.setInt(10, bookInfo.getLocationId());

            preparedCreate.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Vector<Vector<String>> getAll(){
        checkConnection();
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
                    bInfo.add(String.valueOf(rs.getInt(11)));

                    books.add(bInfo);
                }

                return books;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public Vector<Vector<String>> getSortedSurnames () {
        checkConnection();
        try (Statement statement = connection.createStatement()){
            if (statement.execute(properties.getProperty("selectQuery"))) {
                ResultSet rs = statement.getResultSet();
                Vector<Vector<String>> surnames = new Vector<>();

                while (rs.next()) {
                    Vector<String> surname = new Vector<>();
                    surname.add(rs.getString(1));
                    surnames.add(surname);
                }

                return surnames;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vector<Vector<String>> getEditionByBookcase(int bookcase) {
        if (bookcase == -1){
            Vector<Vector<String>> eds = new Vector<>();
            eds.add(new Vector<>());
            return eds;
        }
        checkConnection();
        try (PreparedStatement statement = connection.prepareStatement(properties.getProperty("selectByBookcase"))){

            statement.setInt(1, bookcase);

            if (statement.execute()){
                Vector<Vector<String>> eds = new Vector<>();
                ResultSet rs = statement.getResultSet();
                eds.add(new Vector<>());

                while (rs.next()){
                    Vector<String> ed = new Vector<>();
                    ed.add(rs.getString(1));
                    eds.add(ed);
                }
                return eds;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vector<Vector<String>> getPagesByFullName(String name, String surname, String patronymic, boolean empty){
        if (empty){
            Vector<Vector<String>> eds = new Vector<>();
            eds.add(new Vector<>());
            return eds;
        }
        checkConnection();
        try (PreparedStatement statement = connection.prepareStatement(properties.getProperty("selectPages"))){
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setString(3, patronymic);

            if (statement.execute()){
                Vector<Vector<String>> eds = new Vector<>();
                ResultSet rs = statement.getResultSet();
                eds.add(new Vector<>());
                int count = 0;

                while (rs.next()){
                    count += Integer.parseInt(rs.getString(1));
                }
                Vector<String> ed = new Vector<>();
                ed.add(String.valueOf(count));
                ed.add("");
                ed.add("");
                eds.add(ed);

                return eds;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(BookInfoEntity bookInfo){
        checkConnection();
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
            preparedUpdate.setInt(10, bookInfo.getLocationId());
            preparedUpdate.setLong(11, bookInfo.getId());

            preparedUpdate.execute();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updateStatic(BookInfoEntity bookInfo){
        checkConnection();
        try (PreparedStatement preparedUpdate = connection.prepareStatement(properties.getProperty("updateStaticQuery"))){
            preparedUpdate.setLong(1, bookInfo.getId());

            preparedUpdate.execute();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Vector<String> getColumnNames(){
        Vector<String> columns = new Vector<>(10);
        columns.add("id");
        columns.add("Имя");
        columns.add("Фамилия");
        columns.add("Отчество");
        columns.add("Издание");
        columns.add("Издательский дом");
        columns.add("Год издания");
        columns.add("Кол-во страниц");
        columns.add("Год написания");
        columns.add("Вес, гр");
        columns.add("id места");

        return columns;
    }

    public Vector<String> getSurnameColumn(){
        Vector<String> columns = new Vector<>();
        columns.add("Фамилия");
        return columns;
    }

    public Vector<String> getEditionColumn(){
        Vector<String> columns = new Vector<>();
        columns.add("Издание");
        return columns;
    }

    public Vector<String> getPagesColumn(){
        Vector<String> columns = new Vector<>();
        columns.add("Имя/Страницы");
        columns.add("Фамилия");
        columns.add("Отчество");
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

    private void checkConnection(){
        try {
            if (connection.isClosed()) {
                connection = connectionResolver.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
