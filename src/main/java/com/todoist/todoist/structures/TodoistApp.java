package com.todoist.todoist.structures;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.controllers.TagController;
import com.todoist.todoist.controllers.ProjectController;
import com.todoist.todoist.controllers.SectionController;
import com.todoist.todoist.controllers.TaskController;
import com.todoist.todoist.modals.AddProjectModal;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Task;
import com.todoist.todoist.pages.DashboardPage;
import com.todoist.todoist.pages.ProjectPage;
import com.todoist.todoist.pages.TaskPage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class TodoistApp extends Application {
    public Stage stage;
    public ProjectController projectController;
    public TagController tagController;
    public SectionController sectionController;
    public TaskController taskController;
    public Panel panel;
    public Page currentPage;
    public HashMap<String, Page> pages;

    public TodoistApp(MongoDatabase db) {
        this.projectController = new ProjectController(db);
        this.tagController = new TagController(db, this.projectController);
        this.sectionController = new SectionController(db, this.projectController);
        this.taskController = new TaskController(
                db,
                this.projectController,
                this.tagController,
                this.sectionController
        );

        this.pages = new HashMap<>();
    }

    public void loadPages() {
        pages.clear();
        pages.put("Dashboard", new DashboardPage(this));

        HashMap<ObjectId, Project> projects = projectController.list();
        projects.forEach((id, project) -> {
           pages.put("project:" + project.id, new ProjectPage(this, project));
        });

        HashMap<ObjectId, Task> tasks = taskController.list();
        tasks.forEach((id, task) -> {
            pages.put("task:" + task.id, new TaskPage(this, task));
        });

    }

    public void switchPage(String name) {
        currentPage = pages.get(name);
        panel.setBody(currentPage.getContent());
    }

    private void renderButton(VBox vbox, String pageName, EventHandler<ActionEvent> e) {
        Page page = pages.get(pageName);
        if (page instanceof ProjectPage)
            pageName = ((ProjectPage) page).project.title;
        Button button = new Button(pageName);
        button.setStyle("-fx-text-align: center");
        if(!pageName.equals("+ New Project")) {
            button.setPadding(new Insets(0, 0, 0, 15));
            button.getStyleClass().setAll("h5");
        } else {
            button.getStyleClass().setAll("h5", "lbl-primary");
            button.setAlignment(Pos.CENTER);
            button.setMinHeight(30);
            button.setStyle("-fx-text-fill: white");
        }
        button.setMaxWidth(vbox.getMinWidth());

        button.setOnAction(e);

        vbox.getChildren().add(button);
    }

    private Pane getSidebar() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 0, 0, 0));
        vbox.setMinWidth(200);
        vbox.setAlignment(Pos.TOP_CENTER);
        pages.forEach((pageName, page) -> {
            if (page instanceof ProjectPage || page instanceof TaskPage)
                return;
            renderButton(
                    vbox,
                    pageName,
                    e -> switchPage(pageName)
            );
        });
        Label proj = new Label("Projects");
        proj.setPadding(new Insets(10, 0, 0, 0));
        proj.setStyle("-fx-text-align: center");
        proj.getStyleClass().setAll("b", "h4");
        vbox.getChildren().add(proj);
        pages.forEach((pageName, page) -> {
            if (!(page instanceof ProjectPage))
                return;
            renderButton(
                    vbox,
                    pageName,
                    e -> switchPage("project:" + ((ProjectPage) page).project.id)
            );
        });

        renderButton(
                vbox,
                "+ New Project",
                e -> (new AddProjectModal(this)).getContent().show()
        );
        //vbox.setStyle("-fx-background-color: rgba(0,155,255,1)");
        vbox.getStyleClass().addAll(/*"btn-lg",*/"panel-primary","bg-info");
        vbox.setSpacing(10);
        return vbox;
    }

    public void reload() {
        loadPages();
        panel.setLeft(getSidebar());
        panel.setBody(currentPage.getContent());
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        this.stage = primaryStage;
        InputStream s1 = new FileInputStream("src/main/resources/Task Flow2.png");
        Image image = new Image(s1);
        this.stage.getIcons().add(image);
        loadPages();
        panel = new Panel("Task Flow");
        panel.setLeft(getSidebar());

        panel.getStyleClass().add("panel-primary");

        switchPage("Dashboard");
        currentPage = pages.get("Dashboard");

        Scene scene = new Scene(panel, 1000, 500);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());       //(3)

        this.stage.setTitle("Task Flow");
        this.stage.setMinWidth(900);
        this.stage.setMinHeight(400);
        this.stage.setScene(scene);
        this.stage.sizeToScene();
        this.stage.show();
    }
}
