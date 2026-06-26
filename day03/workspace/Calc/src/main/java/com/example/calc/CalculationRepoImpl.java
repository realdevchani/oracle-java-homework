package com.example.calc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CalculationRepoImpl implements CalculationRepo {

    private final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
    private final String USER = "scott";
    private final String PASSWORD = "tiger";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private Calc resultSetToCalc(ResultSet rs) throws SQLException {
        return Calc.builder()
                .calcId(rs.getLong("CALC_ID"))
                .calcNumber1(rs.getInt("CALC_NUM1"))
                .calcNumber2(rs.getInt("CALC_NUM2"))
                .calcOperator(rs.getString("CALC_OPERATOR"))
                .calcResult(rs.getInt("CALC_RESULT"))
                .build();
    }

    @Override
    public Optional<Calc> findById(String id) {
        String query = """
                SELECT CALC_ID, CALC_NUM1, CALC_NUM2, CALC_OPERATOR, CALC_RESULT
                FROM TBL_CALC
                WHERE CALC_ID = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, Long.parseLong(id));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(resultSetToCalc(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById 중 예외 발생", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Calc> findAll() {
        String query = """
                SELECT CALC_ID, CALC_NUM1, CALC_NUM2, CALC_OPERATOR, CALC_RESULT
                FROM TBL_CALC
                ORDER BY CALC_ID
                """;

        List<Calc> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(resultSetToCalc(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findAll 중 예외 발생", e);
        }

        return list;
    }

    @Override
    public void save(Calc calc) {
        String query = """
                INSERT INTO TBL_CALC(
                    CALC_ID,
                    CALC_NUM1,
                    CALC_NUM2,
                    CALC_OPERATOR,
                    CALC_RESULT
                )
                VALUES (SEQ_CALC.NEXTVAL, ?, ?, ?, ?)
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, calc.getCalcNumber1());
            ps.setInt(2, calc.getCalcNumber2());
            ps.setString(3, calc.getCalcOperator());
            ps.setInt(4, calc.getCalcResult());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save 중 예외 발생", e);
        }
    }

    @Override
    public Optional<Calc> findLastCalculate() {
        String query = """
                SELECT CALC_ID, CALC_NUM1, CALC_NUM2, CALC_OPERATOR, CALC_RESULT
                FROM TBL_CALC
                ORDER BY CALC_ID DESC
                OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return Optional.of(resultSetToCalc(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findLastCalculate 중 예외 발생", e);
        }

        return Optional.empty();
    }
}
