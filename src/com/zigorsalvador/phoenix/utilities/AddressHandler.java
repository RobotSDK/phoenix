package com.zigorsalvador.phoenix.utilities;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.zigorsalvador.phoenix.messages.Address;

public class AddressHandler extends OptionHandler<Address>
{
	public AddressHandler(CmdLineParser parser, OptionDef option, Setter<? super Address> setter)
	{
		super(parser, option, setter);
	}
	
	//////////
	
	@Override
	
	public int parseArguments(Parameters parameters) throws CmdLineException 
	{
		setter.addValue(AddressParser.address(parameters.getParameter(0)));
		
		return 1;
	}
	
	//////////

	@Override
	
	public String getDefaultMetaVariable() 
	{
		return "ADDRESS";
	}
}