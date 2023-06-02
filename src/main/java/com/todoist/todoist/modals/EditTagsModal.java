package com.todoist.todoist.modals;

import com.mongodb.client.model.Updates;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.structures.BaseModal;
import com.todoist.todoist.structures.TodoistApp;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        tagsVbox.setSpacing(10);
        panel1.setBody(tagsVbox);

        tags.forEach((tagId, tag) -> renderTag(tagsVbox, tag, -1));

        Button addTagButton = new Button("+ Add Tag");
        addTagButton.setOnAction(e -> {
            Tag newTag = new Tag(
                    "New Label",
                    Integer.toHexString((int)(Math.random() * 0xFFFFFF)),
                    project);
            this.app.tagController.insert(newTag);
            tags.put(newTag.id, newTag);
            renderTag(tagsVbox, newTag, tagsVbox.getChildren().size() - 1);
        });
        addTagButton.getStyleClass().setAll("btn", "btn-primary");
        tagsVbox.getChildren().add(addTagButton);

        Scene scene = new Scene(panel1,400,200);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.sizeToScene();
        return stage;
    }

    public void renderTag(VBox tagsVbox, Tag tag, int order) {
        TextField tagTitleTextField = new TextField(tag.title);
        tagTitleTextField.setOnKeyPressed(ke -> {
            String tagTitle = tagTitleTextField.getText();
            if (tagTitle == null || tagTitle.equals(""))
                return;
            if (ke.getCode().equals(KeyCode.ENTER)) {
                tag.title = tagTitle;
                var updates = Updates.set("title", tag.title);
                this.app.tagController.update(tag, updates);
                tagsVbox.requestFocus();
            }
        });
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> {
            tag.color = colorPicker.getValue().toString().substring(0, 8);
            var updates = Updates.set("color", tag.color);
            this.app.tagController.update(tag, updates);
        });
        colorPicker.setValue(Color.web(tag.color));
        HBox tagHbox = new HBox(tagTitleTextField, colorPicker);
        tagHbox.setSpacing(10);
        if (order == -1)
            tagsVbox.getChildren().add(tagHbox);
        else
            tagsVbox.getChildren().add(order, tagHbox);
    }
}
