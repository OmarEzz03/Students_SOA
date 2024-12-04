package org.springframework.boot.studentsv2.service;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
// import javax.xml.transform.TransformerFactory;
// import javax.xml.transform.dom.DOMSource;
// import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.springframework.boot.studentsv2.model.Student;
// import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.lang.reflect.Field;

public class StudentManager {

    public StudentManager() {}

    public void deleteStudent(Document document, File xmlFile, String ID) throws Exception {
        Node resultNode = searchDocument("ID", ID, document);
        if (resultNode != null) {
            Element element = (Element) resultNode;
            element.getParentNode().removeChild(resultNode);
            saveStudents(document, xmlFile);
            return;
        }
        throw new Exception("Student with ID " + ID + " not found.");
    }

    private static void addChild(Document document, Element parent, String tagName, String textElemnt) {
        Element element = document.createElement(tagName);
        element.appendChild(document.createTextNode(textElemnt));
        parent.appendChild(element);
    }

    private static void saveStudents(Document document, File XmFile) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(XmFile);

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudents(Document document, File xmlFile, Student student) throws Exception {
        Element studentElement = document.createElement("Student");
        for (Field field : student.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if ((field.get(student).equals( null))) {
                throw new Exception(field.getName() + " is Null");
            }
            if ((field.getType().equals(String.class)) && (field.get(student).equals(""))) {
                throw new Exception(field.getName() + " is Empty");
            }
        }
        if (!student.getFirstName().matches("^[a-zA-Z]+$")) {
            throw new Exception("FirstName should have characters only");
        }
        if (!student.getLastName().matches("^[a-zA-Z]+$")) {
            throw new Exception("LastName should have characters only");
        }
        if (searchDocument("ID", student.getId().toString(), document) != null) {
            throw new Exception("Student with ID " + student.getId() + " already exists.");
        }

        if (!student.getAddress().matches("^[a-zA-Z]+$")) {
            throw new Exception("Address should have characters only");
        }

        if ((student.getGpa() < 0)||(student.getGpa() > 4)) {
            throw new Exception("GPA should be in range from 0 to 4");
        }
        
        studentElement.setAttribute("ID", student.getId().toString());
        addChild(document, studentElement, "FirstName", student.getFirstName());
        addChild(document, studentElement, "LastName", student.getLastName());
        addChild(document, studentElement, "Gender", student.getGender());
        addChild(document, studentElement, "GPA", student.getGpa().toString());
        addChild(document, studentElement, "Level", student.getLevel().toString());
        addChild(document, studentElement, "Address", student.getAddress());
        document.getDocumentElement().appendChild(studentElement);

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("Student");
        ArrayList<Element> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            students.add((Element) nodeList.item(i));
        }

        students.sort(Comparator.comparingInt(e -> Integer.parseInt(e.getAttribute("ID"))));

        while (root.hasChildNodes()) {
            root.removeChild(root.getFirstChild());
        }

        for (Element s : students) {
            root.appendChild(s);
        }
        
        saveStudents(document, xmlFile);
    }

    public Node searchDocument(String searchParam, String searchParamValue, Document document) {
        Node resultNode = null;
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                if (searchParam.equals("ID"))
                {
                    String ID = element.getAttributes().getNamedItem("ID").getNodeValue();
                    if (ID.equals(searchParamValue))
                    {
                        resultNode = node;
                        break;
                    }
                }
                else if(searchParam.equals("FirstName"))
                {
                    String FirstName = element.getElementsByTagName("FirstName").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                    if (FirstName.equals(searchParamValue.toLowerCase()))
                    {
                        resultNode = node;
                        break;
                    }
                }
            }
        }
        return resultNode;
    } 

}
