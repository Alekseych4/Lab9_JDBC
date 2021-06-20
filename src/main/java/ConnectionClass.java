import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

public class ConnectionClass {

    public Connection getConnection(){
        Properties properties = new Properties();
        Connection connection = null;

        try (InputStream inputStream = this.getClass().getResourceAsStream("/properties.yml");){
            properties.load(inputStream);
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
                    properties.getProperty("password"));

//            Enumeration<?> e = properties.propertyNames();
//            while (e.hasMoreElements()){
//                System.out.println(e.nextElement());
//            }
//            properties.list(System.out);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public InputStream getSchemaFile (){
        return this.getClass().getResourceAsStream("/dbSchema.yml");
    }

    public void test(){
        InputStream inputStream = this.getClass().getResourceAsStream("/properties.yml");
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNext()){
            System.out.println(sc.next());
        }
    }
}
