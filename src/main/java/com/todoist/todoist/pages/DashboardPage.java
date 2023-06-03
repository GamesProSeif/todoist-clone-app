package com.todoist.todoist.pages;

import com.todoist.todoist.structures.Page;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;


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

        Panel panel = new Panel("Dashboard Page");
        panel.setStyle("-fx-text-align: center");
        panel.getStyleClass().addAll("panel-success","b","h3");

        hbox1.getChildren().addAll(l1);
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        hbox3.setSpacing(10);
        hbox4.setSpacing(10);
        hbox2.getChildren().addAll(l2);
        label.setAlignment(Pos.TOP_CENTER);
        vbox1.getChildren().addAll(hbox1,hbox2);
        vbox1.setSpacing(20);
        vbox1.setAlignment(Pos.CENTER);

        var tasks = this.app.taskController.list();

        tasks.forEach((objectId, task) -> {
            if(task.dueDate==null)
                return;
            Label D = new Label();
//            long days = Duration.between(task.dueDate, LocalDate.now()).toDays();
            long days = ChronoUnit.DAYS.between(task.dueDate, LocalDate.now());
                if(days<=7)
                {
                    D.setText(task.title+" "+task.dueDate);
                    D.setStyle("-fx-text-align: center");
                    D.getStyleClass().addAll("b","btn-lg","btn","bg-success","h4");
                    hbox4.getChildren().add(D);
                }
            D.setOnMouseClicked(e->{
                this.app.switchPage("task:" + task.id);
            });
        });

        var projects = this.app.projectController.list();

        projects.forEach((id, project) -> {
            Label L = new Label();
            L.setText(project.title);
            L.setStyle("-fx-text-align: center");
            L.getStyleClass().addAll("b","btn-lg","btn","bg-success","h4");
            hbox3.getChildren().add(L);

            L.setOnMouseClicked(e->{
                this.app.switchPage("project:" + project.id);
            });
            //if()
        });

        vbox1.getChildren().add(hbox3);
        vbox1.getChildren().add(1,hbox4);
        vbox1.getChildren().add(2,new Label());
        panel.setBody(vbox1);

        pane.setCenter(panel);

        return pane;
    }
}
