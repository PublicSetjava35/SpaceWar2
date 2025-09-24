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
    private Thread thread, threadPick;
    public void run() {
        threadPick = new Thread(() -> {
            if(threadPick != null && threadPick.isAlive()) {
                threadPick.interrupt();
            }
            try {
                FileInputStream file = new FileInputStream("src/main/audio/clickButton.mp3");
                audio = new Player(file);
                audio.play();
            } catch (IOException | JavaLayerException ignored) {}
        });
        threadPick.start();
    }
    public void err() {
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
       thread = new Thread(() -> {
            try {
                FileInputStream file = new FileInputStream("src/main/audio/error_connected.mp3");
                err_audio = new Player(file);
                err_audio.play();
            } catch (IOException | JavaLayerException ignored) {}
        });
        thread.start();
    }
}
