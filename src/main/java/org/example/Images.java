package org.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Configuration
public class Images {

    @Bean(name = "image")
    class_Images class_images() throws IOException {
        class_Images class_images = new class_Images();
        class_images.setImage(ImageIO.read(new File("src/main/images/cosmosWindow.jpg")));
        return class_images;
    }
}
