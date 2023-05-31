package com.todoist.todoist.pages;

import com.todoist.todoist.models.Project;
import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ProjectPage extends Page {
    Project project;
    public ProjectPage(TodoistApp app, Project project) {
        super(app);
        this.project = project;
    }

    @Override
    public Pane getContent() {
        BorderPane pane = new BorderPane();
        Label label = new Label("Project: " + project.title);
        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        pane.setCenter(vbox);

        var tasks = app.taskController.list();
        tasks.forEach((id, task) -> {
            if (task.project.id.equals(project.id)) {
                Label taskLabel = new Label(task.title);
                vbox.getChildren().add(taskLabel);
            }
        });

        return pane;
    }
}
