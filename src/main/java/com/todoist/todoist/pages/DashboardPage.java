package com.todoist.todoist.pages;

import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class DashboardPage extends Page {
    public DashboardPage(TodoistApp app) {
        super(app);
    }
    @Override
    public Pane getContent() {
        BorderPane pane = new BorderPane();
        Label label = new Label("Dashboard Page");
        pane.setCenter(label);

        return pane;
    }
}
