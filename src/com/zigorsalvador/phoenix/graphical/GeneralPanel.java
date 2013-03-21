package com.zigorsalvador.phoenix.graphical;

import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GeneralPanel extends JPanel
{
	private static final long serialVersionUID = -5106193737624721401L;

	//////////
	
	private JTextField filterTextField;
	
	private JTable localBrokersTable;
	private JTable localSubscribersTable;
	private JTable globalSubscriptionsTable;
	
	private JScrollPane localBrokersScrollPane;
	private JScrollPane localSubscribersScrollPane;
	private JScrollPane globalSubscriptionsScrollPane;
		
	private DefaultTableModel localBrokersTableModel;
	private DefaultTableModel localSubscribersTableModel;
	private DefaultTableModel globalSubscriptionsTableModel;
	
	//////////
	
	public GeneralPanel()
	{
		this.setLayout(null);
				
		createFilterTextField();
		createLocalBrokersTable();
		createLocalSubscribersTable();
		createGlobalSubscriptionsTable();
				
		this.add(filterTextField);
		this.add(localBrokersScrollPane);
		this.add(localSubscribersScrollPane);
		this.add(globalSubscriptionsScrollPane);
	}
	
	//////////
	
	public void createFilterTextField()
	{
		filterTextField = new JTextField ();
		
		filterTextField.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		filterTextField.setFocusable(false);
		filterTextField.setLocation(7, 326);
		filterTextField.setSize(685, 24);
	}
		
	//////////
	
	public void createLocalBrokersTable()
	{
		Vector<String> headings = new Vector<String>();
		
		headings.add("Local brokers");

		localBrokersTableModel = new DefaultTableModel(0, headings.size()) ;
		localBrokersTableModel.setColumnIdentifiers(headings);
		
		localBrokersTable = new JTable(localBrokersTableModel);
		localBrokersTable.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		localBrokersTable.setFocusable(false);
		localBrokersTable.setEnabled(false);
		
		localBrokersScrollPane = new JScrollPane(localBrokersTable);
		localBrokersScrollPane.setVerticalScrollBarPolicy(22);
		localBrokersScrollPane.setLocation(10, 10);
		localBrokersScrollPane.setSize(334, 100);
	}

	//////////
	
	public void createLocalSubscribersTable()
	{
		Vector<String> headings = new Vector<String>();
		
		headings.add("Local subscribers");

		localSubscribersTableModel = new DefaultTableModel(0, headings.size()) ;
		localSubscribersTableModel.setColumnIdentifiers(headings);
		
		localSubscribersTable = new JTable(localSubscribersTableModel);
		localSubscribersTable.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		localSubscribersTable.setFocusable(false);
		localSubscribersTable.setEnabled(false);
		
		localSubscribersScrollPane = new JScrollPane(localSubscribersTable);
		localSubscribersScrollPane.setVerticalScrollBarPolicy(22);
		localSubscribersScrollPane.setLocation(355, 10);
		localSubscribersScrollPane.setSize(334, 100);
	}

	//////////
	
	public void createGlobalSubscriptionsTable()
	{
		Vector<String> headings = new Vector<String>();
		
		headings.add("Subscriber");
		headings.add("Interface");
		headings.add("Filter");
		headings.add("Hidden");
		
		globalSubscriptionsTableModel = new DefaultTableModel(0, headings.size()) ;
		globalSubscriptionsTableModel.setColumnIdentifiers(headings);
		
		globalSubscriptionsTable = new JTable(globalSubscriptionsTableModel);
		globalSubscriptionsTable.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		globalSubscriptionsTable.setFocusable(false);
		globalSubscriptionsTable.setEnabled(false);

		globalSubscriptionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		globalSubscriptionsTable.getColumnModel().getColumn(0).setPreferredWidth(221);
		globalSubscriptionsTable.getColumnModel().getColumn(1).setPreferredWidth(222);
		globalSubscriptionsTable.getColumnModel().getColumn(2).setPreferredWidth(238);
		
		globalSubscriptionsScrollPane = new JScrollPane(globalSubscriptionsTable);
		globalSubscriptionsScrollPane.setHorizontalScrollBarPolicy(31);		
		globalSubscriptionsScrollPane.setVerticalScrollBarPolicy(22);
		globalSubscriptionsScrollPane.setLocation(10, 120);
		globalSubscriptionsScrollPane.setSize(679, 196);

		globalSubscriptionsTable.addMouseListener(new MouseListener(this)); 
	}

	//////////
	
	public void setLocalBrokers(Vector<Vector<Object>> rows)
	{
		while (localBrokersTableModel.getRowCount() > 0)
		{
			localBrokersTableModel.removeRow(0);
		}
		
		Iterator <Vector<Object>> iterator = rows.iterator();
		
		while (iterator.hasNext())
		{
			localBrokersTableModel.addRow(iterator.next());
		}
	}
	
	//////////
	
	public void setLocalSubscribers(final Vector<Vector<Object>> rows)
	{
		while (localSubscribersTableModel.getRowCount() > 0)
		{
			localSubscribersTableModel.removeRow(0);
		}
		
		Iterator <Vector<Object>> iterator = rows.iterator();
		
		while (iterator.hasNext())
		{
			localSubscribersTableModel.addRow(iterator.next());
		}
	}	
	
	//////////
		
	public void setGlobalSubscriptions(Vector<Vector<Object>> rows)
	{	
		filterTextField.setText("");
		
		while (globalSubscriptionsTableModel.getRowCount() > 0)
		{
			globalSubscriptionsTableModel.removeRow(0);
		}
		
		Iterator <Vector<Object>> iterator = rows.iterator();
		
		while (iterator.hasNext())
		{
			globalSubscriptionsTableModel.addRow(iterator.next());
		}		
	}	
	
	//////////
	
	public void setFilterLabel(Point point)
	{
		int row = globalSubscriptionsTable.rowAtPoint(point);
        int column = globalSubscriptionsTable.columnAtPoint(point);
        
        if (row > -1 && column > -1)
        {
        	filterTextField.setText(globalSubscriptionsTableModel.getValueAt(row, 3).toString());
        }
	}
}