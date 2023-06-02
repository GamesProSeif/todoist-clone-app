package com.todoist.todoist.pages;

import com.todoist.todoist.models.Project;
import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

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
        vbox.setSpacing(20);
        pane.setCenter(vbox);

        HBox hbox1 = new HBox();
        hbox1.setSpacing(10);
        Group grp = new Group();

        label.getStyleClass().addAll("panel-heading","alert-info","text-heading","h4","b","btn-lg");

        var tasks = app.taskController.list();
        tasks.forEach((id, task) -> {
            if (task.project.id.equals(project.id)) {
                Label taskLabel = new Label(task.title);
                taskLabel.setText("    "+taskLabel.getText()+"    ");
                taskLabel.getStyleClass().addAll("panel-primary","btn-lg","lbl-info","text-heading");
                /*Rectangle r = new Rectangle(100,100);
                r.setFill(Color.AQUA);
                grp.getChildren().addAll(taskLabel);*/

                hbox1.getChildren().addAll(taskLabel);
                vbox.getChildren().add(hbox1);
            }
        });

        return pane;
    }
}
