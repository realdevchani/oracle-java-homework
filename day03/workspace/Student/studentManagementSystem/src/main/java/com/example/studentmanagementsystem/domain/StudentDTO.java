package com.example.studentmanagementsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {

    private String studentName;
    private String studentEmail;
    private String studentPhone;


}
