package org.springframework.boot.studentsv2.model;

public class Student {
    private String ID;
    private String FirstName;
    private String LastName;
    private String Gender;
    private Double GPA;
    private Integer Level;
    private String Address;

    public String getId() {
        return ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getGender() {
        return Gender;
    }

    public Double getGpa() {
        return GPA;
    }
    
    public Integer getLevel() {
        return Level;
    }

    public String getAddress() {
        return Address;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setGPA(Double gPA) {
        GPA = gPA;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Student() {}

    public Student(String iD, String firstName, String lastName, String gender, Double gPA, Integer level, String address) {
        ID = iD;
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
        GPA = gPA;
        Level = level;
        Address = address;
    }
    @Override
    public String toString() {
        return "Student [ID=" + ID + ", FirstName=" + FirstName + ", LastName=" + LastName + ", Gender=" + Gender
                + ", GPA=" + GPA + ", Level=" + Level + ", Address=" + Address + "]";
    }
}
