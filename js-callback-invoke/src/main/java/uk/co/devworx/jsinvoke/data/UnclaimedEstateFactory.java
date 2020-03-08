package uk.co.devworx.jsinvoke.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Creates the unclaimed estates from a CSV file
 * 
 * @author jsteenkamp
 *
 */
public class UnclaimedEstateFactory
{
	 static final java.util.logging.Logger logger = LogManager.getLogManager().getLogger(UnclaimedEstateFactory.class.getName());
	
	private static final AtomicReference<List<UnclaimedEstate>> estatesRef = new AtomicReference<>(); 

	
	public static List<String> getDistinctPlacesOfBirth()
	{
		return getUnclaimedEstates().stream().map(s -> s.getPlaceOfBirth().orElse("")).distinct().sorted().collect(Collectors.toList());
	}
	
	public static List<String> getDistinctPlacesOfDeath()
	{
		return getUnclaimedEstates().stream().map(s -> s.getPlaceOfDeath()).distinct().sorted().collect(Collectors.toList());
	}
	
	/**
	 * Read the unclaimed estates from the CSV in the resource items.
	 * 
	 * @return
	 */
	public static List<UnclaimedEstate> getUnclaimedEstates()
	{
		
		List<UnclaimedEstate> estates = estatesRef.get();
		if(estates != null)
		{
			return estates;
		}
		synchronized (estatesRef)
		{
			estates = estatesRef.get();
			if(estates != null)
			{
				return estates;
			}
			estates = _read_estates_from_file();
			estatesRef.set(estates);
			return estates;
		}
		
	}
	
	private static List<UnclaimedEstate> _read_estates_from_file()
	{
		final InputStream ins = UnclaimedEstateFactory.class.getResourceAsStream("/unclaimed-estates-list/UnclaimedEstatesList.csv");
		if(ins == null)
		{
			throw new RuntimeException("Unable to find the embedded CSV file.");
		}
		
		final List<UnclaimedEstate> res = new ArrayList<>();
		int rowNum = 0;
		
		try(final CSVParser parser = new CSVParser(new BufferedReader(new InputStreamReader(ins)), CSVFormat.DEFAULT))
		{
			Iterator<CSVRecord> records = parser.iterator();
			while(records.hasNext())
			{
				CSVRecord rec = records.next();
				
				rowNum++;
				if(rowNum == 1) 
				{
					continue;
				}
				
				String bvReference = rec.get(0);
				String dateOfPub = rec.get(1);
				Optional<LocalDate> dateOfPublicationDate = Optional.empty();
				Optional<String> dateOfPublicationString = Optional.empty();
				try
				{
					dateOfPublicationDate = Optional.of(LocalDate.parse(dateOfPub, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
					dateOfPublicationString = Optional.empty();
				}
				catch(Exception e)
				{
					dateOfPublicationString = Optional.of(dateOfPub);
					dateOfPublicationDate = Optional.empty();
				}
				
				String forename = rec.get(2);
				String surname = rec.get(3);
				
				if(rec.get(4).trim().isEmpty() == true) continue;
				
				LocalDate dateOfDate = LocalDate.parse( rec.get(4), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				String placeOfDeath = rec.get(5);
				Optional<LocalDate> dateOfBirth = Optional.empty();
				try
				{
					dateOfBirth = Optional.of(LocalDate.parse(rec.get(9), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				}
				catch(Exception e)
				{
					dateOfBirth = Optional.empty();
				}
				
				Optional<String> placeOfBirth = Optional.empty();
				if(rec.get(10).trim().isEmpty() == false)
				{
					placeOfBirth = Optional.of(rec.get(10));
				}

				res.add(new UnclaimedEstate(bvReference, dateOfPublicationDate, dateOfPublicationString, forename, surname, dateOfDate, placeOfDeath, dateOfBirth, placeOfBirth));
				
			}
		}
		catch(Exception e) 
		{
			throw new RuntimeException("Unable to read from the estates file." , e);
		}
		
		return res;
	}
}
