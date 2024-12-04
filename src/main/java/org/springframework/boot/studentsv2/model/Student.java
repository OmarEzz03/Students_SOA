package org.springframework.boot.studentsv2.model;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private Double gpa;
    private Integer level;
    private String address;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Double getGpa() {
        return gpa;
    }
    
    public Integer getLevel() {
        return level;
    }

    public String getAddress() {
        return address;
    }


    public void setID(String _iD) {
        id = _iD;
    }

    public void setFirstName(String _firstName) {
        firstName = _firstName;
    }

    public void setLastName(String _lastName) {
        lastName = _lastName;
    }

    public void setGender(String _gender) {
        gender = _gender;
    }

    public void setGPA(Double _gPA) {
        gpa = _gPA;
    }

    public void setLevel(Integer _level) {
        level = _level;
    }

    public void setAddress(String _address) {
        address = _address;
    }

    public Student() {}

    public Student(String _iD, String _firstName, String _lastName, String _gender, Double _gPA, Integer _level, String _address) {
        id = _iD;
        firstName = _firstName;
        lastName = _lastName;
        gender = _gender;
        gpa = _gPA;
        level = _level;
        address = _address;
    }
    @Override
    public String toString() {
        return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
                + ", gpa=" + gpa + ", level=" + level + ", address=" + address + "]";
    }

}
