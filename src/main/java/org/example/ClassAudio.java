package org.example;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.Data;
@Data
public class ClassAudio {
    private javazoom.jl.player.Player audio;
    private javazoom.jl.player.Player err_audio;
    private Thread thread, threadPick;
    public void run() {
        threadPick = new Thread(() -> {
            if(threadPick != null && threadPick.isAlive()) {
                threadPick.interrupt();
            }
            try {
                audio = new Player(getClass().getResourceAsStream("/org/example/resources/audio/clickButton.mp3"));
                audio.play();
            } catch (JavaLayerException ignored) {}
        });
        threadPick.start();
    }
    public void err() {
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }
       thread = new Thread(() -> {
            try {
                err_audio = new Player(getClass().getResourceAsStream("/org/example/resources/audio/error_connected.mp3"));
                err_audio.play();
            } catch (JavaLayerException ignored) {}
        });
        thread.start();
    }
}
