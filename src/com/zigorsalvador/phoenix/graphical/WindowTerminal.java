package com.zigorsalvador.phoenix.graphical;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;
import com.zigorsalvador.phoenix.components.Broker;
import com.zigorsalvador.phoenix.constants.Graphics;
import com.zigorsalvador.phoenix.interfaces.ITerminal;

public class WindowTerminal implements ActionListener, ITerminal
{
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu discoveryMenu;
	private JCheckBoxMenuItem unicastMenuItem;
	private JCheckBoxMenuItem multicastMenuItem;
	private JMenu statisticsMenu;
	private JMenuItem saveMenuItem;
	private JTabbedPane tabbedPane;

	private GeneralPanel generalPanel;
	private TrafficPanel trafficPanel;
	private ControlPanel controlPanel;
	private RoutingPanel routingPanel;
	private MatchingPanel matchingPanel;
	private ProcessorPanel processorPanel;
	private MemoryPanel memoryPanel;

	//////////
	
	private int incomingEvents;
	private int outgoingEvents;
	private int incomingSignals;
	private int outgoingSignals;
	private int positiveMatches;
	private int negativeMatches;	
	private int amountOfFilters;	
	private int amountOfClients;
	
	private float processorUtilization;
	private float memoryUtilization;
		
	//////////

	private GraphicsConfiguration screen;
	
	private JavaSysMon systemMonitor;
	private CpuTimes newCpuTimes;
	private CpuTimes oldCpuTimes;
	
	private Integer xsize = 720;
	private Integer ysize = 431;

	private Integer xpos;
	private Integer ypos;
	
	//////////

	private Broker broker;
	
	//////////
	
	public WindowTerminal(Broker broker)
	{
		this.broker = broker;
		
		try
		{
			SwingUtilities.invokeAndWait(new Runnable(){public void run(){createAndShowGUI();}});
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
	}
	
	//////////
	
	private void createAndShowGUI()
	{
		setupCoordinates();
		setupTabbedPane();
		setupMenuBar();
		setupTimers();
		setupFrame();
	}
	
	//////////
	
	private void setupCoordinates()
	{
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = environment.getDefaultScreenDevice();
		screen = device.getDefaultConfiguration();
		
		Rectangle bounds = screen.getBounds();
		
		Integer xorigin = (int) bounds.getX();
		Integer yorigin = (int) bounds.getY();
		
		Integer xres = (int) bounds.getWidth(); 
		Integer yres = (int) bounds.getHeight();
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Insets insets = toolkit.getScreenInsets(screen);
		
		xres = xres - insets.left - insets.right;
		yres = yres - insets.top - insets.bottom;
		
		xpos = (int) (xorigin + insets.left + (0.5 * xres - xsize / 2)); 
		ypos = (int) (yorigin + insets.top + (0.5 * yres - ysize / 2));
	}
	
	//////////
	
	private void setupTabbedPane()
	{	
		tabbedPane = new JTabbedPane();
		
		generalPanel = new GeneralPanel();
		trafficPanel = new TrafficPanel();
		controlPanel = new ControlPanel();
		routingPanel = new RoutingPanel();
	    matchingPanel = new MatchingPanel();
		processorPanel = new ProcessorPanel();
		memoryPanel = new MemoryPanel();
		
		ImageIcon infoIcon = null;
		ImageIcon plotIcon = null;
		
		URL infoIconURL = ClassLoader.getSystemResource("images" + File.separator + "info.png");
		URL plotIconURL = ClassLoader.getSystemResource("images" + File.separator + "plot.png");
		
		if (infoIconURL != null) infoIcon = new ImageIcon(infoIconURL);
		if (plotIconURL != null) plotIcon = new ImageIcon(plotIconURL);
		
		tabbedPane.addTab("General", infoIcon, generalPanel);
		tabbedPane.addTab("Traffic", plotIcon, trafficPanel);
		tabbedPane.addTab("Control", plotIcon, controlPanel);
		tabbedPane.addTab("Routing", plotIcon, routingPanel);
		tabbedPane.addTab("Matching", plotIcon, matchingPanel);
		tabbedPane.addTab("Processor", plotIcon, processorPanel);
		tabbedPane.addTab("Memory", plotIcon, memoryPanel);
		
		tabbedPane.setFocusable(false);
		tabbedPane.setLocation(0, 5);
		tabbedPane.setSize(720, 407);
	}
	
	//////////
	
	private void setupMenuBar()
	{	
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		menuBar = new JMenuBar();
		discoveryMenu = new JMenu("Discovery");
		statisticsMenu = new JMenu("Statistics");
		
		saveMenuItem = new JMenuItem("Save");
		unicastMenuItem = new JCheckBoxMenuItem("Unicast");
		multicastMenuItem = new JCheckBoxMenuItem("Multicast");

		saveMenuItem.addActionListener(this);
		unicastMenuItem.addActionListener(this);
		multicastMenuItem.addActionListener(this);
		
		discoveryMenu.add(unicastMenuItem);
		discoveryMenu.add(multicastMenuItem);
		statisticsMenu.add(saveMenuItem);
		
		menuBar.add(discoveryMenu);
		menuBar.add(statisticsMenu);
	}
	
	//////////
	
	private void setupTimers()
	{
	    systemMonitor = new JavaSysMon();
	    oldCpuTimes = systemMonitor.cpuTimes();
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(new Runnable(){public void run(){periodicUpdate();}}, 0, 1000, TimeUnit.MILLISECONDS);
	}
	
	//////////
	
	private void setupFrame()
	{	
		frame = new JFrame(screen);
		
		frame.add(tabbedPane);
		frame.setJMenuBar(menuBar);
		
		frame.setLayout(null);	
		frame.setResizable(false);
		frame.setSize(xsize, ysize);
		frame.setLocation(xpos, ypos);
		frame.setTitle(broker.getAddress().toString());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}
	
	//////////
	
	public void periodicUpdate()
	{		
		trafficPanel.update(incomingEvents, outgoingEvents);
		controlPanel.update(incomingSignals, outgoingSignals);
		routingPanel.update(amountOfFilters, amountOfClients);
		matchingPanel.update(positiveMatches, negativeMatches);
		processorPanel.update(processorUtilization);
		memoryPanel.update(memoryUtilization);

		incomingEvents = 0;
		outgoingEvents = 0;
		incomingSignals = 0;
		outgoingSignals = 0;
		positiveMatches = 0;
		negativeMatches = 0;
		
		setProcessorUtilization();
		setMemoryUtilization();
		
		unicastMenuItem.setSelected(broker.isUnicastDiscoverable());
		multicastMenuItem.setSelected(broker.isMulticastDiscoverable());
	}
	
	//////////
	
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == saveMenuItem)
		{
			File folder = new File(Graphics.FOLDER + File.separator + broker.getAddress().getAlias());
	
			folder.mkdirs();
					
			FileOperations.writeText(trafficPanel.getData(), folder + File.separator + "traffic");
			FileOperations.writeGraphic(trafficPanel.getPlot(), folder + File.separator + "traffic");
			FileOperations.writeText(controlPanel.getData(), folder + File.separator + "control");
			FileOperations.writeGraphic(controlPanel.getPlot(), folder + File.separator + "control");
			FileOperations.writeText(routingPanel.getData(), folder + File.separator + "routing");
			FileOperations.writeGraphic(routingPanel.getPlot(), folder + File.separator + "routing");
			FileOperations.writeText(matchingPanel.getData(), folder + File.separator + "matching");
			FileOperations.writeGraphic(matchingPanel.getPlot(), folder + File.separator + "matching");
			FileOperations.writeText(processorPanel.getData(), folder + File.separator + "processor");
			FileOperations.writeGraphic(processorPanel.getPlot(), folder + File.separator + "processor");
			FileOperations.writeText(memoryPanel.getData(), folder + File.separator + "memory");
			FileOperations.writeGraphic(memoryPanel.getPlot(), folder + File.separator + "memory");
		}
		else if (event.getSource() == unicastMenuItem)
		{
			if (unicastMenuItem.isSelected() == false)
			{
				broker.stopUnicastListener();
			}
			else
			{
				broker.startUnicastListener();
			}
		}
		else if (event.getSource() == multicastMenuItem)
		{
			if (multicastMenuItem.isSelected() == false)
			{
				broker.stopMulticastListener();
			}
			else
			{
				broker.startMulticastListener();
			}
		}
	}	

	//////////	
	
	@Override
	
	public void setAmountOfFilters(Integer value)
	{
		this.amountOfFilters = value;
	}
	
	//////////
	
	@Override
	
	public void setAmountOfClients(Integer value)
	{
		this.amountOfClients = value;
	}
	
	//////////	
	
	@Override
	
	public void increaseIncomingEvents(Integer delta) 
	{
		this.incomingEvents += delta;
	}

	//////////	
	
	@Override
	
	public void increaseOutgoingEvents(Integer delta) 
	{
		this.outgoingEvents += delta;
	}
	
	//////////	

	@Override
	
	public void increaseIncomingSignals(Integer delta)
	{
		this.incomingSignals += delta;
	}
	
	//////////	
	
	@Override
	
	public void increaseOutgoingSignals(Integer delta)
	{
		this.outgoingSignals += delta;
	}

	//////////	
	
	@Override
	
	public void increasePositiveMatches(Integer delta)
	{
		this.positiveMatches += delta;
	}

	//////////	
	
	@Override
	
	public void increaseNegativeMatches(Integer delta)
	{
		this.negativeMatches += delta;
	}
	
	//////////
	
	@Override
	
	public void setLocalBrokers(final Vector<Vector<Object>> rows)
	{
		SwingUtilities.invokeLater(new Runnable(){public void run(){generalPanel.setLocalBrokers(rows);}});
	}
	
	//////////
	
	@Override
	
	public void setLocalSubscribers(final Vector<Vector<Object>> rows)
	{
		SwingUtilities.invokeLater(new Runnable(){public void run(){generalPanel.setLocalSubscribers(rows);}});
	}
	
	//////////	
	
	@Override
	
	public void setGlobalSubscriptions(final Vector<Vector<Object>> rows)
	{
		SwingUtilities.invokeLater(new Runnable(){public void run(){generalPanel.setGlobalSubscriptions(rows);}});
	}
		
	//////////
		
	private void setProcessorUtilization()
	{
		newCpuTimes = systemMonitor.cpuTimes();
		processorUtilization = newCpuTimes.getCpuUsage(oldCpuTimes) * 100;
		oldCpuTimes = newCpuTimes;
	}
	
	//////////
	
	private void setMemoryUtilization()
	{
		long totalMemory = Runtime.getRuntime().totalMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		memoryUtilization = (float) ((totalMemory - freeMemory) / (1024 * 1024));
	}
}