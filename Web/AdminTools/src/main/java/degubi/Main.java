package degubi;

import java.awt.*;
import java.util.*;
import java.util.stream.*;
import javax.swing.*;

public final class Main {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        var frame = new JFrame("Admin Tools");
        var panel = new JTabbedPane();
        var taskSelector = Components.<String>newComboBox(120, 20);
        
        panel.addChangeListener(e -> refreshTaskList(taskSelector));
        panel.addTab("New Task", createNewTaskPanel());
        panel.addTab("Stored Tasks", createStoredTasksPanel(taskSelector));
        
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    
    private static JPanel createStoredTasksPanel(JComboBox<String> taskSelector) {
        var panel = new JPanel(null);
        
        panel.add(Components.newLabel("Tasks:", 22, 20));
        panel.add(taskSelector);
        panel.add(Components.newButton("Delete Selected", 300, 20, e -> {
            Backend.sendDeleteRequest("/tasks", taskSelector.getSelectedItem());
            refreshTaskList(taskSelector);
        }));
        
        panel.setBackground(Color.DARK_GRAY);
        return panel;
    }

    private static JPanel createNewTaskPanel() {
        var panel = new JPanel(null);
        var taskNameField = Components.newTextField(120, 20);
        var pdfURLField = Components.newTextField(120, 60);
        var consoleInputArea = Components.newTextArea(120, 100);
        
        panel.add(Components.newLabel("Task Name:", 22, 20));
        panel.add(taskNameField);
        panel.add(Components.newLabel("PDF URL:", 22, 60));
        panel.add(pdfURLField);
        panel.add(Components.newLabel("Console Input:", 22, 100));
        panel.add(consoleInputArea);
        panel.add(Components.newButton("Add", 600, 500, e -> {
            var data = Map.of("name", taskNameField.getText(), "pdfURL", pdfURLField.getText(), "consoleInput", consoleInputArea.getText());
            Backend.sendPutRequest("/tasks", data);
        }));
        
        panel.setBackground(Color.DARK_GRAY);
        return panel;
    }
    
    private static void refreshTaskList(JComboBox<String> taskSelector) {
        var taskArray = Backend.sendGetRequest("/tasks", Backend.ofJsonArray()).body();
        var tasks = IntStream.range(0, taskArray.size())
                             .mapToObj(taskArray::getString)
                             .toArray(String[]::new);
        
        taskSelector.setModel(new DefaultComboBoxModel<>(tasks));
    }
}