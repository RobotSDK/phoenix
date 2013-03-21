package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Message;

public interface IRouter
{
	public void connect(Address broker);
	public void disconnect(Address broker);
	public void processConnection(Message message);
	public void processDisconnection(Message message);
	public void processSubscription(Message message);
	public void processUnsubscription(Message message);
	public void processPublication(Message message);
	public void processMigration(Message message);
	public void processReplay(Message message);
	public void processResume(Message message);
	public void processFailure(Address subscriber);
	public void terminate();
}