package Sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {


    @FXML
    private AnchorPane Pane;

    @FXML
    private String[] wallpapers = {"porsche.jpg", "porsche2.jpg","porsche3.jpg","bmw.jpg", "bmw2.jpg","mcLarenP1.jpg"};

    @FXML
    private ListView<String> listView;

    @FXML
    private Button changeImageButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Label labelSong;

    @FXML
    private Button nextButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button playButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button resetButton;

    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private ComboBox<String> speedBox;

    @FXML
    private Slider volumeSlider;

    private File directory;
    private  File[] files;

    private ArrayList<File> songs;
    private int songNumber;
    private double[] speeds = {0.25,0.50,0.75,1.0,1.25,1.5,1.75,2.0};

    private Timer timer;
    private TimerTask task;
    private boolean running;

    private Media media;
    private MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        songs = new ArrayList<File>();

        directory = new File("Musics");

        files = directory.listFiles();

        for (File eleman : files){
            listView.getItems().add(eleman.getName());
        }

        if (files != null){

            for (File file: files){
                songs.add(file);


            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        labelSong.setText(songs.get(songNumber).getName());


        for (int i = 0 ; i < speeds.length; i+=1){
            speedBox.getItems().add("x "+ Double.toString(speeds[i]));
        }

//        speedBox.setOnAction(this::changeSpeed);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

    }


    @FXML
    void changeSpeed(ActionEvent event) {

        if (speedBox.getValue() == null){
            mediaPlayer.setRate(1);
        }
        else{
            mediaPlayer.setRate(Double.parseDouble((speedBox.getValue().split(" "))[1]));
        }

    }

    @FXML
    void nextMedia(ActionEvent event) {

        if(songNumber < songs.size()-1){
            songNumber+=1;
            mediaPlayer.stop();


            if (running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            labelSong.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {
            songNumber = 0;
            mediaPlayer.stop();


            if (running){
                cancelTimer();
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            labelSong.setText(songs.get(songNumber).getName());

            playMedia();


        }

    }

    @FXML
    void pauseMedia(ActionEvent event) {
        cancelTimer();
        mediaPlayer.pause();
    }

    @FXML
    void playMedia() {

        beginTimer();
        changeSpeed(null); // Event parametresi var ama girmemiz gereken bir olay yok sadece fonksiyonu
                                // çağırmak istiyoruz o halde parametreye null geçeriz
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    @FXML
    void previousMedia(ActionEvent event) {

        if(songNumber > 0){
            songNumber-=1;
            mediaPlayer.stop();

            if (running){
                cancelTimer();
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            labelSong.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {
            songNumber = songs.size() -1 ;
            mediaPlayer.stop();

            if (running){
                cancelTimer();
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            labelSong.setText(songs.get(songNumber).getName());

            playMedia();


        }



    }

    @FXML
    void resetMedia(ActionEvent event) {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));

    }

    void beginTimer(){

        timer = new Timer();

        task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if (current/end == 1){
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0,1000);
    }

    void cancelTimer(){

        running = false;
        timer.cancel();
    }

    @FXML
    void changeWallpaper(){

        String randomWalpaper = wallpapers[(int)(Math.random()*wallpapers.length)];

        Image resim = new Image(randomWalpaper);
        imageView.setImage(resim);
    }

}
