package com.zigorsalvador.phoenix.jackson;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.zigorsalvador.phoenix.messages.Address;

public class AddressSerializer extends JsonSerializer<Address>
{
	@Override

	public void serialize(Address address, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException
	{
		generator.writeString(address.toString());		
	}
}
