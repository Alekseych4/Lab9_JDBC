import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class CustomTableModel extends DefaultTableModel {
    public CustomTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }
}
