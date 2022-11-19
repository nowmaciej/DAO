import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id=?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username=?, email=?, password=? WHERE id = ?";

    public static void create(User user) throws SQLException {
        String username = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();

        insert(DbUtil.connect(), CREATE_USER_QUERY, username, email, hashPassword(password));
    }

    private static void insert(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("Inserted new user. Generated ID is: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(long id) {
        try (PreparedStatement statement = DbUtil.connect().prepareStatement(DELETE_USER_QUERY)) {
            statement.setString(1, String.valueOf(id));
            statement.execute();
            System.out.printf("Deleted user with ID = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(long id, User user) {
        String newUsername = user.getName();
        String newEmail = user.getEmail();
        String newPassword = user.getPassword();

        try (PreparedStatement statement = DbUtil.connect().prepareStatement(UPDATE_USER_QUERY)) {
            statement.setString(1, newUsername);
            statement.setString(2, newEmail);
            statement.setString(3, newPassword);
            statement.setString(4, String.valueOf(id));
            statement.executeUpdate();

            System.out.println("Updated user with ID = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String hashPassword(String password) {
        return "hashed " + password;
    }

}

