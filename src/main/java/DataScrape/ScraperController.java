package DataScrape;

import javafx.concurrent.Task;
import javafx.fxml.FXML;


import java.io.IOException;
import java.util.List;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class ScraperController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentField;

    private int index=0;

    private List<Article> articles;

    @FXML
    public void initialize()  {
        Task<List<Article>> scrapeTask = new Task<>() {
            @Override
            protected List<Article> call() {
                return MyScrapper.scraper(3);
            }
        };

        scrapeTask.setOnSucceeded(event -> {
            articles = scrapeTask.getValue();

            // <-- Debug prints to see what was scraped
            System.out.println("Articles scraped: " + articles.size());
            for (Article a : articles) {
                System.out.println("Title: " + a.getTitle());
                System.out.println("Content length: " + a.getContent().length());
            }

            // Save to DB only if there are articles
            if (articles != null && !articles.isEmpty()) {
                DatabaseScraper.saveArticles(articles);
                titleField.setText(articles.get(0).getTitle());
                contentField.setText(articles.get(0).getContent());
            } else {
                titleField.setText("No articles found");
                contentField.setText("No articles found");
            }
        });

        scrapeTask.setOnFailed(event -> {
            scrapeTask.getException().printStackTrace();
        });

        new Thread(scrapeTask).start();
    }

}
