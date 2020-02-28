package com.arquiteturareferencia.kafkaassyncsend.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class KafkaProducerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerConfig.class);

	@Value("${kafka.brokers}")
	private String servers;
	
	
	//Esse bean de configuração é muito sensível e altera significativamente o comportamento default
	//é de se esperar que na maioria das vezes não será necessário criar um pool de threads e a configuração seguinte,
	// que retornar um Executor customizado pode ser comentada.
	//Dever ser usado apenas quando houver uma taxa muito alta de requisição por segundo e preferencialmente
	//ser discutido junto com o time de arquitetura
	//Sugestão de leitura: https://stackoverflow.com/questions/60385961/what-are-the-drawnbacks-and-risks-replacing-default-simpleasynctaskexecutor-by-o
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("KafkaMsgExecutor-");
		executor.initialize();
		return executor;
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		return props;
	}

}
