
import entity.BookInfoEntity;
import entity.BookLocationEntity;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

public class StartGUI {
    private JFrame mainFrame;
    private final BookInfoService bookInfoService;
    private final BookLocationService bookLocationService;
    private JButton deleteBtn;
    private JButton addBtn;
    private JButton updateBtn;
    private JButton editionsBtn;
    private JButton authorBtn;

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
        editionsBtn = new JButton("Выдать издания в шкафу");
        authorBtn = new JButton("Всего страниц у автора");
//        mainFrame.add(jButton);
//        mainFrame.add(setUpTabs());
        mainFrame.getContentPane().add(deleteBtn);
        mainFrame.getContentPane().add(addBtn);
        mainFrame.getContentPane().add(updateBtn);
        mainFrame.getContentPane().add(editionsBtn);
        mainFrame.getContentPane().add(authorBtn);
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

//            Statement drop = connection.createStatement();
//            drop.execute("DROP TABLE book_location");
//
//            Statement drop2 = connection.createStatement();
//            drop2.execute("DROP TABLE book_info");

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

        JTable bookLocationTable = new JTable(new CustomTableModel(bookLocationService.getAll(), bookLocationService.getColumnNames()));
//        bookLocationTable.getModel().addTableModelListener(bookLocationTableListener());

        JTable bookInfoTable = new JTable(new CustomTableModel(bookInfoService.getAll(), bookInfoService.getColumnNames()));
//        bookInfoTable.getModel().addTableModelListener(bookInfoTableListener());

        JTable surnamesTable = new JTable(new CustomTableModel(bookInfoService.getSortedSurnames(), bookInfoService.getSurnameColumn()));

        JTable bookcaseTable = new JTable(new CustomTableModel(bookInfoService.getEditionByBookcase(-1), bookInfoService.getEditionColumn())){
            @Override
            public boolean isCellEditable(int row, int column) {
                return row == 0;
            }
        };

        JTable pagesTable = new JTable(new CustomTableModel(bookInfoService.getPagesByFullName("", "", "", true), bookInfoService.getPagesColumn())){
            @Override
            public boolean isCellEditable(int row, int column) {
                return row == 0;
            }
        };

        JScrollPane jScrollPane = new JScrollPane(bookLocationTable);
        bookLocationTable.setFillsViewportHeight(false);

        JScrollPane jScrollPane1 = new JScrollPane(bookInfoTable);
        bookLocationTable.setFillsViewportHeight(false);

        JScrollPane jScrollPane2 = new JScrollPane(surnamesTable);
        surnamesTable.setFillsViewportHeight(false);

        JScrollPane jScrollPane3 = new JScrollPane(bookcaseTable);
        bookcaseTable.setFillsViewportHeight(false);

        JScrollPane jScrollPane4 = new JScrollPane(pagesTable);
        pagesTable.setFillsViewportHeight(false);

        jTabbedPane.addTab("Место книги", jScrollPane);
        jTabbedPane.addTab("Информация о книге", jScrollPane1);
        jTabbedPane.addTab("Фамилии", jScrollPane2);
        jTabbedPane.addTab("Ввести шкаф", jScrollPane3);
        jTabbedPane.addTab("Страницы автора", jScrollPane4);

        addBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    BookLocationEntity blEntity = retrieveLocationData(bookLocationTable.getModel(), 0, false);
                    if (bookLocationService.create(blEntity)){
                        refreshTableData(bookLocationTable, bookLocationService.getAll());
                    }

                    break;
                case 1:
                    BookInfoEntity biEntity = retrieveInfoData(bookInfoTable.getModel(), 0, false);
                    if (bookInfoService.create(biEntity)) {
                        refreshTableData(bookInfoTable, bookInfoService.getAll());
                    }
                    break;
                case -1:
                    break;
            }
        });

        deleteBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    for (int row : bookLocationTable.getSelectedRows()) {
                        if (row != 0)
                            bookLocationService.delete(Long.parseLong((String) bookLocationTable.getValueAt(row, 0)));
                    }
                    refreshTableData(bookLocationTable, bookLocationService.getAll());
                    break;
                case 1:
                    for (int row : bookInfoTable.getSelectedRows()) {
                        if (row != 0)
                            bookInfoService.delete(Long.parseLong((String) bookInfoTable.getValueAt(row, 0)));
                    }
                    refreshTableData(bookInfoTable, bookInfoService.getAll());
                    break;
            }
        });

        updateBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    BookLocationEntity blEntity = retrieveLocationData(bookLocationTable.getModel(), bookLocationTable.getSelectedRow(), true);
                    if (bookLocationService.update(blEntity)) {
                        refreshTableData(bookLocationTable, bookLocationService.getAll());
                    }
                    break;
                case 1:
                    BookInfoEntity biEntity = retrieveInfoData(bookInfoTable.getModel(), bookInfoTable.getSelectedRow(), true);
                    if (bookInfoService.update(biEntity)) {
                        refreshTableData(bookInfoTable, bookInfoService.getAll());
                    }
                    break;
            }
        });

        editionsBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 3:
                    CustomTableModel model = (CustomTableModel) bookcaseTable.getModel();
                    String bookcase = getNonNullString(model.getValueAt(0, 0));
                    refreshTableData(bookcaseTable, bookInfoService.getEditionByBookcase(Integer.parseInt(bookcase)));
                    break;
            }
        });

        authorBtn.addActionListener(l -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 4:
                    CustomTableModel model = (CustomTableModel) pagesTable.getModel();
                    String name = getNonNullString(model.getValueAt(0, 0));
                    String surname = getNonNullString(model.getValueAt(0, 1));
                    String p = getNonNullString(model.getValueAt(0, 2));
                    refreshTableData(pagesTable, bookInfoService.getPagesByFullName(name, surname, p, false));
                    break;
            }
        });

        jTabbedPane.addChangeListener(e -> {
            switch (jTabbedPane.getSelectedIndex()){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    refreshTableData(surnamesTable, bookInfoService.getSortedSurnames());
                    break;
            }
        });

        return jTabbedPane;
    }

    private BookLocationEntity retrieveLocationData(TableModel bookLocationModel, int row, boolean updateFlag){
        BookLocationEntity blEntity = new BookLocationEntity();

        if (updateFlag)
            blEntity.setId(Long.parseLong(getNonNullString(bookLocationModel.getValueAt(row, 0))));
        blEntity.setFloor(Integer.parseInt(getNonNullString(bookLocationModel.getValueAt(row, 1))));
        blEntity.setBookcase(Integer.parseInt(getNonNullString(bookLocationModel.getValueAt(row, 2))));
        blEntity.setShelf(Integer.parseInt(getNonNullString(bookLocationModel.getValueAt(row, 3))));

        return blEntity;
    }

    private BookInfoEntity retrieveInfoData(TableModel bookInfoModel, int row, boolean updateFlag){
        BookInfoEntity biEntity = new BookInfoEntity();

        if (updateFlag)
            biEntity.setId(Long.parseLong(getNonNullString(bookInfoModel.getValueAt(row, 0))));
        biEntity.setAuthorName(getNonNullString(bookInfoModel.getValueAt(row, 1)));
        biEntity.setAuthorSurname(getNonNullString(bookInfoModel.getValueAt(row, 2)));
        biEntity.setAuthorPatronymic(getNonNullString(bookInfoModel.getValueAt(row, 3)));
        biEntity.setEdition(getNonNullString(bookInfoModel.getValueAt(row, 4)));
        biEntity.setPublishingHouse(getNonNullString(bookInfoModel.getValueAt(row, 5)));
        biEntity.setPublishingYear(getNonNullString(bookInfoModel.getValueAt(row, 6)));
        biEntity.setPages(Integer.parseInt(getNonNullString(bookInfoModel.getValueAt(row, 7))));
        biEntity.setWrittenYear(getNonNullString(bookInfoModel.getValueAt(row, 8)));
        biEntity.setWeight(Double.parseDouble(getNonNullString(bookInfoModel.getValueAt(row, 9))));
        biEntity.setLocationId(Integer.parseInt(getNonNullString(bookInfoModel.getValueAt(row, 10))));

        return biEntity;
    }

    private String getNonNullString(Object cellValue) {
        String c = (String) cellValue;
        if (c == null || c.isEmpty()){
            throw new RuntimeException("Не определено поле");
        }

        return c;
    }

    private void refreshTableData(JTable jTable, Vector<Vector<String>> rows){
        CustomTableModel model = (CustomTableModel) jTable.getModel();
        for (int i = jTable.getRowCount() - 1; i >= 0; i--) {
            model.removeRow(i);
        }

        for (Vector<String> row : rows) {
            model.addRow(row);
        }
    }

    private TableModelListener bookInfoTableListener(){
        return e -> {
            int row = e.getType();
            int column = e.getColumn();
            CustomTableModel model = (CustomTableModel) e.getSource();

            String columnName = model.getColumnName(column);
            Object data = model.getValueAt(row, column);
        };
    }

    private TableModelListener bookcaseTableListener(){
        return e -> {


        };
    }
}
