import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbCheck {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/relationship_social_network?options=-c%20timezone=UTC";
        String user = "social_user";
        String password = "social_password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            
            System.out.println("--- USERS ---");
            ResultSet rs = stmt.executeQuery("SELECT id, username, email FROM users");
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("id") + ", Username: " + rs.getString("username") + ", Email: " + rs.getString("email"));
            }

            System.out.println("\n--- FRIENDSHIPS ---");
            ResultSet frs = stmt.executeQuery("SELECT id, requester_id, receiver_id, status FROM friendships");
            while (frs.next()) {
                System.out.println("ID: " + frs.getLong("id") + ", Requester: " + frs.getLong("requester_id") + ", Receiver: " + frs.getLong("receiver_id") + ", Status: " + frs.getString("status"));
            }
        }
    }
}
