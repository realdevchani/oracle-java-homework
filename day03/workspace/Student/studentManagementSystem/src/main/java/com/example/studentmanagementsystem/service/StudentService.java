package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.domain.StudentDTO;
import com.example.studentmanagementsystem.domain.StudentVO;
import com.example.studentmanagementsystem.repository.StudentDAO;

import java.util.List;

public interface StudentService {

    boolean register(StudentVO studentVO);
    void update(StudentVO studentVO);
    void delete(Long id);
    StudentDTO findById(Long id);
    List<StudentDTO> findAll();

}
