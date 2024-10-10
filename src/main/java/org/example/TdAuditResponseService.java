package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TdAuditResponseService {

    public void save(TdAuditResponse auditResponse) {
        String insertSQL = "INSERT INTO td_audit_response (user_id, request_id, harassment, selfharm) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setLong(1, auditResponse.getUserId());
            preparedStatement.setString(2, String.valueOf(auditResponse.getRequestId()));
            preparedStatement.setInt(3, auditResponse.getHarassment());
            preparedStatement.setInt(4, auditResponse.getSelfHarm());

            preparedStatement.executeUpdate();
            System.out.println("AuditResponse saved to PostgreSQL");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
