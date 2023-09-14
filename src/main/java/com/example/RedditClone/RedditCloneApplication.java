package com.example.RedditClone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate){
//		return args -> {
//			kafkaTemplate.send("redditCloneTopic", "hello kafka...");
//		};
//	}

	//COMENZI KAFKA
	//.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
	//.\bin\windows\kafka-server-start.bat .\config\server.properties
	//.\bin\windows\kafka-console-consumer.bat --topic redditCloneTopic --bootstrap-server localhost:9092 --from-beginning



}
