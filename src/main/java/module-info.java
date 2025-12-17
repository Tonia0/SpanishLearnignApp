module org.andrei.sample_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // important from here

    requires org.postgresql.jdbc;
    requires java.sql;
    requires org.jsoup;
    requires java.desktop;

    exports org.andrei.sample_project;

    opens org.andrei.sample_project.model to javafx.base;
    exports org.andrei.sample_project.model;

    opens org.andrei.sample_project.connection to java.sql;
    exports org.andrei.sample_project.connection;

    opens org.andrei.sample_project.repository to java.sql;
    exports org.andrei.sample_project.repository;
    opens org.andrei.sample_project to java.sql, javafx.fxml;

    opens DataScrape to javafx.fxml;
}