package com.todoist.todoist.structures;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.controllers.LabelController;
import com.todoist.todoist.controllers.ProjectController;
import com.todoist.todoist.controllers.SectionController;
import com.todoist.todoist.controllers.TaskController;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.pages.DashboardPage;
import com.todoist.todoist.pages.ProjectPage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.HashMap;

public class TodoistApp extends Application {
    public ProjectController projectController;
    public LabelController labelController;
    public SectionController sectionController;
    public TaskController taskController;
    private Panel panel;
    public Page currentPage;
    public HashMap<String, Page> pages;

    public TodoistApp(MongoDatabase db) {
        this.projectController = new ProjectController(db);
        this.labelController = new LabelController(db, this.projectController);
        this.sectionController = new SectionController(db, this.projectController);
        this.taskController = new TaskController(
                db,
                this.projectController,
                this.labelController,
                this.sectionController
        );

        this.pages = new HashMap<>();
    }

    public void loadPages() {
        pages.put("Dashboard", new DashboardPage(this));

        HashMap<ObjectId, Project> projects = projectController.list();
        projects.forEach((id, project) -> {
           pages.put(project.title, new ProjectPage(this, project));
        });
    }

    public void switchPage(String name) {
        currentPage = pages.get(name);
        panel.setBody(currentPage.getContent());
    }

    private void renderButton(VBox vbox, String pageName, Page page) {
        Button button = new Button(pageName);
        button.setStyle("-fx-text-align: center");
        button.getStyleClass().setAll("btn");
        button.setMaxWidth(vbox.getMinWidth());

        button.setOnAction(e -> {
            switchPage(pageName);
        });

        vbox.getChildren().add(button);
    }

    private Pane getSidebar() {
        VBox vbox = new VBox();
        vbox.setMinWidth(200);
        vbox.setAlignment(Pos.TOP_CENTER);
        pages.forEach((pageName, page) -> {
            renderButton(vbox, pageName, page);
        });

//        vbox.setStyle("-fx-background-color: #FF0");

        return vbox;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadPages();
        panel = new Panel("Todoist");
        panel.setLeft(getSidebar());
        panel.getStyleClass().add("panel-primary");

        switchPage("Dashboard");
        currentPage = pages.get("Dashboard");

        Scene scene = new Scene(panel, 1000, 500);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());       //(3)

        primaryStage.setTitle("Todoist");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
