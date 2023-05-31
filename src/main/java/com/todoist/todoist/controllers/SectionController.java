package com.todoist.todoist.controllers;

import com.mongodb.client.MongoDatabase;
import com.todoist.todoist.models.Project;
import com.todoist.todoist.models.Section;
import com.todoist.todoist.structures.BaseController;
import org.bson.Document;
import org.bson.types.ObjectId;

public class SectionController extends BaseController<Section> {
    private final ProjectController projectController;

    public SectionController(MongoDatabase db, ProjectController projectController) {
        super(db, "sections");
        this.projectController = projectController;
    }

    @Override
    protected Section documentToModel(Document doc) {
        Project project = projectController.findById(doc.getObjectId("project"));

        return new Section(
                doc.getObjectId("_id"),
                doc.getString("title"),
                project
        );
    }

    @Override
    protected Document modelToDocument(Section section) {
        return new Document()
                .append("_id", section.id)
                .append("title", section.title)
                .append("project", section.project.id);
    }
}
