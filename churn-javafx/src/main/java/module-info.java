module tcc.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens tcc.project to javafx.fxml;
    exports tcc.project;
}