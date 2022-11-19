import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.mindrot.jbcrypt.BCrypt.*;

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
            statement.setString(3, hashPassword(newPassword));
            statement.setString(4, String.valueOf(id));
            statement.executeUpdate();

            System.out.println("Updated user with ID = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User read(int id) {
        String query = "SELECT * FROM users WHERE id=?";
        try (PreparedStatement statement = DbUtil.connect().prepareStatement(query)) {
            statement.setString(1, String.valueOf(id));
            ResultSet result = statement.executeQuery();

            User user = new User();

            if (result.next()) {
                user.setId(result.getInt("id"));
                user.setName(result.getString("username"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
            }
            return user;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static User[] readAll() {
        User[] usersArray = new User[0];
        String query = "SELECT * FROM users";
        try (PreparedStatement statement = DbUtil.connect().prepareStatement(query)) {
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setName(result.getString("username"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));

                usersArray = addToArray(usersArray, user);
            }
            return usersArray;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private static User[] addToArray(User[] oldArray, User user) {
        oldArray = Arrays.copyOf(oldArray, oldArray.length+1);
        oldArray[oldArray.length-1] = user;
        return oldArray;
    }
    private static String hashPassword(String password) {
        return hashpw(password, gensalt());
    }

}

