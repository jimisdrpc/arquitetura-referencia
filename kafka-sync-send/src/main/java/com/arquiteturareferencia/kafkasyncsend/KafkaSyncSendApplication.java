package com.arquiteturareferencia.kafkasyncsend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.arquiteturareferencia.kafkasyncsend.producer.Producer;





@SpringBootApplication
public class KafkaSyncSendApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KafkaSyncSendApplication.class, args);
	}

	@Autowired
	private Producer p;
	
	//Para demonstrar um uso simples
	@Override
	public void run(String... strings) throws Exception {
		// test é o nome do tópico
		p.send("test", "Messagem demonstrativa sincrona");
	}
}
