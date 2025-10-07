package org.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Configuration
abstract class Images {
    @Bean(name = "image")
    ClassImages class_images() throws IOException {
        ClassImages class_images = new ClassImages();
        class_images.setImage(ImageIO.read(new File("src/main/images/cosmosWindow.jpg")));
        return class_images;
    }
    @Bean(name = "image2")
    ClassImages class_images2() throws IOException {
        ClassImages class_images2 = new ClassImages();
        class_images2.setImage2(ImageIO.read(new File("src/main/images/OfflineCosmos.jpg")));
        return class_images2;
    }
    @Bean(name = "image3")
    ClassImages class_images3() throws IOException {
        ClassImages class_images3 = new ClassImages();
        class_images3.setImage3(ImageIO.read(new File("src/main/images/SettingsCosmos.jpg")));
        return class_images3;
    }
    @Bean(name = "image4")
    ClassImages class_images4() throws IOException {
        ClassImages class_images = new ClassImages();
        class_images.setImage4(ImageIO.read(new File("src/main/images/OnlineCosmos.jpg")));
        return class_images;
    }
    @Bean(name = "wall")
    ClassImages class_images5() throws IOException {
        ClassImages class_images = new ClassImages();
        class_images.setImage5(ImageIO.read(new File("src/main/images/Wall.jpg")));
        return class_images;
    }
}
