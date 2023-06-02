package com.todoist.todoist.pages;

import com.todoist.todoist.models.Task;
import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TaskPage extends Page {
    private final Task task;
    public TaskPage(TodoistApp app, Task task) {
        super(app);
        this.task = task;
    }

    @Override
    public Pane getContent() {
        BorderPane pane = new BorderPane();
        Label label = new Label(task.title);
        label.getStyleClass().addAll(
                "panel-heading","alert","text-heading","h4","b","btn-lg"
        );
        Button deleteTaskButton = new Button("Delete Task");
        HBox hbox = new HBox(label);
        pane.setTop(hbox);
        return pane;
    }
}
