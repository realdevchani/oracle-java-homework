package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.domain.StudentVO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDAOImpl implements StudentDAO {


    private final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
    private final String USER = "scott";
    private final String PASSWORD = "tiger";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    private StudentVO resultSetToStudent(ResultSet rs) throws SQLException {
        return StudentVO.builder()
                .studentId(rs.getLong("STUDENT_ID"))
                .studentName(rs.getString("STUDENT_NAME"))
                .studentPhone(rs.getString("STUDENT_PHONE"))
                .studentEmail(rs.getString("STUDENT_EMAIL"))
                .studentAddress(rs.getString("STUDENT_ADDRESS"))
                .studentAge(rs.getInt("STUDENT_AGE"))
                .build();
    }

    @Override
    public void save(StudentVO studentVO) {
        String query = """
                INSERT INTO TBL_STUDENT(STUDENT_ID, STUDENT_NAME, STUDENT_EMAIL, STUDENT_PHONE, STUDENT_AGE, STUDENT_ADDRESS)
                VALUES(SEQ_STUDENT.NEXTVAL, ?, ?, ?, ?, ?);
                """;
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, studentVO.getStudentName());
            ps.setString(2, studentVO.getStudentEmail());
            ps.setString(3, studentVO.getStudentPhone());
            ps.setInt(4, studentVO.getStudentAge());
            ps.setString(5, studentVO.getStudentAddress());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save 중 예외 발생", e);
        }
    }

    @Override
    public void update(StudentVO studentVO) {
        String query = """
                UPDATE TBL_STUDENT
                SET TBL_STUDENT.STUDENT_NAME = ?,
                    TBL_STUDENT.STUDENT_AGE = ?,
                    TBL_STUDENT.STUDENT_ADDRESS = ?,
                    TBL_STUDENT.STUDENT_EMAIL = ?,
                    TBL_STUDENT.STUDENT_PHONE = ?
                WHERE STUDENT_ID = ? 
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, studentVO.getStudentName());
            ps.setInt(2, studentVO.getStudentAge());
            ps.setString(3, studentVO.getStudentAddress());
            ps.setString(3, studentVO.getStudentEmail());
            ps.setString(4, studentVO.getStudentPhone());
            ps.setString(5, studentVO.getStudentAddress());

            ps.setLong(6, studentVO.getStudentId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("update 중 예외 발생", e);
        }
    }

    @Override
    public void delete(Long studentId) {
        String query = """
                DELETE TBL_STUDENT
                WHERE STUDENT_ID = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, studentId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("delete 중 예외 발생", e);
        }
    }

    @Override
    public Optional<StudentVO> findById(Long studentId) {
        String query = """
                SELECT STUDENT_ID, STUDENT_NAME, STUDENT_PHONE, STUDENT_EMAIL,STUDENT_ADDRESS, STUDENT_AGE
                FROM TBL_STUDENT
                WHERE STUDENT_ID = ?
                """;

        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)
        ) {
            ps.setLong(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(resultSetToStudent(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("findById 중 예외 발생", e);
        }

        return Optional.empty();
    }

    @Override
    public List<StudentVO> findAll() {
        String query = """
                SELECT STUDENT_ID, STUDENT_NAME, STUDENT_PHONE, STUDENT_EMAIL, STUDENT_ADDRESS, STUDENT_AGE
                FROM TBL_STUDENT
                ORDER BY STUDENT_ID
                """;

        List<StudentVO> list = new ArrayList<StudentVO>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(resultSetToStudent(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll 중 예외 발생", e);
        }

        return list;
    }
}
