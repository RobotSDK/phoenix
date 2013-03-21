package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Address;

public interface IBroker
{
	public void connect(Address broker);
	public void disconnect(Address broker);
	public void terminate();
}
