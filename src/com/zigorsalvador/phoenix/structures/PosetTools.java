package com.zigorsalvador.phoenix.structures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zigorsalvador.phoenix.covering.FilterCovering;
import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.matching.EventMatching;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;

public class PosetTools 
{
	protected static Set<PosetNode> directCoveringNodes(PosetNode node, Filter filter)
	{
		Set<PosetNode> coveringNodes = coveringNodes(node, filter);
		Set<PosetNode> toRemove = new HashSet<PosetNode>();		
				
		for (PosetNode candidate : coveringNodes)
		{
			for (PosetNode omega : candidate.getOmegas())
			{
				if (coveringNodes.contains(omega))
				{
					toRemove.add(candidate);
					break;
				}
			}
		}
		
		coveringNodes.removeAll(toRemove);

		return coveringNodes;
	}
	
	//////////
		
	protected static Set<PosetNode> directCoveredNodes(PosetNode node, Filter filter)
	{
		Set<PosetNode> coveredNodes = coveredNodes(node, filter);
		Set<PosetNode> toRemove = new HashSet<PosetNode>();
		
		for (PosetNode candidate : coveredNodes)
		{
			for (PosetNode alpha : candidate.getAlphas())
			{
				if (coveredNodes.contains(alpha))
				{
					toRemove.add(candidate);
					break;
				}
			}
		}
		
		coveredNodes.removeAll(toRemove);
		
		return coveredNodes;
	}
	
	//////////
		  
	protected static Set<PosetNode> coveringNodes(PosetNode node, Filter filter) // NOTE: Recursive...
	{
		Set<PosetNode> result = new HashSet<PosetNode>();
		  
		if (FilterCovering.covers(node.getFilter(), filter))
		{
			result.add(node);
			
			for (PosetNode omega : node.getOmegas())
			{
				result.addAll(coveringNodes(omega, filter));
			}
		}			
		
		return result;
	}

	//////////
	  
	protected static Set<PosetNode> coveredNodes(PosetNode node, Filter filter) // NOTE: Recursive...
	{
		Set<PosetNode> result = new HashSet<PosetNode>();
		  
		if (FilterCovering.covers(filter, node.getFilter()))
		{
			result.add(node);
		}
		
		for (PosetNode omega : node.getOmegas())
		{
			result.addAll(coveredNodes(omega, filter));
		}
			
		return result;
	}
	
	//////////
    
	protected static Set<Address> matchingSubscribers(PosetNode node, Event event, Set<Address> interfaces, ITerminal window) // NOTE: Recursive...
	{
		Set<Address> result = new HashSet<Address>();
		
		if (EventMatching.matches(event, node.getFilter()))
		{
			window.increasePositiveMatches(1);			
			
			List<Address> subscribers = node.getSubscribers();
			
			result.addAll(subscribers);
			interfaces.removeAll(subscribers);
			if (interfaces.isEmpty()) return result;
			
			for (PosetNode omega : node.getOmegas())
			{			
				Set<Address> subResult = matchingSubscribers(omega, event, interfaces, window);
				
				result.addAll(subResult);
				interfaces.removeAll(subResult);
				if (interfaces.isEmpty()) return result;
			}
		}
		else
		{
			window.increaseNegativeMatches(1);
		}
			
		return result;
	}
	
	//////////
	    
	protected static Set<String> matchingDigests(PosetNode node, Event event, ITerminal window) // NOTE: Recursive...
	{
		Set<String> result = new HashSet<String>();
		
		if (EventMatching.matches(event, node.getFilter()))
		{
			window.increasePositiveMatches(1);
			
			result.add(node.getDigest());
			
			for (PosetNode omega : node.getOmegas())
			{
				result.addAll(matchingDigests(omega, event, window));
			}
		}
		else
		{
			window.increaseNegativeMatches(1);
		}
		
		return result;
	}

	//////////
	
	protected static Set<PosetNode> subposetNodes(PosetNode node)
	{
		Set<PosetNode> result = new HashSet<PosetNode>();
		
		result.add(node);
		
		for (PosetNode omega : node.getOmegas())
		{
			result.addAll(subposetNodes(omega));
		}
		
		return result;
	}
}