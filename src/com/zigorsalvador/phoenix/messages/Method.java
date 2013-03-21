package com.zigorsalvador.phoenix.messages;

public enum Method
{
	CNN,	// Broker connection request
	BYE,	// Broker disconnection request	
	SUB,	// Filter subscription request		
	UNS,	// Filter unsubscription request	
	PUB,	// Event publication message	
	RES,	// Connection resume request
	MIG,	// Subscriber migration request		
	REP,	// Targeted event replay message	
}