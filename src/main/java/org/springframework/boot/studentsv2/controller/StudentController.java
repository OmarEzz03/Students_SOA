package org.springframework.boot.studentsv2.controller;

import org.springframework.boot.studentsv2.model.Student;
import org.springframework.boot.studentsv2.service.StudentManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.w3c.dom.Document;
import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// import javax.xml.transform.TransformerFactory;
// import javax.xml.transform.dom.DOMSource;
// import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

@RestController
@RequestMapping("/students")
public class StudentController {
    private static String file_path = "D:\\CS\\4th_year\\1st_semester\\SOA\\studentsv2\\src\\main\\resources\\university.xml";
    private static StudentManager studentManager;

    public StudentController() {
        studentManager = new StudentManager();
    }

    private Document getDocument(File xmlFile) throws Exception {
        try{
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document;
            
            if (xmlFile.exists()) {
                document = documentBuilder.parse(xmlFile);
            } else {
                document = documentBuilder.newDocument();
                Element rootElement = document.createElement("university");
                document.appendChild(rootElement);
            }
            return document;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/add")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        try {
            File xmlFile = new File(file_path);
            Document document = getDocument(xmlFile);
            studentManager.addStudents(document, xmlFile, student);
            return ResponseEntity.ok("Student with ID " + student.getId() + " has been added.\n");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding student: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Student> searchStudents(@RequestParam String searchParam, @RequestParam String value) {
        try {
            File xmlFile = new File(file_path);
            Document document = getDocument(xmlFile);
            Node resultNode = studentManager.searchDocument(searchParam, value, document);
            if (resultNode != null) {
                Element element = (Element) resultNode;
                Student student = new Student(element.getAttribute("ID"),
                                             element.getElementsByTagName("FirstName").item(0).getTextContent(),
                                             element.getElementsByTagName("LastName").item(0).getTextContent(),
                                             element.getElementsByTagName("Gender").item(0).getTextContent(),
                                             Double.parseDouble(element.getElementsByTagName("GPA").item(0).getTextContent()),
                                             Integer.parseInt(element.getElementsByTagName("Level").item(0).getTextContent()),
                                             element.getElementsByTagName("Address").item(0).getTextContent());
                return ResponseEntity.ok(student);
            } else {
                return ResponseEntity.notFound( ).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Student());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteStudent(@RequestParam String id) {
        try {
            File xmlFile = new File(file_path);
            Document document = getDocument(xmlFile);
            studentManager.deleteStudent(document, xmlFile, id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting student: " + e.getMessage());
        }
    }
}
