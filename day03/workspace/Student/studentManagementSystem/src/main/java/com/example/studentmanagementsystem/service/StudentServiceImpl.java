package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.domain.StudentDTO;
import com.example.studentmanagementsystem.domain.StudentVO;
import com.example.studentmanagementsystem.repository.StudentDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;

    @Override
    public boolean register(StudentVO studentVO) {
        try {
            studentDAO.save(studentVO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void update(StudentVO studentVO) {
        studentDAO.update(studentVO);
    }

    @Override
    public void delete(Long id) {
        studentDAO.delete(id);
    }

    @Override
    public StudentDTO findById(Long id) {
        StudentVO studentVO = studentDAO.findById(id).orElseThrow(() -> new RuntimeException("학생을 찾지 못함"));
        return studentVO.voToStudentDTO(studentVO);
    }

    @Override
    public List<StudentDTO> findAll() {
        List<StudentVO> list = studentDAO.findAll();

        if (list.isEmpty()) {
            return List.of();
        }

        return list.stream().map(vo -> vo.voToStudentDTO(vo)).toList();
    }
}
