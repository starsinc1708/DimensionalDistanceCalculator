package org.core;

import org.core.points.Point;
import org.core.points.Point2D;
import org.core.points.Point3D;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {

    public static Map<Point, Point> parseXML(String xmlFilePath) throws ParserConfigurationException, IOException, SAXException {
        Map<Point, Point> pointPairs = new HashMap<>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler defaultHandler = new DefaultHandler() {
                private Point point1;
                private Point point2;

                private boolean point1Started = false;
                private boolean point2Started = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equals("pair")) {
                        point1Started = false;
                        point2Started = false;
                    } else if (qName.equals("point")) {
                        String type = attributes.getValue("type");
                        double x = parseDoubleAttribute(attributes, "x");
                        double y = parseDoubleAttribute(attributes, "y");
                        double z = attributes.getValue("z") != null ? parseDoubleAttribute(attributes, "z") : 0.0;

                        if (!point1Started) {
                            point1 = createPoint(type, x, y, z);
                            point1Started = true;
                        } else if (!point2Started) {
                            point2 = createPoint(type, x, y, z);
                            point2Started = true;
                        } else {
                            throw new SAXException("Unexpected element: " + qName);
                        }
                    }
                }

                private double parseDoubleAttribute(Attributes attributes, String attributeName) throws SAXException {
                    String value = attributes.getValue(attributeName);
                    if (value == null) {
                        throw new SAXException("Missing attribute: " + attributeName);
                    }
                    try {
                        return Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        throw new SAXException("Invalid value for attribute " + attributeName + ": " + value);
                    }
                }

                private Point createPoint(String type, double x, double y, double z) throws SAXException {
                    if (type.equals("2D")) {
                        return new Point2D(x, y);
                    } else if (type.equals("3D")) {
                        return new Point3D(x, y, z);
                    } else {
                        throw new SAXException("Invalid point type: " + type);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equals("point") && point1Started && point2Started) {
                        pointPairs.put(point1, point2);
                        point1Started = false;
                        point2Started = false;
                    }
                }
            };
            saxParser.parse(new File(xmlFilePath), defaultHandler);
            return pointPairs;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw e;
        }
    }


}
