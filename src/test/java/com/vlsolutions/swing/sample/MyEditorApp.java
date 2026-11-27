package com.vlsolutions.swing.sample;

import com.vlsolutions.swing.docking.DockingConstants;

import javax.swing.*;
import java.awt.*;

public class MyEditorApp extends DockedApplication {

    public MyEditorApp() {
        desk.addDockable(editorPanel);
        desk.split(editorPanel, treePanel, DockingConstants.SPLIT_LEFT);
        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton("OK");
        button.setName("OkButton");
        panel.add(button, BorderLayout.SOUTH);
        panel.add(desk, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        MyEditorApp frame = new MyEditorApp();
        frame.setName("parent");
        frame.setSize(800, 600);
        frame.validate();
        // in the event dispatch thread
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}