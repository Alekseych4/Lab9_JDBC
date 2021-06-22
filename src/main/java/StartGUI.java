import entity.BookInfoEntity;
import entity.BookLocationEntity;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class StartGUI {
    private JFrame mainFrame;
    private final BookInfoService bookInfoService;
    private final BookLocationService bookLocationService;
    private JButton deleteBtn;
    private JButton addBtn;
    private JButton updateBtn;

    public StartGUI() {
        bookInfoService = new BookInfoService();
        bookLocationService = new BookLocationService();

        mainFrame = setUpMainFrame();
        JPanel panel = new JPanel(new BorderLayout());

        mainFrame.setContentPane(panel);

        mainFrame.getContentPane().setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));

        deleteBtn = new JButton("Удалить строку");
        updateBtn = new JButton("Обновить строку");
        addBtn = new JButton("Добавить строку");
//        mainFrame.add(jButton);
//        mainFrame.add(setUpTabs());
        mainFrame.getContentPane().add(deleteBtn);
        mainFrame.getContentPane().add(addBtn);
        mainFrame.getContentPane().add(updateBtn);
        mainFrame.getContentPane().add(setUpTabs());

//        mainFrame.setContentPane(panel);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartGUI();
            }
        });

        ConnectionResolver connectionResolver = new ConnectionResolver();

        try (Connection connection = connectionResolver.getConnection();
             InputStream inputStream = connectionResolver.getSchemaFile()){
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

    private JTabbedPane setUpTabs() {
        JTabbedPane jTabbedPane = new JTabbedPane();

        JTable bookLocationTable = new JTable(bookLocationService.getAll(), bookLocationService.getColumnNames());
        bookLocationTable.getModel().addTableModelListener(bookLocationTableListener());

        JTable bookInfoTable = new JTable(bookInfoService.getAll(), bookInfoService.getColumnNames());
        bookInfoTable.getModel().addTableModelListener(bookInfoTableListener());


        JScrollPane jScrollPane = new JScrollPane(bookLocationTable);
        bookLocationTable.setFillsViewportHeight(false);

        JScrollPane jScrollPane1 = new JScrollPane(bookInfoTable);
        bookLocationTable.setFillsViewportHeight(false);

        jTabbedPane.addTab("Место книги", jScrollPane);
        jTabbedPane.addTab("Информация о книге", jScrollPane1);

        addBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    BookLocationEntity blEntity = retrieveLocationTableData(bookLocationTable.getModel());
                    boolean f = bookLocationService.create(blEntity);
                    System.out.println(f);
                    break;
                case 1:
                    BookInfoEntity biEntity = retrieveInfoTableData(bookInfoTable.getModel());
                    bookInfoService.create(biEntity);
                    break;
                case -1:
                    break;
            }
        });

        deleteBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    for (int row : bookLocationTable.getSelectedRows()) {
                        if (bookLocationService.delete(Long.parseLong((String) bookInfoTable.getValueAt(row, 0)))){
                            ((DefaultTableModel) bookLocationTable.getModel()).removeRow(row);
                        }
                    }
                    break;
                case 1:
                    for (int row : bookInfoTable.getSelectedRows()) {
                        if (bookInfoService.delete(Long.parseLong((String) bookInfoTable.getValueAt(row, 0)))){
                            ((DefaultTableModel) bookInfoTable.getModel()).removeRow(row);
                        }
                    }
                    break;
            }
        });

        updateBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    BookLocationEntity blEntity = retrieveLocationTableData(bookLocationTable.getModel());
                    boolean f = bookLocationService.update(blEntity);
                    break;
                case 1:
                    BookInfoEntity biEntity = retrieveInfoTableData(bookInfoTable.getModel());
                    bookInfoService.update(biEntity);
                    break;
            }
        });

        jTabbedPane.addChangeListener(e -> {
            JTabbedPane jTabbedPane1 = (JTabbedPane) e.getSource();
            switch (jTabbedPane1.getSelectedIndex()){
                case 0:

                    break;
                case 1:
                    break;
                case -1:
                    break;
            }
        });

        return jTabbedPane;
    }

    private BookLocationEntity retrieveLocationTableData(TableModel bookLocationModel){
        BookLocationEntity blEntity = new BookLocationEntity();
        blEntity.setFloor(Integer.parseInt((String) bookLocationModel.getValueAt(0, 1)));
        blEntity.setBookcase(Integer.parseInt((String) bookLocationModel.getValueAt(0, 2)));
        blEntity.setShelf(Integer.parseInt((String) bookLocationModel.getValueAt(0, 3)));

        return blEntity;
    }

    private BookInfoEntity retrieveInfoTableData(TableModel bookInfoModel){
        BookInfoEntity biEntity = new BookInfoEntity();
        biEntity.setAuthorName((String) bookInfoModel.getValueAt(0, 1));
        biEntity.setAuthorSurname((String) bookInfoModel.getValueAt(0, 2));
        biEntity.setAuthorPatronymic((String) bookInfoModel.getValueAt(0, 3));
        biEntity.setEdition((String) bookInfoModel.getValueAt(0, 4));
        biEntity.setPublishingHouse((String) bookInfoModel.getValueAt(0, 5));
        biEntity.setPublishingYear((String) bookInfoModel.getValueAt(0, 6));
        biEntity.setPages(Integer.parseInt((String) bookInfoModel.getValueAt(0, 7)));
        biEntity.setWrittenYear((String) bookInfoModel.getValueAt(0, 8));
        biEntity.setWeight(Double.parseDouble((String) bookInfoModel.getValueAt(0, 9)));

        return biEntity;
    }

    private TableModelListener bookInfoTableListener(){
        return e -> {
            int row = e.getType();
            int column = e.getColumn();
            TableModel model = (TableModel)e.getSource();
            String columnName = model.getColumnName(column);
            Object data = model.getValueAt(row, column);
        };
    }

    private TableModelListener bookLocationTableListener(){
        return e -> {

        };
    }
}
