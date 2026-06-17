package com.example.converter;

import com.example.Spring_boot_demo.dao.Student;
import com.example.Spring_boot_demo.dto.StudentDTO;

public class StudentConverter {

    public static StudentDTO convertStudent(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setAge(student.getAge());
        studentDTO.setGender(student.getGender());
        studentDTO.setHobby(student.getHobby());
        studentDTO.setGrade(student.getGrade());
        return studentDTO;
    }

    public static Student convertStudentDTO(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        if (studentDTO.getAge() != null) {
            student.setAge(studentDTO.getAge());
        }
        student.setGender(studentDTO.getGender());
        student.setHobby(studentDTO.getHobby());
        student.setGrade(studentDTO.getGrade());
        return student;
    }
}
