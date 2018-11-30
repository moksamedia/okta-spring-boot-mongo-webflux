package com.okta.springbootmongo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.text.NumberFormat;
import java.text.ParseException;

@SpringBootApplication
//@EnableReactiveMongoRepositories
public class MainApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    ApplicationRunner init(KayakRepository repository) {

        String[][] data = {
                {"sea", "Andrew", "300.12", "NDK"},
                {"creek", "Andrew", "100.75", "Piranha"},
                {"loaner", "Andrew", "75", "Necky"}
        };

        return args -> {
            repository
                .deleteAll()
                .thenMany(
                    Flux
                        .just(data)
                        .map(array -> {
                            try {
                                return new Kayak(array[0], array[1], NumberFormat.getInstance().parse(array[2]), array[3]);
                            }
                            catch (ParseException e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .flatMap(repository::save)
                    )
                .thenMany(repository.findAll())
                .subscribe(kayak -> System.out.println("saving " + kayak.toString()));

        };
    }
    /*
    @Autowired
    MongoClient mongoClient;

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient, "springbootmongodb");
    }
    */

    /*
    @Override
    protected String getDatabaseName() {
        return "springbootmongodb";
    }

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create();
    }
    */
}
