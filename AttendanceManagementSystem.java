package as;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void markAttendance(int studentId, String date, boolean isPresent) {
        String query = "INSERT INTO attendance (student_id, date, is_present) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, date);
            stmt.setBoolean(3, isPresent);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAttendance(int studentId) {
        List<String> records = new ArrayList<>();
        String query = "SELECT date, is_present FROM attendance WHERE student_id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.add(rs.getString("date") + " - " + (rs.getBoolean("is_present") ? "Present" : "Absent"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void main(String[] args) {
        AttendanceManagementSystem system = new AttendanceManagementSystem();
        system.markAttendance(1, "2025-02-25", true);
        List<String> attendanceRecords = system.getAttendance(1);
        for (String record : attendanceRecords) {
            System.out.println(record);
        }
    }
}