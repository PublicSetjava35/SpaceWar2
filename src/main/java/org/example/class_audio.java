package org.example;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
@Data
public class class_audio {
    private javazoom.jl.player.Player audio;
    private javazoom.jl.player.Player err_audio;
    public void run() {
        new Thread(() -> {
            try {
                FileInputStream file = new FileInputStream("src/main/audio/clickButton.mp3");
                audio = new Player(file);
                audio.play();
                if(audio != null) {
                    audio.close();
                    audio = null;
                }
            } catch (IOException | JavaLayerException ignored) {}
        }).start();
    }
    public void err() {
        new Thread(() -> {
            try {
                FileInputStream file = new FileInputStream("src/main/audio/error_connected.mp3");
                err_audio = new Player(file);
                err_audio.play();
                if(err_audio != null) {
                   err_audio.close();
                   err_audio = null;
                }
            } catch (IOException | JavaLayerException ignored) {}
        }).start();
    }
}
