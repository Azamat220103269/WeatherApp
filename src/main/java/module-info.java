module com.example.finalproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.net.http;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;


    opens com.example.finalproj to javafx.fxml;
    exports com.example.finalproj;
    exports com.example.finalproj.func;
    opens com.example.finalproj.func to javafx.fxml;
}