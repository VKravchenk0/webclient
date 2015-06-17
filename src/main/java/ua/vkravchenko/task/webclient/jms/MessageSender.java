package ua.vkravchenko.task.webclient.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
	
	private JmsTemplate jmsTemplate;
	
	private Queue outgoingQueue;
	
	private static final Logger logger = Logger .getLogger(MessageSender.class);

	// отсылает сообщение в очередь и ждет ответа
	public String sendMessage(String message) throws JMSException {
		logger.debug("About to put message on queue. Queue[" + outgoingQueue.toString() + "] Message[" + message + "]");
		jmsTemplate.setTimeToLive(5000);
		jmsTemplate.convertAndSend(outgoingQueue, message);
		
		Message receivedMessage = jmsTemplate.receive(); 
		String messageToReturn = null;
		if (receivedMessage != null) {
			if (receivedMessage instanceof TextMessage) {
				try {
					messageToReturn = ((TextMessage) receivedMessage).getText();
					logger.debug("About to process message: " + messageToReturn);
				} catch (JMSException jmsEx_p) {
					String errMsg = "An error occurred extracting message";
					logger.error(errMsg, jmsEx_p);
					return null;
				}
			}
			else {
				String errMsg = "Message is not of expected type TextMessage";
				logger.error(errMsg);
			}
			
			return messageToReturn;
		} else {
			return null;
		}
		
	}
	
	public void setJmsTemplate(JmsTemplate tmpl) {
		this.jmsTemplate = tmpl;
	}
	
	public void setOutgoingQueue(Queue queue) {
		this.outgoingQueue = queue;
	}

}
