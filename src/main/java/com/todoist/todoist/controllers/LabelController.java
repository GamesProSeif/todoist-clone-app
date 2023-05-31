package com.todoist.todoist.controllers;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.models.Label;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.structures.BaseController;
import org.bson.Document;

public class LabelController extends BaseController<Label> {
    private final ProjectController projectController;
    public LabelController(MongoDatabase db, ProjectController projectController) {
        super(db, "labels");
        this.projectController = projectController;
    }

    @Override
    protected Label documentToModel(Document doc) {
        Project project = projectController.findById(doc.getObjectId("project"));

        return new Label(
                doc.getObjectId("_id"),
                doc.getString("title"),
                doc.getInteger("color"),
                project
        );
    }

    @Override
    protected Document modelToDocument(Label label) {
        return new Document()
                .append("_id", label.id)
                .append("title", label.title)
                .append("color", label.color)
                .append("project", label.project.id);
    }
}
