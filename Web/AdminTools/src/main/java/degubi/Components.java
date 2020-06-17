package degubi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public final class Components {
    private Components() {}
    
    public static JLabel newLabel(String text, int x, int y) {
        var label = new JLabel(text);
        label.setBounds(x, y, text.length() * 7, 30);
        label.setForeground(Color.WHITE);
        return label;
    }
    
    public static JTextField newTextField(int x, int y) {
        var field = new JTextField();
        field.setBounds(x, y, 250, 30);
        field.setBorder(new EmptyBorder(0, 5, 0, 0));
        return field;
    }
    
    public static JButton newButton(String text, int x, int y, ActionListener listener) {
        var butt = new JButton(text);
        butt.addActionListener(listener);
        butt.setBounds(x, y, 150, 30);
        return butt;
    }
    
    public static JTextArea newTextArea(int x, int y) {
        var area = new JTextArea();
        area.setBounds(x, y, 350, 150);
        area.setFont(area.getFont().deriveFont(12F));
        return area;
    }
    
    public static<T> JComboBox<T> newComboBox(int x, int y) {
        var box = new JComboBox<T>();
        box.setBounds(x, y, 150, 30);
        return box;
    }
}