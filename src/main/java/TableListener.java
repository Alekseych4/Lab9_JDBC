import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TableListener implements TableModelListener {
    @Override
    public void tableChanged(TableModelEvent e) {
        TableModel tableModel = (TableModel) e.getSource();
    }
}
