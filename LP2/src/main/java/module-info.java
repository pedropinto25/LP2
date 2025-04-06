module com.lp2.lp2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires java.dotenv;
    requires com.opencsv;
    requires com.mailjet.api;
    requires org.json;
    opens com.lp2.lp2.Model;
    opens com.lp2.lp2.Controller to javafx.fxml;
    opens com.lp2.lp2 to javafx.fxml;
    exports com.lp2.lp2;
    exports com.lp2.lp2.Controller;
    exports com.lp2.lp2.Controller.Cliente;
    opens com.lp2.lp2.Controller.Cliente to javafx.fxml;
    exports com.lp2.lp2.Controller.Leilao;
    opens com.lp2.lp2.Controller.Leilao to javafx.fxml;
    exports com.lp2.lp2.Controller.Login;
    opens com.lp2.lp2.Controller.Login to javafx.fxml;
}