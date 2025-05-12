package org.example.ballsort.Database;
import org.example.ballsort.model.GameSession;
import org.mindrot.jbcrypt.BCrypt;
import org.example.ballsort.model.User;

import java.sql.*;

import static org.mindrot.jbcrypt.BCrypt.checkpw;

public class DAO {
    private static final Connection con = DataBaseConnection.getConnection();


    public static void insertSession(GameSession session) {

        String query = "INSERT INTO sessions ( idUser, level, won, score, playedAt, duration) VALUES (?,?,?,?,?,?) ";
        PreparedStatement ps = null;
        try { // 	idSession	idUser	level	won	score	playedAt	duration
            ps = con.prepareStatement(query);
            ps.setInt(1, session.getIdUser());
            ps.setInt(2, session.getLevel());
            ps.setBoolean(3, session.isWon());
            ps.setInt(4, session.getScore());
            ps.setTimestamp(5, Timestamp.valueOf(session.getPlayedAt()));
            ps.setInt(6, session.getDuration());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void insertUser(User user) {
        String query = "INSERT INTO users (name, email, hashed_password) VALUES (?, ?, ?)";
        try {
            System.out.println("Inserting user: " + user.getName());
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User selectUser(String name) {
        String query = "SELECT * FROM users WHERE name = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String hashedPassword = rs.getString("hashed_password");
                return new User(id, name, email, hashedPassword);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    public static boolean checkLogin(String name, String password) {

        User user = selectUser(name);
        if(user == null) return false;
        System.out.println(user.getName() + " " + user.getEmail() + " " + user.getPassword());
        String hashedPassword = user.getPassword();
        if(hashedPassword == null) return false;
        return checkpw(password, hashedPassword);
    }

    public static boolean userExists(String name) {
        if(selectUser(name) == null) return false;
        return true;
    }

    public static void modifySession(GameSession session) {
        String queryDelete = "DELETE FROM sessions ORDER BY idSession DESC limit 1";
//        String queryUpdate = "UPDATE "
        try {
            PreparedStatement ps = con.prepareStatement(queryDelete);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        insertSession(session);
    }
}
