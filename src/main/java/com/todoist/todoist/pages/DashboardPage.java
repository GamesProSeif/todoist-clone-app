package com.todoist.todoist.pages;

import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DashboardPage extends Page {
    public DashboardPage(TodoistApp app) {
        super(app);
    }
    @Override
    public Pane getContent() {
        BorderPane pane = new BorderPane();
        Label label = new Label("    Dashboard Page    ");
        label.getStyleClass().addAll("h3","b","alert-success","panel-primary","btn-lg");

        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();

        Label l1 = new Label("Due Today: ");
        l1.getStyleClass().addAll("h4","b","text-info");

        Label l2 = new Label("Projects: ");
        l2.getStyleClass().addAll("h4","b","text-info");

        hbox1.getChildren().addAll(l1);
        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(l2);
        label.setAlignment(Pos.TOP_CENTER);
        vbox1.getChildren().addAll(label,hbox1,hbox2);
        vbox1.setSpacing(75);
        vbox1.setAlignment(Pos.CENTER);


        pane.setCenter(vbox1);


        return pane;
    }
}
