package com.example.studentmanagementsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StudentVO {

    private Long studentId;
    private String studentName;
    private Integer studentAge;
    private String studentAddress;
    private String studentEmail;
    private String studentPhone;

    public StudentDTO voToStudentDTO(StudentVO studentVO){
        StudentDTO studentDTO = StudentDTO.builder()
                .studentEmail(studentVO.getStudentEmail())
                .studentPhone(studentVO.getStudentPhone())
                .studentName(studentVO.getStudentName())
                .build();
        return studentDTO;
    }

}
