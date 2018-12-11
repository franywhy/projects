package com.izhubo.web.mq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.izhubo.rest.anno.Rest;

@Controller
public class MessageProductor {

	@Autowired
	private AmqpAdmin admin;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private ConnectionFactory connectionFactory;

	public void pushToMessageQueue(String routingKey, String message) {
		amqpTemplate.convertAndSend(routingKey, message);
		
		
	}

	public void popMessage(String destinationQueueName) {
		Message message = amqpTemplate.receive(destinationQueueName);
		System.out.println("成功取出消息 " + new String(message.getBody()));
	}
    
    
    

}
