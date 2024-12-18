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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import java.util.Comparator;


public class StudentManager {

    public StudentManager() {}

    public void deleteStudent(Document document, File xmlFile, String ID) throws Exception {
        List<Node> resultNodes = searchDocument("id", ID, document);
        if (!resultNodes.isEmpty()) {
            Node resultNode = resultNodes.get(0);
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

    private void updateChildNode(Element parentElement, String tagName, String newValue) {
        if (newValue != null) {
            NodeList nodes = parentElement.getElementsByTagName(tagName);
            if (nodes.getLength() > 0) {
                nodes.item(0).setTextContent(newValue);
            }
        }
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
        if (!searchDocument("ID", student.getId().toString(), document).isEmpty()) {

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

    public List<Node> searchDocument(String searchParam, String searchParamValue, Document document) {
        List<Node> resultNodes = new ArrayList<Node>();
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                switch (searchParam.toLowerCase()) {
                    case "id":
                        String ID = element.getAttributes().getNamedItem("ID").getNodeValue();
                        if (ID.equals(searchParamValue))
                        {
                            resultNodes.add(node);
                        }
                        break;
                    case "firstname":
                        String FirstName = element.getElementsByTagName("FirstName").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                        if (FirstName.equals(searchParamValue.toLowerCase()))
                        {
                            resultNodes.add(node);
                        }
                        break;
                    case "lastname":
                        String LastName = element.getElementsByTagName("LastName").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                        if (LastName.equals(searchParamValue.toLowerCase()))
                        {
                            resultNodes.add(node);
                        }
                        break;
                    case "gender":
                        String Gender = element.getElementsByTagName("Gender").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                        if (Gender.equals(searchParamValue.toLowerCase()))
                        {
                            resultNodes.add(node);
                        }
                        break;
                    case "address":
                        String Address = element.getElementsByTagName("Address").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                        if (Address.equals(searchParamValue.toLowerCase()))
                        {
                            resultNodes.add(node);
                        }
                        break;
                    case "gpa":
                        String GPA = element.getElementsByTagName("GPA").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                        if (GPA.equals(searchParamValue.toLowerCase()))
                        {
                            resultNodes.add(node);
                        }
                        break;
                    case "level":
                        String Level = element.getElementsByTagName("Level").item(0).getChildNodes().item(0).getNodeValue().toLowerCase();
                        if (Level.equals(searchParamValue.toLowerCase()))
                        {
                            resultNodes.add(node);
                        }
                        break;
                }
            }
        }
        return resultNodes;
    } 

    public void updateStudent(Document document, File xmlFile, Long id, Student student) throws Exception {
        List<Node> resultNodes = searchDocument("id", id.toString(), document); 

        if (student.getId() != null) {
            throw new Exception("Student ID can't be updated.");
        }

        if (resultNodes.isEmpty()) {
            throw new Exception("Student with ID " + id + " not found.");
        }

        Node resultNode = resultNodes.get(0); 
        Element element = (Element) resultNode;
        updateChildNode(element, "FirstName", student.getFirstName());
        updateChildNode(element, "LastName", student.getLastName());
        updateChildNode(element, "Gender", student.getGender());
        updateChildNode(element, "GPA", student.getGpa() != null ? student.getGpa().toString() : null);
        updateChildNode(element, "Level", student.getLevel() != null ? student.getLevel().toString() : null);
        updateChildNode(element, "Address", student.getAddress());

        saveStudents(document, xmlFile);
    }

    public ArrayList<Element> sortStudents(Document document, File xmlFile, String sortBy, String order) {
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("Student");
        ArrayList<Element> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            students.add((Element) nodeList.item(i));
        }

        Comparator<Element> comparator;
        switch (sortBy.toLowerCase()) {
            case "firstname":
                comparator = Comparator.comparing(e -> e.getElementsByTagName("FirstName").item(0).getTextContent());
                break;
            case "lastname":
                comparator = Comparator.comparing(e -> e.getElementsByTagName("LastName").item(0).getTextContent());
                break;
            case "gpa":
                comparator = Comparator.comparingDouble(e -> Double.parseDouble(e.getElementsByTagName("GPA").item(0).getTextContent()));
                break;
            case "level":
                comparator = Comparator.comparingInt(e -> Integer.parseInt(e.getElementsByTagName("Level").item(0).getTextContent()));
                break;
            case "address":
                comparator = Comparator.comparing(e -> e.getElementsByTagName("Address").item(0).getTextContent());
                break;
            default: // default to sorting by ID
                comparator = Comparator.comparingInt(e -> Integer.parseInt(e.getAttribute("ID")));
                break;
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        students.sort(comparator);
        while (root.hasChildNodes()) {
            root.removeChild(root.getFirstChild());
        }

        for (Element s : students) {
            root.appendChild(s);
        }
        saveStudents(document, xmlFile);
        return students;
    }   
}
