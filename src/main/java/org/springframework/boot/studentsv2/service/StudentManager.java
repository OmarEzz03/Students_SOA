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
        if (searchDocument("ID", student.getId().toString(), document) != null) {
            throw new Exception("Student with ID " + student.getId() + " already exists.");
        }
        studentElement.setAttribute("ID", student.getId().toString());
        addChild(document, studentElement, "FirstName", student.getFirstName());
        addChild(document, studentElement, "LastName", student.getLastName());
        addChild(document, studentElement, "Gender", student.getGender());
        addChild(document, studentElement, "GPA", student.getGpa().toString());
        addChild(document, studentElement, "Level", student.getLevel().toString());
        addChild(document, studentElement, "Address", student.getAddress());
        document.getDocumentElement().appendChild(studentElement);
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
                if (searchParam.equals("id"))
                {
                    String ID = element.getAttributes().getNamedItem("ID").getNodeValue();
                    if (ID.equals(searchParamValue))
                    {
                        resultNode = node;
                        break;
                    }
                }
                else if(searchParam.equals("firstname"))
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

    public void updateStudent(Document document, File xmlFile, Long id, Student student) throws Exception {
        Node resultNode = searchDocument("ID", id.toString(), document); 
        if (resultNode == null) {
            throw new Exception("Student with ID " + student.getId() + " not found.");
        }
        if (student.getId() != null) {
            throw new Exception("Student ID can't be updated.");
        }
        Element element = (Element) resultNode;
        updateChildNode(element, "FirstName", student.getFirstName());
        updateChildNode(element, "LastName", student.getLastName());
        updateChildNode(element, "Gender", student.getGender());
        updateChildNode(element, "GPA", student.getGpa() != null ? student.getGpa().toString() : null);
        updateChildNode(element, "Level", student.getLevel() != null ? student.getLevel().toString() : null);
        updateChildNode(element, "Address", student.getAddress());

        saveStudents(document, xmlFile);
    }
}
