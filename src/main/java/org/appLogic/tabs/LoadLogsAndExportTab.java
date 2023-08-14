package org.appLogic.tabs;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.core.HibernateUtil;
import org.core.entity.DistanceLog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LoadLogsAndExportTab extends JPanel {
    private final JButton loadLogButton;
    private final JButton exportToExcelButton;
    private final JTable logTable;


    public LoadLogsAndExportTab() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        loadLogButton = new JButton("Load Log");
        loadLogButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        exportToExcelButton = new JButton("Export to Excel");
        exportToExcelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane logScrollPane = new JScrollPane(logTable);
        add(loadLogButton);
        add(logScrollPane);
        add(exportToExcelButton);

        addListeners();
    }

    private void addListeners() {
        addLoadLogListener();
        addExcelExportListener();
    }

    private void addLoadLogListener() {
        loadLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<DistanceLog> distanceLogs = getDistanceLogsFromDatabase();
                DefaultTableModel logTableModel = createLogTableModel(distanceLogs);
                logTable.setModel(logTableModel);
            }

            private java.util.List<DistanceLog> getDistanceLogsFromDatabase() {
                java.util.List<DistanceLog> distanceLogs = new ArrayList<>();
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                try (Session session = sessionFactory.openSession()){
                    CriteriaBuilder builder = session.getCriteriaBuilder();
                    CriteriaQuery<DistanceLog> criteriaQuery = builder.createQuery(DistanceLog.class);
                    criteriaQuery.from(DistanceLog.class);
                    distanceLogs = session.createQuery(criteriaQuery).getResultList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return distanceLogs;
            }

            private DefaultTableModel createLogTableModel(List<DistanceLog> distanceLogs) {
                DefaultTableModel model = new DefaultTableModel(
                        new Object[]{"Run ID","Point A",  "Point B", "Method", "Distance", "Start Time", "End Time"},
                        0
                );
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (DistanceLog log: distanceLogs) {
                    model.addRow(new Object[]{
                            log.getRunId(),
                            log.getPointADescription(),
                            log.getPointBDescription(),
                            log.getCalculationMethod(),
                            log.getDistance(),
                            dateFormat.format(log.getStartTime()),
                            dateFormat.format(log.getEndTime()),
                    });
                }
                return model;
            }
        });
    }

    private void addExcelExportListener() {
        exportToExcelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".xls")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".xls");
                }
                try {
                    WritableWorkbook workbook = Workbook.createWorkbook(selectedFile);
                    WritableSheet sheet = workbook.createSheet("Log", 0);

                    for (int col = 0; col < logTable.getColumnCount(); col++) {
                        jxl.write.Label label = new jxl.write.Label(col, 0, logTable.getColumnName(col));
                        sheet.addCell(label);
                    }

                    for (int row = 0; row < logTable.getRowCount(); row++) {
                        for (int col = 0; col < logTable.getColumnCount(); col++) {
                            jxl.write.Label label = new Label(col, row + 1, logTable.getValueAt(row, col).toString());
                            sheet.addCell(label);
                        }
                    }
                    workbook.write();
                    workbook.close();
                    JOptionPane.showMessageDialog(null, "Log exported to Excel: " + selectedFile.getName(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException | WriteException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while exporting to Excel", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
