package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Address;

public interface ISender
{
	public void failure(Address address);
}
