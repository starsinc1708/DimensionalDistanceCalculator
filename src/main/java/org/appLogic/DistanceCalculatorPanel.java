package org.appLogic;

import org.appLogic.tabs.CalculationTab;
import org.appLogic.tabs.LoadLogsAndExportTab;

import javax.swing.*;
import java.awt.*;

public class DistanceCalculatorPanel extends JPanel {

    private CalculationTab calculationTab;
    private LoadLogsAndExportTab logsAndExportTab;

    public DistanceCalculatorPanel() {
        setLayout(new BorderLayout());
        init();
        createUI();
    }

    private void init() {
        calculationTab = new CalculationTab();
        logsAndExportTab = new LoadLogsAndExportTab();
    }

    private void createUI() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Calculation", calculationTab);
        tabbedPane.addTab("Logs & Export", logsAndExportTab);
        add(tabbedPane, BorderLayout.CENTER);
    }
}

