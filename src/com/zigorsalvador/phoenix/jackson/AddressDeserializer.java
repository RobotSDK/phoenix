package com.zigorsalvador.phoenix.jackson;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.AddressParser;

public class AddressDeserializer extends JsonDeserializer<Address>
{
	@Override
	
	public Address deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException
	{
		return AddressParser.address(parser.readValuesAs(String.class).next());
	}
}
