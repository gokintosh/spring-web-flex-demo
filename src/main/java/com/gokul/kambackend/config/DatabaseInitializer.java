package com.gokul.kambackend.config;

import com.gokul.kambackend.domain.Product;
import com.gokul.kambackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


@Component
@AllArgsConstructor
@Log4j2
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {


    private ProductRepository productRepository;





    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
            productRepository.deleteAll().thenMany(
                            Flux.just("Macbook pro","Macbook Air","Ipad pro","Ipad mini")
                                    .map(name->new Product(UUID.randomUUID().toString(),name, ThreadLocalRandom.current().nextDouble(1000,5000)))
                                    .flatMap(productRepository::save)
                    )
                    .thenMany(productRepository.findAll())
                    .subscribe(product -> log.info("Saved product {}",product));
        }
}
