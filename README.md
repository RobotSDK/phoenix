## Phoenix

Phoenix is a content-based publish/subscribe middleware implemented in Java. Phoenix enables the development of distributed publish/subscribe applications that are based on the interaction of three different types of publish/subscribe components:

* **Publishers**: generate information in the form of events.
* **Subscribers**: subscribe to arbitrary flows of information.
* **Brokers**: route information from publishers to subscribers.

In order to deploy a minimal publish/subscribe application, one publisher, one subscriber and one broker have to be instantiated. However, typical publish/subscribe applications include multiple publishers, multiple subscribers and one or more brokers. Brokers connect to each other forming a content delivery network with the purpouse of routing published events from publishers to subscribers. In order to enable the optimal delivery of events, subscribers issue subscription messages that describe the kinds of content they would like to receive. In order to determine the set of subscribers that should receive a published event, brokers perform a content-based matching between the event and the subscriptions that have been issued. As a result of the content-based matching, an event will either be delivered to one or more subscribers or be dismissed, if the content of the event is of no interest to any of the subscribers.

## Features

The current release of Phoenix leverages the Apache MINA network application framework and has the following features:

* **Java implementation**: Phoenix is entirely written in Java and only makes use of open source libraries.
* **Android compatibility**: Phoenix components can be executed in Android devices (smartphones/tablets).
* **Client mobility support**: Phoenix supports publisher and subscriber mobility (disconnection/migration).
* **Broker discovery protocol**: Phoenix simplies the deployment of complex publish/susbcribe applications.
* **Text-based protocol (JSON)**: Phoenix uses an interoperable message format that simplifies debugging.

## License

The current version of Phoenix was developed in the context of my Ph.D thesis and is released under the [MIT License](http://github.com/zigorsalvador/phoenix/blob/master/LICENSE.md).

## Installation

First, download a copy of Phoenix:

    git clone https://github.com/zigorsalvador/phoenix.git

Then, enter your local repository:

    cd phoenix

Finally, build the project using Ant:

    ant package

Optionally, build the documentation:

    ant javadoc

## Launchers

Phoenix comes bundled with three launcher classes that enable the quick deployment of publish/subscribe applications:

* **Broker launcher**: com.zigorsalvador.phoenix.launchers.BrokerLauncher.
* **Publisher launcher**: com.zigorsalvador.phoenix.launchers.PublisherLauncher.
* **Subscriber launcher**: com.zigorsalvador.phoenix.launchers.SubscriberLauncher.

## Demonstration

Execute the following command in a terminal window to launch a broker component:

    java -classpath jar/*:lib/* com.zigorsalvador.phoenix.launchers.BrokerLauncher

The broker component will be initialized and its application window will be displayed:

![](https://raw.github.com/zigorsalvador/phoenix/master/png/screenshot_01.png)

Execute the following command in a new terminal window to launch a subscriber:

    java -classpath jar/*:lib/* com.zigorsalvador.phoenix.launchers.SubscriberLauncher -verbose

The subscriber component will discover the broker and will issue a subscription:

    12:34:56:789 > subscriber > Sent subscription = null text STR EQU good

The subscription and the subscriber will be registered in the broker window:

![](https://raw.github.com/zigorsalvador/phoenix/master/png/screenshot_03.png)

Execute the following command in a new terminal window to launch a publisher:

    java -classpath jar/*:lib/* com.zigorsalvador.phoenix.launchers.PublisherLauncher -verbose -text "good"

The publisher component will discover the broker and will start publishing events:

    12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:22222 1] [null STR text EQU good] []                                                  
    12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:22222 2] [null STR text EQU good] []                                                  
    12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:22222 3] [null STR text EQU good] [] 

The broker will match the events with the subscription and will finally deliver them:

    12:34:56:789 > subscriber > Received publication = [publisher:127.0.0.1:22222 1] [null STR text EQU good] []                                             
    12:34:56:789 > subscriber > Received publication = [publisher:127.0.0.1:22222 2] [null STR text EQU good] []                                             
    12:34:56:789 > subscriber > Received publication = [publisher:127.0.0.1:22222 3] [null STR text EQU good] []            

Note that the delivery of events takes place because they match the existing subscription.

Execute the following command in a new terminal window to launch a new publisher:

    java -classpath jar/*:lib/* com.zigorsalvador.phoenix.launchers.PublisherLauncher -verbose -text "bad"
    
The publisher component will discover the broker and will start publishing events.

    12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:4444 1] [null STR text EQU bad] []                                               
    12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:4444 2] [null STR text EQU bad] []                                                
    12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:4444 3] [null STR text EQU bad] []

Note these events do not match the subscription and will not be delivered by the broker.

## BrokerLauncher

The BrokerLauncher class can take a series of optional arguments:

| Option                        | Meaning                                          | Example                        |
| ------------------------------|--------------------------------------------------| -------------------------------|
| -broker \<name:address:port\> | Select the name, address and port for the broker | -broker broker:127.0.0.1:11111 |
| -master \<name:address:port\> | Connect the broker to an existing broker         | -master broker:127.0.0.1:22222 |
| -console                      | Enable console-only broker output mode           | -console                       |

Note that, unless specified with the -broker option, Phoenix will automatically choose an appropriate name, address and port for the broker. Note, as well, that Phoenix will try to discover an existing broker and connect to it, unless the -master option is used to force the connection to a broker with a specific name, address and port. Finally, note that the -console option disables the broker window.

## PublisherLauncher

The PublisherLauncher class can take a series of optional arguments:

| Option                        | Meaning                                             | Example                           |
| ------------------------------|-----------------------------------------------------| ----------------------------------|
| -client \<name:address:port\> | Select the name, address and port for the publisher | -client publisher:127.0.0.1:22222 |
| -broker \<name:address:port\> | Connect the publisher to an existing broker         | -broker broker:127.0.0.1:11111    |
| -time \<seconds\>             | Select the life span of the publisher               | -time 60                          |
| -text \<"message"\>           | Select the text that will be published              | -text "something"                 |
| -verbose                      | Enable verbose notification of events               | -verbose                          |

Note that, unless specified with the -client option, Phoenix will automatically choose an appropriate name, address and port for the publisher. Note, as well, that Phoenix will try to discover an existing broker and connect to it, unless the -broker option is used to force the connection to a broker with a specific name, address and port. The -time and -text options can be used to change the default behaviour of the publisher, which is to execute indefinitely and to publish events containing the text "good".

## SubscriberLauncher

The SubscriberLauncher class can take a series of optional arguments:

| Option                        | Meaning                                              | Example                            |
| ------------------------------|------------------------------------------------------| -----------------------------------|
| -client \<name:address:port\> | Select the name, address and port for the subscriber | -client subscriber:127.0.0.1:33333 |
| -broker \<name:address:port\> | Connect the subscriber to an existing broker         | -broker broker:127.0.0.1:11111     |
| -time \<seconds\>             | Select the life span of the subscriber               | -time 60                           |
| -text \<"message"\>           | Select the text for the subscription                 | -text "something"                  |
| -verbose                      | Enable verbose notification of events                | -verbose                           |

Note that, unless specified with the -client option, Phoenix will automatically choose an appropriate name, address and port for the subscriber. Note, as well, that Phoenix will try to discover an existing broker and connect to it, unless the -broker option is used to force the connection to a broker with a specific name, address and port. The -time and -text options can be used to change the default behaviour of the subscriber, which is to execute indefinitely and to subscribe to events containing the text "good".

## Customization

The BrokerLauncher, PublisherLauncher and SubscriberLauncher classes are helpful in order to test Phoenix. However, in order to develop your own publish/subscribe applications, you will have to design and implement your own publisher and subscriber components. In the following, I document the code for the creation of a minimalistic publisher component and its related subscriber component. Note that broker customization is not required and you can keep using the BrokerLauncher class to setup the publish/subscribe infrastructure support for the client components.

## Publisher

A custom publisher component can use the **com.zigorsalvador.phoenix.components.Publisher** class in the following way:

```java 

public class SimplePublisher 
{
	public SimplePublisher()
	{
		Address broker = DiscoveryManager.discover();
	
		if (broker != null)
		{
			Event event = new Event();
			Address client = new Address("publisher");
			Publisher publisher = new Publisher(client, broker);
			MessageLogger logger = new MessageLogger(client.getAlias());

			for (int counter = 0; counter < 100; counter++)
			{
				event.addValue(new Value(Type.INT, "counter", counter));
				SimpleSleeper.sleepMilliseconds(1000);
				publisher.publish(event);
				
				logger.println(Messages.SENT_PUBLICATION + event);
			}
			
			publisher.terminate();
		}
	}
	
	//////////
	
	public static void main(String[] args)
	{
		new SimplePublisher();
	}
}
```

First, the code performs a discovery operation to get the address of a broker in the local area network (Address broker). If a suitable broker is found, the publisher generates a valid address for itself (Address client) and instantiates a publisher component that connects to the broker that has been discovered (Publisher publisher). Then, the code enters a loop where 100 events are published with a periodicity of 1 event/second. Eventually, the loop will be exited and the publisher component terminated. The complete code for this example can be found in the **SimplePublisher** class of the **com.zigorsalvador.phoenix.examples** package. The execution of such a publisher will generate the following output:

	12:34:56:789 > publisher > Broker = broker:127.0.0.1:11111
	12:34:56:789 > publisher > Publisher = publisher:127.0.0.1:22222
	12:34:56:789 > publisher > Connection success = broker:127.0.0.1:11111
	12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:22222 1] [null INT counter EQU 0] []
	12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:22222 2] [null INT counter EQU 1] []
	12:34:56:789 > publisher > Sent publication = [publisher:127.0.0.1:22222 3] [null INT counter EQU 2] []

Note that in a real application, events will encapsulate several values or different types. For instance:

```java

event.addValue(new Value(Type.STR, "ticker", "PHNX"));
event.addValue(new Value(Type.DOU, "price", 12.3d));
event.addValue(new Value(Type.BOO, "live", true));
```

## Subscriber

A custom subscriber component can use the **com.zigorsalvador.phoenix.components.Subscriber** class in the following way:

```java 

public class SimpleSubscriber implements IListener
{
	private MessageLogger logger;

	//////////

	public SimpleSubscriber()
	{
		Address broker = DiscoveryManager.discover();

		if (broker != null)
		{
			Address client = new Address("subscriber");
			Subscriber subscriber = new Subscriber(client, broker, this);
			logger = new MessageLogger(client.getAlias());
			
			Filter subscription = new Filter();
			subscription.addPredicate(new Predicate(Type.INT, "counter", new Constraint(Relation.ANY)));
			subscriber.subscribe(subscription);
			
			SimpleSleeper.sleepSeconds(100);
			
			subscriber.unsubscribe(subscription);
			
			subscriber.terminate();
		}
	}
	
	//////////

	@Override
	
	public void notify(Event event, Boolean replay) 
	{
		logger.println(Messages.RECEIVED_PUBLICATION + event);
	}
	
	//////////
	
	public static void main(String[] args)
	{
		new SimpleSubscriber();
	}
}
```

First, the code performs a discovery operation to get the address of a broker in the local area network (Address broker). If a suitable broker is found, the subscriber generates a valid address for itself (Address client) and instantiates a subscriber component that connects to the broker that has been discovered (Subscriber subscriber). Then, the code performs a subscription using a filter that is compatible with the events produced by the related publisher. Event notifications are asynchronously handled in the notify method declared by the IListener interface. In this case, the subscriber will spend 100 seconds listening for events. Whenever an event is received, the subscriber outputs its contents to the terminal. Eventually, an unsubscription operation will be performed and the subscriber component terminated. The complete code for this example can be found in the **SimpleSubscriber** class of the **com.zigorsalvador.phoenix.examples** package. The execution of such a subscriber will generate the following output:


	12:34:56:789 > subscriber > Broker = broker:127.0.0.1:11111
	12:34:56:789 > subscriber > Subscriber = subscriber:127.0.0.1:33333
	12:34:56:789 > subscriber > Connection success = broker:127.0.0.1:11111
	12:34:56:789 > subscriber > Sent subscription = null counter INT ANY null
	12:34:56:789 > subscriber > Received publication = [publisher:127.0.0.1:22222 1] [null INT counter EQU 0] []
	12:34:56:789 > subscriber > Received publication = [publisher:127.0.0.1:22222 2] [null INT counter EQU 1] []
	12:34:56:789 > subscriber > Received publication = [publisher:127.0.0.1:22222 3] [null INT counter EQU 2] []

Note that in a real application, filters will encapsulate several predicates and constraints. For instance:

```java

subscription.addPredicate(new Predicate(Type.STR, "ticker", new Constraint(Relation.EQU, "PHNX")));
subscription.addPredicate(new Predicate(Type.DOU, "price", new Constraint(Relation.GEQ, 10.0d)));
subscription.addPredicate(new Predicate(Type.BOO, "live", new Constraint(Relation.EQU, true)));
```

## Android

The use of Phoenix in Android devices is pretty straightforward. Nevertheless, I have compiled and plan to update a list of repositories that contain sample Android publish/subscribe applications implemented on top of the Phoenix publish/subscribe middleware:

* **Mobility**: a subscriber that illustrates disconnection and reconnection support [https://github.com/zigorsalvador/mobility](https://github.com/zigorsalvador/mobility).