package com.arquiteturareferencia.kafkaassyncsend.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class Producer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Async
	public void send(String topic, String message) {
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			
			//Call Back de sucesso de postagem da mensagem no tópico
			@Override
			public void onSuccess(final SendResult<String, String> message) {
				LOGGER.info("sent message= " + message + " with offset= " + message.getRecordMetadata().offset());
			}

			//Call Back de fracasso de postagem da mensagem no tópico 
			@Override
			public void onFailure(final Throwable throwable) {
				LOGGER.error("unable to send message= " + message, throwable);
			}
		});
	}
}
