package com.todoist.todoist;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.structures.TodoistApp;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Objects;

public class Program extends Application {
    static TodoistApp app;

    @Override
    public void start(Stage primaryStage) throws Exception {
        app.start(primaryStage);
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        try(MongoClient mongoClient = MongoClients.create(Objects.requireNonNull(dotenv.get("DB_URI")))) {
            MongoDatabase db = mongoClient.getDatabase("dev");
            System.out.println("Connected to db: " + db.getName());

            app = new TodoistApp(db);
            launch();
        }
    }

}