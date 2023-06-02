package com.todoist.todoist.modals;

import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.structures.BaseModal;
import com.todoist.todoist.structures.TodoistApp;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.HashMap;

public class EditTagsModal extends BaseModal {
    Project project;
    HashMap<ObjectId, Tag> tags;
    public EditTagsModal(TodoistApp app, Project project, HashMap<ObjectId, Tag> tags) {
        super(app);
        this.project = project;
        this.tags = tags;
    }

    @Override
    public Stage getContent() {
        Stage stage = new Stage();
        stage.initOwner(app.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(project.title + ": Edit Tags");

        Panel panel1 = new Panel(project.title + ": Edit Tags");
        panel1.getStyleClass().addAll("panel-primary");

        VBox tagsVbox = new VBox();
        panel1.setBody(tagsVbox);

        tags.forEach((tagId, tag) -> {
            TextField tagTitleTextField = new TextField(tag.title);
            Rectangle rect = new Rectangle(50, 50);
            rect.setStyle("-fx-background-color: #" + Integer.toHexString(tag.color));
            HBox tagHbox = new HBox();
            tagsVbox.getChildren().add(tagHbox);
        });

        Button addTagButton = new Button("+ Add Tag");
        addTagButton.getStyleClass().setAll("btn", "btn-primary");
        tagsVbox.getChildren().add(addTagButton);

        Scene scene = new Scene(panel1,400,200);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.sizeToScene();
        return stage;
    }
}
