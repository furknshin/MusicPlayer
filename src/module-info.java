module music.player {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;


    opens Sample to javafx.fxml;
    exports Sample;

    // Direk open Sample da diyebilirsin
}