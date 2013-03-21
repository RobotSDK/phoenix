package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Event;

public interface IListener
{
	public void notify(Event event, Boolean replay);
}
