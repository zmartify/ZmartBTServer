package com.zmartify.hub.zmartbtserver.bluetooth;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zmartify.hub.zmartbtsever.jettyclient.JettyClient;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MessageManager {
	private static final Logger log = LoggerFactory.getLogger(MessageManager.class);

	private AtomicInteger receivedMessages = new AtomicInteger(0);

	protected MessageListener subscriber = null;

	protected JettyClient jettyClient = new JettyClient();

	protected static ISendMessage sendMessage;

	protected transient boolean running = false;

	public MessageManager() {
	}

	public Observer<Message> messageListener() {
		return new Observer<Message>() {

			@Override
			public void onSubscribe(Disposable d) {
				log.debug(" onSubscribe : " + d.isDisposed());
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onNext(Message message) {
				// TODO Auto-generated method stub
				log.debug("We got a message (" + receivedMessages.incrementAndGet() + ") : " + message.toString()
						+ " : " + new String(message.getPayload()));

				String test = "{ \"uri\": \"http://192.168.2.130:8080/rest\", \"method\": \"GET\","
						+ " \"headers\": { \"AUTHORIZATION\": \"Basic test\" , \"CONTENT-TYPE\": \"application/json\" }, "
						+ " \"body\": { \"test\": [ \"ABC\", \"DEF\" ], \"AUTHORIZATION\": \"Basic test\" , \"CONTENT-TYPE\": \"application/json\" } }";

				message.setPayload(test);

				for (int i = 0; i < 10; i++)
					jettyClient.handleRequest(message);
			}

		};
	}

	public void register(MessageListener listener) {
		this.subscriber = listener;
		sendMessage = new SendMessage(subscriber);
		jettyClient.register(listener);
	}
}
