package task.timer.front;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OneTableCellRenderer extends DefaultTableCellRenderer {

    
    private static final long serialVersionUID = 1423537746904743064L;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row % 2 == 0) {
            comp.setBackground(new Color(202, 225, 255));
        } else {
            comp.setBackground(new Color(254, 254, 254));
        }

        if (column == 0) {
            ((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
        } else if (column == 6) {
            ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
        } else {
            ((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
        }

        if (isSelected) {
            comp.setBackground(new Color(comp.getBackground().getRed() - 40, comp.getBackground().getGreen() - 40, comp.getBackground().getBlue() - 40));
        }
        return (comp);
    }
}
