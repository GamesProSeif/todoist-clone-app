package com.todoist.todoist.pages;

import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class DashboardPage extends Page {
    public DashboardPage(TodoistApp app) {
        super(app);
    }
    @Override
    public Pane getContent() {
        BorderPane pane = new BorderPane();
        Label label = new Label("    Dashboard Page    ");
        label.setStyle("-fx-text-align: center");
        label.getStyleClass().addAll("h3","b","alert-success","panel-success","btn-lg");

        VBox vbox1 = new VBox();
        HBox hbox1 = new HBox();

        Label l1 = new Label("Due-Dates Approaching: ");
        l1.getStyleClass().addAll("h4","b","text-info");

        Label l2 = new Label("Current Projects: ");
        l2.getStyleClass().addAll("h4","b","text-info","p");

        hbox1.getChildren().addAll(l1);
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        hbox3.setSpacing(10);
        hbox2.getChildren().addAll(l2);
        label.setAlignment(Pos.TOP_CENTER);
        vbox1.getChildren().addAll(label,hbox1,hbox2);
        vbox1.setSpacing(20);
        vbox1.setAlignment(Pos.CENTER);

        var projects = this.app.projectController.list();

        projects.forEach((id, project) -> {
            Label L = new Label();
            L.setText(project.title);
            L.setStyle("-fx-text-align: center");
            L.getStyleClass().addAll("b","btn-lg","btn","bg-success","h4");
            hbox3.getChildren().add(L);
        });

        vbox1.getChildren().add(hbox3);

        //InputStream s1 = new FileInputStream();
        //Image image = new Image("src/main/resources/Task Flow2.png");

        pane.setCenter(vbox1);

       // pane.getStyleClass().add("-fx-background-image:url(src/main/resources/Task Flow2.png)");

        return pane;
    }
}
