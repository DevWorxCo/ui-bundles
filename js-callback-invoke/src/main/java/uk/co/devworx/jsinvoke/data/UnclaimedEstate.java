package uk.co.devworx.jsinvoke.data;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Describes an unclaimed estate. 
 * 
 * @author jsteenkamp
 *
 */
public class UnclaimedEstate
{
	private final String bvReference;
	private final Optional<LocalDate> dateOfPublicationDate;
	private final Optional<String> dateOfPublicationString;
	private final String forename;
	private final String surname;
	private final LocalDate dateOfDeath;
	private final String placeOfDeath;
	private final Optional<LocalDate> dateOfBirth;
	private final Optional<String> placeOfBirth;
	
	UnclaimedEstate(String bvReference, Optional<LocalDate> dateOfPublicationDate, Optional<String> dateOfPublicationString, String forename, String surname, 
	                LocalDate dateOfDeath, String placeOfDeath, Optional<LocalDate> dateOfBirth, Optional<String> placeOfBirth)
	{
		super();
		this.bvReference = bvReference;
		this.dateOfPublicationDate = dateOfPublicationDate;
		this.dateOfPublicationString = dateOfPublicationString;
		this.forename = forename;
		this.surname = surname;
		this.dateOfDeath = dateOfDeath;
		this.placeOfDeath = placeOfDeath;
		this.dateOfBirth = dateOfBirth;
		this.placeOfBirth = placeOfBirth;
	}

	public String getBvReference()
	{
		return bvReference;
	}

	public Optional<LocalDate> getDateOfPublicationDate()
	{
		return dateOfPublicationDate;
	}

	public Optional<String> getDateOfPublicationString()
	{
		return dateOfPublicationString;
	}

	public String getForename()
	{
		return forename;
	}

	public String getSurname()
	{
		return surname;
	}

	public LocalDate getDateOfDeath()
	{
		return dateOfDeath;
	}

	public String getPlaceOfDeath()
	{
		return placeOfDeath;
	}

 
	public Optional<LocalDate> getDateOfBirth()
	{
		return dateOfBirth;
	}

	public Optional<String> getPlaceOfBirth()
	{
		return placeOfBirth;
	}
	

}
