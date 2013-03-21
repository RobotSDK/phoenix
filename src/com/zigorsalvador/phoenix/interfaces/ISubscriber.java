package com.zigorsalvador.phoenix.interfaces;

import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Filter;

public interface ISubscriber
{
	public void subscribe(Filter filter);
	public void unsubscribe(Filter filter);
	public void reconfigure(Address broker);
	public void resume(Boolean replay);
	public void migrate(Boolean replay);
	public void unsubscribe();
	public void terminate();
}
