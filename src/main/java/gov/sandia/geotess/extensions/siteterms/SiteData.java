package gov.sandia.geotess.extensions.siteterms;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import gov.sandia.geotess.AttributeDataDefinitions;
import gov.sandia.geotess.Data;
import gov.sandia.gmp.util.globals.GMTFormat;
import gov.sandia.gmp.util.globals.Globals;
import gov.sandia.gmp.util.numerical.vector.EarthShape;

/**
 * Container class for site terms. Presumably by tomographic
 * inversion.  These objects don't have a reference to a station
 * name because it is presumed that they will be the values in 
 * a Map&lt;String, ArrayList&lt;SiteTerm&gt;&gt; where the keys are the station
 * names and the values are the list of station locations at 
 * different on/off times. 
 * 
 */
public class SiteData
{
	/**
	 * The location of the station to which the site term is applicable.
	 */
	protected double[] stationLocation;

	/**
	 * The radius of the station to which the site term is applicable.
	 */
	protected double   stationRadius;

	/**
	 * The jdate when the station became active at specified position.
	 */
	protected int onDate;

	/**
	 * The jdate when the station stopped being active at specified position.
	 */
	protected int offDate;

	/**
	 * site term in seconds.  Add this to a calculated prediction.
	 */
	protected Data   data;

	/**
	 * Constructor that takes the Site object for which a site term is to be
	 * evaluated.
	 * 
	 * @param site The site at which the site term was evaluated.
	 * @param attrDef
	 */
	public SiteData(Site site, AttributeDataDefinitions attrDef) throws IOException
	{
		this(site.getUnitVector(), site.getRadius(),
				(int)site.getOndate(),
				(int)site.getOffdate(), attrDef);
	}

    /**
     * Constructor that takes the content of a Site object for
     * which a site term is to be evaluated.
     * 
     * @param siteUnitVector The lateral location of the site for which the site
     *                       term is applicable.
     * @param siteRadius     The radius (km) of the site for which the site term
     *                       is applicable.
     * @param onDate         The jdate when the site became active at
     *                       specified position.
     * @param offDate        The jdate when the site stopped being active at
     *                       specified position.
     * @throws IOException
     */
    public SiteData(double[] siteUnitVector, double siteRadius,
            int onDate, int offDate,
            AttributeDataDefinitions attrDef) throws IOException
    {
        stationLocation = siteUnitVector.clone();
        stationRadius   = siteRadius;
        this.onDate = onDate;
        this.offDate = offDate;
        data = Data.getData(attrDef);
        testOnOffDates();
    }

    /**
     * Constructor that takes the content of a Site object for
     * which a site term is to be evaluated.
     * 
     * @param siteUnitVector The lateral location of the site for which the site
     *                       term is applicable.
     * @param siteRadius     The radius (km) of the site for which the site term
     *                       is applicable.
     * @param onTime         The epoch time when the site became active at
     *                       specified position.
     * @param offTime        The epoch time when the site stopped being active at
     *                       specified position.
     * @throws IOException
     */
    public SiteData(double[] siteUnitVector, double siteRadius,
            double onTime, double offTime,
            AttributeDataDefinitions attrDef) throws IOException
    {
        stationLocation = siteUnitVector.clone();
        stationRadius   = siteRadius;
        this.onDate = GMTFormat.getJDate(onTime);
        this.offDate = GMTFormat.getJDate(offTime);
        data = Data.getData(attrDef);
        testOnOffDates();
    }

	/**
	 * Constructor that reads a site term from a DataInputStream.
	 * 
	 * @param input      The DataInputStream from which the site terms is read.
	 * @param earthShape The EarthShape for the site defining this site term.
	 * @param attrDef    The data definition for the site terms to be stored on
	 *                   this SiteData object.
	 * @param formatVersion if == 1 then double onTime, offTime are read from the
	 *                   input, otherwise, integer onDate, offDate are read.
	 * @throws IOException
	 */
	public SiteData(DataInputStream input, EarthShape earthShape,
			AttributeDataDefinitions attrDef, int formatVersion)
					throws IOException 
	{
		// get lateral unit vector position and radius of the site term. The site
		// radius is stored as an elevation. The latitude and longitude or stored
		//in degrees.
		stationLocation = earthShape.getVectorDegrees(input.readDouble(),
				input.readDouble());
		stationRadius   = earthShape.getEarthRadius(stationLocation) +
				input.readDouble();

		// read the on time, off time, and site term
		if (formatVersion == 1)
		{
		  // version 1 stored onTime and offTime; epoch time in seconds.
          onDate = GMTFormat.getJDate(input.readDouble());
          offDate = GMTFormat.getJDate(input.readDouble());
		}
		else
		{
		  // versions after version 1 store ondate and offdate as julian dates
          onDate = input.readInt();
          offDate = input.readInt();
		}

        testOnOffDates();

		data = Data.getData(input, attrDef);
	}

    /**
     * Constructor that reads a site term from a Scanner.
     * 
     * @param input      The ASCII Scanner from which the site term is read.
     * @param earthShape The EarthShape for the site defining this site term.
     * @param attrDef    The data definition for the site terms to be stored on
     *                   this SiteData object.
     * @throws IOException
     */
    public SiteData(Scanner input, EarthShape earthShape, 
            AttributeDataDefinitions attrDef, int formatVersion)
                    throws IOException 
    {
        // get lateral unit vector position and radius of the site term. The site
        // radius is stored as an elevation. The latitude and longitude or stored
        //in degrees.
        stationLocation = earthShape.getVectorDegrees(input.nextDouble(),
                input.nextDouble());
        stationRadius   = earthShape.getEarthRadius(stationLocation) +
                input.nextDouble();
        
        String next1 = input.next();
        String next2 = input.next();
        
        if (formatVersion == 1)
        {
          // version 1 stored onTime and offTime; epoch time in seconds.
          onDate = GMTFormat.getJDate(Double.parseDouble(next1));
          offDate = GMTFormat.getJDate(Double.parseDouble(next2));
        }
        else
        {
          // versions after version 1 store ondate and offdate as julian dates
          onDate = Integer.parseInt(next1);
          offDate = Integer.parseInt(next2);
        }
        
        testOnOffDates();

        data = Data.getData(input, attrDef);
    }

    /**
     * Constructor that reads a site term from a Scanner.
     * 
     * @param input      The ASCII Scanner from which the site term is read.
     * @param earthShape The EarthShape for the site defining this site term.
     * @param attrDef    The data definition for the site terms to be stored on
     *                   this SiteData object.
     * @throws IOException
     */
    public SiteData(Scanner input, EarthShape earthShape, 
            AttributeDataDefinitions attrDef)
                    throws IOException 
    {
        // get lateral unit vector position and radius of the site term. The site
        // radius is stored as an elevation. The latitude and longitude or stored
        //in degrees.
        stationLocation = earthShape.getVectorDegrees(input.nextDouble(),
                input.nextDouble());
        stationRadius   = earthShape.getEarthRadius(stationLocation) +
                input.nextDouble();
        
        String next1 = input.next();
        String next2 = input.next();
        
        try
        {
          onDate = Integer.parseInt(next1);
          offDate = Integer.parseInt(next2);
        }
        catch (NumberFormatException ex)
        {
          onDate = GMTFormat.getJDate(Double.parseDouble(next1));
          offDate = GMTFormat.getJDate(Double.parseDouble(next2));
        }
        
        testOnOffDates();

        data = Data.getData(input, attrDef);
    }
    
    private void testOnOffDates() throws IOException
    {
      if (onDate < Site.ONDATE_NA || offDate > Site.OFFDATE_NA)
        throw new IOException("onDate = "+onDate+" is out of range.");

      if (offDate < Site.ONDATE_NA || offDate > Site.OFFDATE_NA)
        throw new IOException("offDate = "+offDate+" is out of range.");

    }

	/**
	 * Returns true if this SiteData is equivalent in content to the
	 * input SiteData. If the input object is null, or not a
	 * SiteData, or has different content then false is returned.
	 * 
	 * @return True if this SiteData is equivalent in content to the
	 *         input SiteData.
	 */
	@Override
	public boolean equals(Object other)
	{
		// return false if the input object is null or not a SiteData
		// object
		if ((other == null) || !(other instanceof SiteData))
			return false;

		// return false if the content of the input SiteData object is not
		// the same as this SiteData object.
		SiteData otherSD = (SiteData) other;
		
		return otherSD.onDate == onDate && otherSD.offDate  == offDate
				&& otherSD.data.equals(data);
	}

	/**
	 * Read just the station name from an ASCII Scanner file.
	 *  
	 * @param input The ASCII Scanner from which the site term is read.
	 * @return The station name read from the input scanner. 
	 */
	static public String readStationName(Scanner input) 
	{
		return input.next();
	}

	/**
	 * Read just the station name from a DataInputStream.

	 * @param input The DataInputStream from which the site term is read.
	 * @return The station name read from the DataInputStream. 
	 * @throws IOException
	 */
	static public String readStationName(DataInputStream input) throws IOException 
	{
		return Globals.readString(input);
	}

	/**
	 * Output the site and it's site term to the DataOutputStream.
	 * 
	 * @param output      The DataOutputStream into which the site and it's site
	 *                    term are written. The name of the site for which this
	 *                    SiteData object was created.
	 * @param stationName The name of the site for which this SiteData
	 *                    object was created.
	 * @param earthShape  The EarthShape for the site defining this site term.
	 * @throws IOException
	 */
	protected void write(DataOutputStream output, String stationName,
			EarthShape earthShape) 
					throws IOException
	{
		Globals.writeString(output, stationName);
		output.writeDouble(earthShape.getLatDegrees(stationLocation));
		output.writeDouble(earthShape.getLonDegrees(stationLocation));
		output.writeDouble(stationRadius - earthShape.getEarthRadius(stationLocation));
		output.writeInt(onDate);
		output.writeInt(offDate);
		data.write(output);
	}

	/**
	 * Output the site and it's site term to the Writer ASCII file.
	 * 
	 * @param output      The Writer into which the site and it's site term
	 *                    are written. The name of the site for which this
	 *                    SiteData object was created.
	 * @param stationName The name of the site for which this SiteData
	 *                    object was created.
	 * @param earthShape  The EarthShape for the site defining this site term.
	 * @throws IOException
	 */
	public void write(Writer output, String stationName,
			EarthShape earthShape) throws IOException 
	{
		output.write(stationName+" "+toString(earthShape)+"\n");
	}

	/**
	 * Returns the sites position string that owns this site term.
	 * 
	 * @param earthShape The EarthShape for the site defining this site term.
	 * @return The sites position string that owns this site term.
	 */
	public String getPositionString(EarthShape earthShape)
	{
		return String.format("%s %8.3f",
				earthShape.getLatLonString(stationLocation),
				stationRadius -
				earthShape.getEarthRadius(stationLocation));
	}

    /**
     * Returns a string defining this SiteData contents.
     * 
     * @return A string defining this SiteData contents.
     */
	@Override
    public String toString()
    {
      return toString(EarthShape.WGS84);
    }
    
    /**
     * Returns a string defining this SiteData contents.
     * 
     * @param earthShape The EarthShape for the site defining this site term.
     * @return A string defining this SiteData contents.
     */
    public String toString(EarthShape earthShape)
	{
		return String.format("%s %7d %7d %s", 
				getPositionString(earthShape),
				onDate, offDate, data.toString());
	}

	/**
	 * Returns this SiteData lateral unit vector position.
	 * 
	 * @return This SiteData lateral unit vector position.
	 */
	public double[] getStationLocation()
	{
		return stationLocation;
	}

	/**
	 * Sets this SiteData lateral unit vector position into the input
	 * vector.
	 * 
	 * @param stationLocation Contains the lateral unit vector position on
	 *                        output.
	 */
	public void setStationLocation(double[] stationLocation)
	{
		this.stationLocation = stationLocation;
	}

	/**
	 * Returns this SiteData site radius (km).
	 * 
	 * @return This SiteData site radius (km).
	 */
	public double getStationRadius()
	{
		return stationRadius;
	}

	/**
	 * Sets this SiteData radius (km).
	 * 
	 * @param stationRadius The new site radius (km).
	 */
	public void setStationRadius(double stationRadius)
	{
		this.stationRadius = stationRadius;
	}

    /**
     * Turn on date. Date on which the station, or sensor indicated began
     * operating. The columns offdate and ondate are not intended to accommodate
     * temporary downtimes, but rather to indicate the time period for which the
     * columns of the station (&lt;I&gt;lat&lt;/I&gt;, &lt;I&gt;lon&lt;/I&gt;, &lt;I&gt;elev&lt;/I&gt;,) are valid
     * for the given station code. Stations are often moved, but with the
     * station code remaining unchanged.
     * 
     * @return ondate
     */
    public long getOnDate() {
        return onDate;
    }

    /**
     * Turn on date. Date on which the station, or sensor indicated began
     * operating. The columns offdate and ondate are not intended to accommodate
     * temporary downtimes, but rather to indicate the time period for which the
     * columns of the station (&lt;I&gt;lat&lt;/I&gt;, &lt;I&gt;lon&lt;/I&gt;, &lt;I&gt;elev&lt;/I&gt;,) are valid
     * for the given station code. Stations are often moved, but with the
     * station code remaining unchanged.
     * 
     * @param onDate
     * @return reference to this
     */
    public SiteData setOnDate(int onDate) {
        this.onDate = onDate;
        return this;
    }

    /**
     * Turn off date. This column is the Julian Date on which the station or
     * sensor indicated was turned off, dismantled, or moved (see &lt;I&gt;ondate&lt;/I&gt;)
     * 
     * @return offdate
     */
    public int getOffDate() {
        return offDate;
    }

    /**
     * Turn off date. This column is the Julian Date on which the station or
     * sensor indicated was turned off, dismantled, or moved (see &lt;I&gt;ondate&lt;/I&gt;)
     * 
     * @param offDate
     * @return reference to this
     */
    public SiteData setOffDate(int offDate) {
        this.offDate = offDate;
        return this;
    }

	/**
	 * Returns the site epoch on time in seconds.
	 * 
	 * @return The site epoch on time in seconds.
	 */
	public double getOnTime()
	{
		return GMTFormat.getEpochTime(onDate);
	}

	/**
	 * Sets this SiteData epoch on time (sec).
	 *  
	 * @param epochTime The new epoch on time for this SiteData object.
	 */
	public void setOnTime(double epochTime)
	{
		this.onDate = GMTFormat.getJDate(epochTime);
	}

	/**
	 * Returns the site epoch off time in seconds.  Convert a jdate (int yyyyddd) 
	 * into an epoch time (double seconds since 1970). Before returning it, 1 day 
	 * minus 1 millisecond is added to the epoch time.
	 * 
	 * @return The site epoch off time in seconds.
	 */
	public double getOffTime()
	{
		return GMTFormat.getOffTime(offDate);
	}

	/**
	 * Sets this SiteData epoch off time (sec).
	 *  
	 * @param epochTime The new epoch on time for this SiteData object.
	 */
	public void setOffTime(double epochTime)
	{
		this.onDate = GMTFormat.getJDate(epochTime);
	}

	/**
	 * Returns the site term in seconds.
	 * 
	 * @return The site term in seconds.
	 */
	public double getSiteTerm(int attributeIndex)
	{
		return data.getDouble(attributeIndex);
	}

	/**
	 * Sets this SiteData site term (sec).
	 *  
	 * @param siteTerm The new site term for this SiteData object.
	 */
	public void setSiteTerm(int attributeIndex, double siteTerm)
	{
		data.setValue(attributeIndex, siteTerm);
	}

    /**
     * Returns true if the specified input epochTime is in range
     * (&gt;= onTime and &lt; offTime) of this SiteData "on" range.
     * 
     * @param jdate The input epoch time to be tested for in range with this
     *                  SiteData "on" range. 
     * @return True if the specified input epochTime is in range
     *         (&gt;= onTime and &lt; offTime) of this SiteData "on" range.
     */
    public boolean inRange(int jdate)
    {
      
        return jdate >= onDate && jdate <= offDate;
    }
    /**
     * Returns true if the specified input epochTime is in range
     * (&gt;= onTime and &lt; offTime) of this SiteData "on" range.
     * 
     * @param epochTime The input epoch time to be tested for in range with this
     *                  SiteData "on" range. 
     * @return True if the specified input epochTime is in range
     *         (&gt;= onTime and &lt; offTime) of this SiteData "on" range.
     */
    
    public boolean inRange(double epochTime)
    {
      return inRange(GMTFormat.getJDate(epochTime));
    }
}
