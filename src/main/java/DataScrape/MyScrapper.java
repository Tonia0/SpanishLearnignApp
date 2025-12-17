package DataScrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyScrapper {

    public static List<Article> scraper(int limit) {
        List<Article> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://elpais.com/")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements articlesList = doc.select("article.c.c-d.c--m");
            int count = 0;

            for (Element article : articlesList) {
                if (count >= limit) break;

                Element link = article.selectFirst("h2.c_t > a");
                if (link == null) continue;

                String url = link.absUrl("href");
                String title = link.text();

                if (url.isEmpty()) continue;

                try {
                    Document articleDoc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0")
                            .timeout(10000)
                            .get();

                    Elements paragraphs = articleDoc.select("article p");
                    StringBuilder text = new StringBuilder();
                    for (Element p : paragraphs) {
                        text.append(p.text()).append("\n\n");
                    }

                    if (!title.isEmpty() && text.length() > 0) {
                        articles.add(new Article(title, text.toString()));
                        count++;
                        System.out.println("Scraped: " + title);
                    }

                } catch (IOException e) {
                    System.out.println("Failed to pull article: " + url);
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to connect to homepage");
            e.printStackTrace();
        }

        return articles;
    }
}
