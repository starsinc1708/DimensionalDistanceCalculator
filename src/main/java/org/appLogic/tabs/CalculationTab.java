package org.appLogic.tabs;

import org.core.HibernateUtil;
import org.core.XMLParser;
import org.core.distance_calculators.DistanceCalculatorFactory;
import org.core.entity.DistanceLog;
import org.core.points.Point;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class CalculationTab extends JPanel {
    private final JButton loadFileButton;
    private final JButton calculateButton;
    private final JTable resultTable;
    private long runId;
    private Map<Point, Point> pairList;

    public CalculationTab() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        loadFileButton = new JButton("Load File");
        loadFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        calculateButton = new JButton("Calculate");
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        add(loadFileButton);
        add(resultScrollPane);
        add(calculateButton);

        getRunIdFromDataBase();
        addListeners();
    }

    private void getRunIdFromDataBase() {
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            Long maxRunId = (Long) session.createQuery("SELECT MAX(runId) FROM DistanceLog").uniqueResult();
            if (maxRunId != null) {
                runId = maxRunId + 1;
            }
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addListeners() {
        addLoadFileListener();
        addCalculationListener();
    }

    private void addLoadFileListener() {
        loadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter xmlFilter = new FileNameExtensionFilter("XML Files", "xml");
                fileChooser.setFileFilter(xmlFilter);

                if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
                File loadedFile = fileChooser.getSelectedFile();

                try {
                    pairList = XMLParser.parseXML(loadedFile.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "Loaded file: " + loadedFile.getName(), "File Loaded", JOptionPane.INFORMATION_MESSAGE);
                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid type of File (.xml needed)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void addCalculationListener() {
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pairList == null || pairList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No data available for calculation", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DefaultTableModel model = createDefaultTableModel();
                resultTable.setModel(model);
            }

            private DefaultTableModel createDefaultTableModel() {
                DefaultTableModel model = new DefaultTableModel(new Object[]{"Point A", "Point B", "Distance"}, 0);
                DistanceCalculatorFactory calculatorFactory = new DistanceCalculatorFactory();
                Point pointA;
                Point pointB;
                Date calculationStartTime;
                Date calculationEndTime;
                for (Map.Entry<Point, Point> pair : pairList.entrySet()) {
                    pointA = pair.getKey();
                    pointB = pair.getValue();
                    calculationStartTime = new Date();
                    double distance = calculatorFactory.calculateDistance(pointA, pointB);
                    calculationEndTime = new Date();
                    model.addRow(new Object[]{pointA.print(), pointB.print(), distance});
                    saveCalculationToDistanceLog(new DistanceLog(
                            runId,
                            calculationStartTime,
                            calculationEndTime,
                            pointA.print(),
                            pointB.print(),
                            calculatorFactory.getLastCalculationMethod().getTitle(),
                            distance
                    ));
                }
                runId++;
                return model;
            }

            private void saveCalculationToDistanceLog(DistanceLog distanceLog) {
                Transaction transaction = null;
                try {
                    SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    transaction = session.beginTransaction();
                    session.save(distanceLog);
                    transaction.commit();
                    session.close();
                } catch (Exception e) {
                    if (transaction != null) transaction.rollback();
                    e.printStackTrace();
                }
            }
        });
    }
}
