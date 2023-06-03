package com.todoist.todoist.modals;

import com.mongodb.client.model.Updates;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.structures.BaseModal;
import com.todoist.todoist.structures.TodoistApp;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class EditTagsModal extends BaseModal {
    Project project;
    HashMap<ObjectId, Tag> tags;
    Panel panel;

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

        panel = new Panel(project.title + ": Edit Tags");
        panel.getStyleClass().addAll("panel-primary");

        VBox tagsVbox = new VBox();
        tagsVbox.setSpacing(10);
        panel.setBody(tagsVbox);

        tags.forEach((tagId, tag) -> renderTag(tagsVbox, tag, -1));

        Button addTagButton = new Button("+ Add Tag");
        addTagButton.setOnAction(e -> {
            Tag newTag = new Tag(
                    "New Tag",
                    Integer.toHexString((int)(Math.random() * 0xFFFFFF)),
                    project);
            while (newTag.color.length() != 6) {
                newTag.color = "0" + newTag.color;
            }
            this.app.tagController.insert(newTag);
            tags.put(newTag.id, newTag);
            renderTag(tagsVbox, newTag, tagsVbox.getChildren().size() - 1);
            stage.setMinHeight(tags.size()*30+200);
            stage.setHeight(stage.getMinHeight());
        });
        addTagButton.getStyleClass().setAll("btn", "btn-primary");
        tagsVbox.getChildren().add(addTagButton);

        Scene scene = new Scene(panel,400,200);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        InputStream s1 = null;
        try {
            s1 = new FileInputStream("src/main/resources/Task Flow2.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(s1);
        stage.getIcons().add(image);
        stage.setMinHeight(tags.size()*30+200);
        stage.setHeight(stage.getMinHeight());
        stage.setMinWidth(400);
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
            tag.color = colorPicker.getValue().toString().substring(2, 8);
            var updates = Updates.set("color", tag.color);
            this.app.tagController.update(tag, updates);
        });
        colorPicker.setValue(Color.web(tag.color));
        ImageView iv = new ImageView(new Image("/trash-fill.red.png"));
        iv.setFitHeight(15);
        iv.setFitWidth(15);
        HBox tagHbox = new HBox(tagTitleTextField, colorPicker, iv);
        iv.setOnMouseEntered(me -> panel.setCursor(Cursor.HAND));
        iv.setOnMouseExited(me -> panel.setCursor(Cursor.DEFAULT));
        iv.setOnMouseClicked(e -> {
            this.app.taskController.list().forEach((taskId, task) -> {
                if (!task.project.id.equals(tag.project.id))
                    return;
                if (task.tags.contains(tag.id)) {
                    task.tags.remove(tag.id);
                    var updates = Updates.set("tags", task.tags);
                    this.app.taskController.update(task, updates);
                }
            });
            tags.remove(tag.id);
            this.app.tagController.delete(tag);
            tagsVbox.getChildren().remove(tagHbox);
            this.app.reload();
        });
        tagHbox.setAlignment(Pos.CENTER_LEFT);
        tagHbox.setSpacing(10);
        if (order == -1)
            tagsVbox.getChildren().add(tagHbox);
        else
            tagsVbox.getChildren().add(order, tagHbox);
    }
}
