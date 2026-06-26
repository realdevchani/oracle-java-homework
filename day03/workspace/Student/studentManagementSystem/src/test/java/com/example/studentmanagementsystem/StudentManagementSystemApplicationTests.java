package com.example.studentmanagementsystem;

import com.example.studentmanagementsystem.domain.StudentVO;
import com.example.studentmanagementsystem.repository.StudentDAO;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ToString
@Slf4j
class StudentManagementSystemApplicationTests {

    @Autowired
    private StudentDAO studentDAO;

	@Test
	void saveTest() {
        StudentVO studentVO = StudentVO.builder()
                .studentName("lee")
                .studentAddress("안산시")
                .studentEmail("chan@naver.com")
                .studentAge(26)
                .studentPhone("010-1234-5678")
                .build();

        studentDAO.save(studentVO);

	}

    @Test
    void updateTest() {
        StudentVO modifyVO = StudentVO.builder()
                .studentId(1L)
                .studentName("modify")
                .studentAddress("수정")
                .studentEmail("update@naver.com")
                .studentAge(16)
                .studentPhone("010-9999-5678")
                .build();

        studentDAO.update(modifyVO);
    }

    @Test
    void deleteTest() {
        studentDAO.delete(1L);
    }

    @Test
    void findByIdTest() {
        if(studentDAO.findById(1L).isPresent()){
            log.info("가져오기 성공 ");
        }else {
            log.info("가져오기 실패 ");
        }
    }

    @Test
    void findAllTest() {
        log.info(studentDAO.findAll().toString());
    }
}
