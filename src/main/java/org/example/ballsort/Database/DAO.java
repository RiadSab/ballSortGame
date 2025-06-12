package org.example.ballsort.Database;
import org.example.ballsort.model.GameSession;
import org.example.ballsort.model.Score;
import org.mindrot.jbcrypt.BCrypt;
import org.example.ballsort.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.mindrot.jbcrypt.BCrypt.checkpw;

public class DAO {
    private static final Connection con = DataBaseConnection.getConnection();


    public static void insertSession(GameSession session) {
        //the query doesn't contain the idSession because it's auto increment and also the primary key,
        //it's easy like this to maintain its unicity.
        String query = "INSERT INTO sessions ( idUser, level, won, score, playedAt, duration) VALUES (?,?,?,?,?,?) ";
        PreparedStatement ps = null;
        try {
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
        //the query doesn't contain the idUser because it's auto increment and the primary key too.
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

    public static User selectUser(int id) {
        String query = "SELECT * FROM users WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
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
        // si la table users du Database ne contient pas un enregistrement correspendant au nom entré,
        // l'utilisateur sera null car la methode selectUser(name) a retourné null puisqu'elle n'a rien tourvé
        if(user == null) return false;
        System.out.println(user.getName() + " " + user.getEmail() + " " + user.getPassword());
        String hashedPassword = user.getPassword(); // on récupère le password si le user deja existe.

        // verifie si le password entré est identique à celui hashed in Database et retourne true si vrai.
        // the second parameter is decrypted and then comapred with the password entered.
        return checkpw(password, hashedPassword);
    }

    public static boolean userExists(String name) {
        if(selectUser(name) == null) return false;
        return true;
    }

    public static List<Score> getListScores(int levelSearched) throws SQLException {
        String query = "SELECT * FROM sessions ORDER BY score DESC LIMIT 10";
        List<Score> scores = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(query)
        ){
            int rank = 1;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idSession = rs.getInt("idSession");
                int idUser = rs.getInt("idUser");
                int level = rs.getInt("level");
                int won = rs.getInt("won");
                int score = rs.getInt("score");
                User user = selectUser(idUser);
                System.out.println("user: " + user.getName());
                if(user != null && level == levelSearched) {
                    Score s = new Score(score, user,rank++);
                    scores.add(s);
                }
            }
            return scores;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifySession(GameSession session) {
        // this method delete the record from the table and insert again the correct one

        String queryDelete = "DELETE FROM sessions ORDER BY idSession DESC limit 1";
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
