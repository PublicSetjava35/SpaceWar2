package org.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Configuration
abstract class Images {
    @Bean(name = "image")
    class_Images class_images() throws IOException {
        class_Images class_images = new class_Images();
        class_images.setImage(ImageIO.read(new File("src/main/images/cosmosWindow.jpg")));
        return class_images;
    }
    @Bean(name = "image2")
    class_Images class_images2() throws IOException {
        class_Images class_images2 = new class_Images();
        class_images2.setImage2(ImageIO.read(new File("src/main/images/OfflineCosmos.jpg")));
        return class_images2;
    }
    @Bean(name = "image3")
    class_Images class_images3() throws IOException {
        class_Images class_images3 = new class_Images();
        class_images3.setImage3(ImageIO.read(new File("src/main/images/SettingsCosmos.jpg")));
        return class_images3;
    }
    @Bean(name = "image4")
    class_Images class_images4() throws IOException {
        class_Images class_images = new class_Images();
        class_images.setImage4(ImageIO.read(new File("src/main/images/OnlineCosmos.jpg")));
        return class_images;
    }
    @Bean(name = "wall")
    class_Images class_images5() throws IOException {
        class_Images class_images = new class_Images();
        class_images.setImage5(ImageIO.read(new File("src/main/images/Wall.jpg")));
        return class_images;
    }
}
