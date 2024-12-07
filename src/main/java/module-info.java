module com.example.warehouse {
    requires javafx.controls;
    requires javafx.fxml;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.api.apicommon;
    requires com.google.common;
    requires java.sql;


    opens com.example.warehouse to javafx.fxml;
    exports com.example.warehouse;
}