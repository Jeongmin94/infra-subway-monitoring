package nextstep.subway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableJpaRepositories
@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableWebMvc
public class SubwayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubwayApplication.class, args);
    }

}
