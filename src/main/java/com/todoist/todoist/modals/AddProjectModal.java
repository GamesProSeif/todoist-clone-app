package com.todoist.todoist.modals;

import com.todoist.todoist.models.Project;
import com.todoist.todoist.structures.BaseModal;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddProjectModal extends BaseModal {
    public AddProjectModal(TodoistApp app) {
        super(app);
    }

    public Stage getContent() {
        Stage stage = new Stage();
        stage.initOwner(app.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Add new Project");

        Panel panel1 = new Panel("Add a New Project");
        panel1.getStyleClass().addAll("panel-primary");
        Label l1 = new Label("Project Name: ");
        l1.getStyleClass().addAll("h5","b");

        TextField projName = new TextField();

        Button addProj = new Button("Add Project");
        addProj.getStyleClass().addAll("btn","alert-info","panel-primary");
        addProj.setOnAction(e -> {
            String projTitle = projName.getText();
            if (projTitle == null || projTitle.equals(""))
                return;
            createNewProject(projTitle);
            stage.close();
            this.app.reload();
        });

        VBox v1 = new VBox(l1);
        VBox v2 = new VBox(projName,addProj);
        v1.setSpacing(25);
        v2.setSpacing(20);

        HBox h1 = new HBox(v1,v2);
        h1.setSpacing(20);
        h1.setAlignment(Pos.CENTER);

        panel1.setBody(h1);

        Scene scene = new Scene(panel1,400,200);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        InputStream s1 = null;
        try {
            s1 = new FileInputStream("src/main/resources/Task Flow2.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(s1);
        stage.getIcons().add(image);
        stage.setMinHeight(200);
        stage.setMinWidth(400);
        stage.setScene(scene);
        stage.sizeToScene();
        return stage;
    }

    private void createNewProject(String title) {
        this.app.projectController.insert(new Project(title));
    }
}
