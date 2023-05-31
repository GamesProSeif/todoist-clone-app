module com.todoist.todoist {
    requires javafx.controls;
    requires javafx.fxml;
            
                        requires org.kordamp.bootstrapfx.core;
            
    opens com.todoist.todoist to javafx.fxml;
    exports com.todoist.todoist;
}