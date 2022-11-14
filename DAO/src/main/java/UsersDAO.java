import java.sql.SQLException;

public class UsersDAO {
    public static User create(User user) {
        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        int id;

        try {
            DbUtil.insert(DbUtil.connect(),
                    "INSERT INTO users (name, email, password) VALUES (?, ?, ?)",
                    name, email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            id = Integer.parseInt(DbUtil.singleValue(DbUtil.connect(),
                    "SELECT id FROM users ORDER BY id DESC LIMIT 1"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User createdUser = new User();
        createdUser.setId(id);
        createdUser.setName(name);
        createdUser.setPassword(password);
        createdUser.setEmail(email);


        return createdUser;
    }
}
