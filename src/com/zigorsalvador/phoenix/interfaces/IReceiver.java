package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Message;

public interface IReceiver
{
	public void process(Message message);
}
