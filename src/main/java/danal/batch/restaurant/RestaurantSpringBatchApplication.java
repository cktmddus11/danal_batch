package danal.batch.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RestaurantSpringBatchApplication {
    public static void main(String[] args) {

        if (args != null) {
            String argsInfo = String.join(" ", args);
            log.info("main started with args '{}'", argsInfo);
        }

        try (var context = SpringApplication.run(RestaurantSpringBatchApplication.class, args) ){
            var exitCode = SpringApplication.exit(context);

            log.info("Spring batch Finished! Exit Code is '{}'", exitCode);
            System.exit(exitCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            System.exit(-1);
        }

    }

}
