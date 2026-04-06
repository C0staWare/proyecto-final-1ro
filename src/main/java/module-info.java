module com.sucursalbancaria {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.sucursalbancaria to javafx.fxml;
    opens com.sucursalbancaria.Models.Solicitantes to javafx.base;
    opens com.sucursalbancaria.Models.Solicitudes to javafx.base;

    exports com.sucursalbancaria;
    opens com.sucursalbancaria.Controllers.ControlVistas to javafx.fxml;
}
