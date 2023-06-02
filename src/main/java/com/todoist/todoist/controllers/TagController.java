package com.todoist.todoist.controllers;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.models.Tag;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.structures.BaseController;
import org.bson.Document;

public class TagController extends BaseController<Tag> {
    private final ProjectController projectController;
    public TagController(MongoDatabase db, ProjectController projectController) {
        super(db, "labels");
        this.projectController = projectController;
    }

    @Override
    protected Tag documentToModel(Document doc) {
        Project project = projectController.findById(doc.getObjectId("project"));

        return new Tag(
                doc.getObjectId("_id"),
                doc.getString("title"),
                doc.getInteger("color"),
                project
        );
    }

    @Override
    protected Document modelToDocument(Tag tag) {
        return new Document()
                .append("_id", tag.id)
                .append("title", tag.title)
                .append("color", tag.color)
                .append("project", tag.project.id);
    }
}
