public class DAO {
    public static void main(String[] args) {
        User user1 = new User();

        user1.setName("Maciej");
        user1.setEmail("maciej@test.pl");
        user1.setPassword("haslomaciej");

        System.out.println(UsersDAO.create(user1).toString());

    }
}
