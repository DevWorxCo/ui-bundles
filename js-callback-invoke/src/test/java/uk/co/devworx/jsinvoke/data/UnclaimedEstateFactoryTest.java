package uk.co.devworx.jsinvoke.data;

import java.util.List;

import org.junit.jupiter.api.Test;

public class UnclaimedEstateFactoryTest
{
	
	@Test
	public void parseItems()
	{
		List<UnclaimedEstate> estates = UnclaimedEstateFactory.getUnclaimedEstates();
		System.out.println("Number of Estates : " + estates.size());
		
		
	}
	

}
