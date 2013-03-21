package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Event;

public interface IPublisher
{
	public void publish(Event event);
	public void terminate();
}
