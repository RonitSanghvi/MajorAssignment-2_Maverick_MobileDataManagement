package com.example.majorassignment_2_studentinfo;

public class StudentModel {

    private String name;
    private String number;
    private String email;
    private String course;

    // Constructor
    public StudentModel(String name, String number, String email, String course) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.course = course;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nStudent No: " + number + "\nEmail: " + email + "\nCourse Code: " + course;
    }

    public String getEmail() { return email;}

}