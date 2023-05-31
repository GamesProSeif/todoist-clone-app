package com.todoist.todoist;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.todoist.todoist.controllers.ProjectController;
import com.todoist.todoist.models.Project;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import java.io.IOException;

public class TodoistApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Panel panel = new Panel("This is the title");
        panel.getStyleClass().add("panel-primary");
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(20));
        Button button = new Button("Hello BootstrapFX");
        button.getStyleClass().setAll("btn","btn-danger");                     //(2)
        content.setCenter(button);
        panel.setBody(content);

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());       //(3)

        primaryStage.setTitle("BootstrapFX");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        String uri = "mongodb+srv://todoistuser1:RCXjZ65rPeDaxSSn@todoistcluster.dj0rajo.mongodb.net/?retryWrites=true&w=majority";
        try(MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase db = mongoClient.getDatabase("dev");
            System.out.println("Connected to db: " + db.getName());
            ProjectController projectController = new ProjectController(db);
//            Project project = projectController.findById(new ObjectId("647229ea25190a603960de7f"));
//            System.out.println(project);
//            projectController.insert(new Project("no way"));

//            var projects = projectController.list();
//            projects.forEach((k, v) -> {
//                System.out.println(v.title + "\n" + v.title);
//            });

//            projectController.delete(projectController.findById(new ObjectId("647229ea25190a603960de7f")));
            projectController.update(new ObjectId("647233149e919b33027656db"), Updates.set("title", "yes way"));

        }
        launch();
    }
}