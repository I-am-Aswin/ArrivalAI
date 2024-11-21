package org.broz.arrivalai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ArrivalAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArrivalAiApplication.class, args);
    }

}
