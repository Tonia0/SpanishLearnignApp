package DataScrape;

import DataScrape.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseScraper {

    private static final String URL = "jdbc:postgresql://localhost:5432/users";
    private static final String USER = "access_user";
    private static final String PASSWORD = "user123";

    public static void saveArticles(List<Article> articles) {
        String sql = "INSERT INTO articles (title, content) VALUES (?, ?)";
        try (Connection connect = DriverManager.getConnection(URL, USER, PASSWORD);

             /**
              * For testing the database
              */
             PreparedStatement ps = connect.prepareStatement(sql)) {
            System.out.println("Connected to DB!");
            for (Article a : articles) {
                ps.setString(1, a.getTitle());
                ps.setString(2, a.getContent());
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("Articles saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        List<Article> test = List.of(
                new Article("Test Title", "Test content")
        );
        saveArticles(test);
    }

}
