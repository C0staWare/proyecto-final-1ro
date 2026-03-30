module com.sucursalbancaria {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.sucursalbancaria to javafx.fxml;
    exports com.sucursalbancaria;
    opens com.sucursalbancaria.Controllers.ControlVistas to javafx.fxml;
}
