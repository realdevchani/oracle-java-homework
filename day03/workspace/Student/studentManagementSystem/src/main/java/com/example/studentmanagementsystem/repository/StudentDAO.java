package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.domain.StudentVO;

import java.util.List;
import java.util.Optional;

public interface StudentDAO {

    void save(StudentVO studentVO);
    void update(StudentVO studentVO);
    void delete(Long studentId);
    Optional<StudentVO> findById(Long studentId);
    List<StudentVO> findAll();

}
