package org.example.ballsort.Database;
import org.example.ballsort.model.gameSession;
import org.mindrot.jbcrypt.BCrypt;
import org.example.ballsort.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mindrot.jbcrypt.BCrypt.checkpw;

public class DAO {
    private static final Connection con = DataBaseConnection.getConnection();


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

    public static void insertSession(gameSession session) {
        String query = "INSERT INTO sessions (idSession, idUser, level, won, score, playedAt, duration) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setInt(1, session.getIdSession());
            ps.setInt(2, session.getIdUser());
            ps.setInt(3, session.getLevel());
            ps.setBoolean(4, session.isWon());
            ps.setInt(5, session.getScore());
            ps.setDate(6, session.getPlayedAt());
            ps.setInt(7, session.getDuration());
            ps.executeUpdate();
            ps.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
