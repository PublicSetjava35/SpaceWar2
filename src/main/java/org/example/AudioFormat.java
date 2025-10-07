package org.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AudioFormat {
    @Bean(name = "entered")
    ClassAudio class_audio() {
        return new ClassAudio();
    }
    @Bean(name = "err")
    ClassAudio class_audio2() {
        return new ClassAudio();
    }
}
