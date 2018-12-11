package com.izhubo.web.mq;

import org.hibernate.SessionFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.rabbitmq.client.Channel;

public abstract class BaseConsumer implements ChannelAwareMessageListener {

	/** _mainMongo */
	protected MongoTemplate _mainMongo;
	protected MongoTemplate _qquserMongo;
	protected MongoTemplate _logMongo;
	protected SessionFactory _sessionFactory;

	
	public BaseConsumer(MongoTemplate mainMongo, MongoTemplate qquserMongo,
			MongoTemplate logMongo, SessionFactory sessionFactory) {
		super();
		this._mainMongo = mainMongo;
		this._qquserMongo = qquserMongo;
		this._logMongo = logMongo;
		this._sessionFactory = sessionFactory;
	}

	@Override
	public abstract void onMessage(Message message, Channel channel)
			throws Exception;
	

	public MongoTemplate get_mainMongo() {
		return _mainMongo;
	}

	public void set_mainMongo(MongoTemplate _mainMongo) {
		this._mainMongo = _mainMongo;
	}

	public MongoTemplate get_qquserMongo() {
		return _qquserMongo;
	}

	public void set_qquserMongo(MongoTemplate _qquserMongo) {
		this._qquserMongo = _qquserMongo;
	}

	public MongoTemplate get_logMongo() {
		return _logMongo;
	}

	public void set_logMongo(MongoTemplate _logMongo) {
		this._logMongo = _logMongo;
	}

	public SessionFactory get_sessionFactory() {
		return _sessionFactory;
	}

	public void set_sessionFactory(SessionFactory _sessionFactory) {
		this._sessionFactory = _sessionFactory;
	}

}
