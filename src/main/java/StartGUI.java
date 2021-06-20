import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class StartGUI {
    private JFrame mainFrame;

    public StartGUI() {
        mainFrame = setUpMainFrame();

        mainFrame.add(setUpMenuBar());
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartGUI();
            }
        });

        ConnectionClass connectionClass = new ConnectionClass();

        try (Connection connection = connectionClass.getConnection();
             InputStream inputStream = connectionClass.getSchemaFile()){
            Properties properties = new Properties();
            properties.load(inputStream);
            System.out.println(properties.getProperty("bookLocation"));

            Statement statement = connection.createStatement();
            statement.execute(properties.getProperty("bookLocation"));
            statement.execute(properties.getProperty("bookInfo"));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private JFrame setUpMainFrame() {
        JFrame mainFrame = new JFrame("Домашняя библиотека");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        return mainFrame;
    }

    private JTabbedPane setUpMenuBar() {
        JTabbedPane jTabbedPane = new JTabbedPane();

        JTable jTable = new JTable(2, 2);
//        jTable.tableChanged();
        JTable jTable1 = new JTable(4, 4);

        JScrollPane jScrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JScrollPane jScrollPane1 = new JScrollPane(jTable1);
        jTable1.setFillsViewportHeight(true);

        jTabbedPane.addTab("Hello", jScrollPane);
        jTabbedPane.addTab("Hello2", jScrollPane1);

        jTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });

        return jTabbedPane;
    }
}
