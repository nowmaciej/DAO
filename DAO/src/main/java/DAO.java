import org.w3c.dom.ls.LSOutput;

import java.sql.SQLException;

public class DAO {
    public static void main(String[] args) throws SQLException {
//        User user1 = new User();//
//        user1.setName("Washington");
//        user1.setEmail("randommail@test.pl");
//        user1.setPassword("yosarrian");

//        UsersDAO.create(user1);
//        UsersDAO.read(10);
//        UsersDAO.delete(10);
//        UsersDAO.update(10,user1);

        UsersDAO userConnector = new UsersDAO();

        for (User u:userConnector.readAll()) {
            System.out.println(u.toString());
        }

    }
}
