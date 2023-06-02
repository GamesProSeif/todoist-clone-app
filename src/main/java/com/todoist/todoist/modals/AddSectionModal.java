package com.todoist.todoist.modals;

import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Section;
import com.todoist.todoist.pages.ProjectPage;
import com.todoist.todoist.structures.BaseModal;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class AddSectionModal extends BaseModal {
    Project project;
    ProjectPage page;
    public AddSectionModal(TodoistApp app, ProjectPage page) {
        super(app);
        this.page = page;
        this.project = page.project;
    }

    public Stage getContent() {
        Stage stage = new Stage();
        stage.initOwner(app.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(project.title + ": Add new Section");

        Panel panel1 = new Panel(project.title + ": Add new Section");
        panel1.getStyleClass().addAll("panel-primary");
        Label l1 = new Label("Section Name: ");
        l1.getStyleClass().addAll("h5","b");

        TextField sectionNameTextField = new TextField();

        Button addSection = new Button("Add Section");
        addSection.getStyleClass().addAll("btn","alert-info","panel-primary");
        addSection.setOnAction(e -> {
            String sectionTitle = sectionNameTextField.getText();
            if (sectionTitle == null || sectionTitle.equals(""))
                return;
            createNewSection(sectionTitle);
            stage.close();
            this.app.reload();
        });

        VBox v1 = new VBox(l1);
        VBox v2 = new VBox(sectionNameTextField, addSection);
        v1.setSpacing(25);
        v2.setSpacing(20);

        HBox h1 = new HBox(v1,v2);
        h1.setSpacing(20);
        h1.setAlignment(Pos.CENTER);

        panel1.setBody(h1);

        Scene scene = new Scene(panel1,400,200);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.sizeToScene();
        return stage;
    }

    private void createNewSection(String sectionTitle) {
        this.app.sectionController.insert(new Section(sectionTitle, project));
    }
}
