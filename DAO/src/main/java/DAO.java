import java.sql.SQLException;

public class DAO {
    public static void main(String[] args) throws SQLException {
        User user1 = new User();

        user1.setName("Washington");
        user1.setEmail("wirving3@test.pl");
        user1.setPassword("yosarrian");

        UsersDAO.create(user1);

    }
}
