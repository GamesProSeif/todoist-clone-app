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
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

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
        button.getStyleClass().setAll("btn");
        button.setMaxWidth(vbox.getMinWidth());

        button.setOnAction(e);

        vbox.getChildren().add(button);
    }

    private Pane getSidebar() {
        VBox vbox = new VBox();
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

        vbox.getChildren().add(new Label("Projects"));
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
        return vbox;
    }

    public void reload() {
        loadPages();
        panel.setLeft(getSidebar());
        panel.setBody(currentPage.getContent());
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        loadPages();
        panel = new Panel("Todoist");
        panel.setLeft(getSidebar());
        panel.getStyleClass().add("panel-primary");

        switchPage("Dashboard");
        currentPage = pages.get("Dashboard");

        Scene scene = new Scene(panel, 1000, 500);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());       //(3)

        this.stage.setTitle("Todoist");
        this.stage.setScene(scene);
        this.stage.sizeToScene();
        this.stage.show();
    }
}
