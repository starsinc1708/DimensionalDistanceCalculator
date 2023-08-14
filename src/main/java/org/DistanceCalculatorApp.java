package org;

import org.appLogic.DistanceCalculatorPanel;

import javax.swing.*;

public class DistanceCalculatorApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dimensional Distance Calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.getContentPane().add(new DistanceCalculatorPanel());
            frame.pack();
            frame.setSize(1000, 600);
            frame.setVisible(true);
        });
    }
}
