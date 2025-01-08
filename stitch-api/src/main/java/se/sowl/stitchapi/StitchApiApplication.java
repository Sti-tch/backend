package se.sowl.stitchapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EntityScan(basePackages = {"se.sowl.stitchdomain"})
@EnableCaching
public class StitchApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StitchApiApplication.class, args);
    }

}
//