//- ****************************************************************************
//- 
//- Copyright 2009 Sandia Corporation. Under the terms of Contract
//- DE-AC04-94AL85000 with Sandia Corporation, the U.S. Government
//- retains certain rights in this software.
//- 
//- BSD Open Source License.
//- All rights reserved.
//- 
//- Redistribution and use in source and binary forms, with or without
//- modification, are permitted provided that the following conditions are met:
//- 
//-    * Redistributions of source code must retain the above copyright notice,
//-      this list of conditions and the following disclaimer.
//-    * Redistributions in binary form must reproduce the above copyright
//-      notice, this list of conditions and the following disclaimer in the
//-      documentation and/or other materials provided with the distribution.
//-    * Neither the name of Sandia National Laboratories nor the names of its
//-      contributors may be used to endorse or promote products derived from
//-      this software without specific prior written permission.
//- 
//- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//- AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//- IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//- ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
//- LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
//- CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
//- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
//- INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
//- CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
//- ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
//- POSSIBILITY OF SUCH DAMAGE.
//-
//- ****************************************************************************

package gov.sandia.geotess;

import gov.sandia.gmp.util.containers.arraylist.ArrayListDouble;
import gov.sandia.gmp.util.containers.arraylist.ArrayListInt;
import gov.sandia.gmp.util.globals.InterpolatorType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import static gov.sandia.gmp.util.globals.Globals.NL;

/**
 * A Profile defined by two radii and zero Data.
 */
public class ProfileEmpty extends Profile
{

	private float radiusBottom, radiusTop;

	private double[] layerNormal = null;

	/**
	 * Parameterized constructor that takes two radius values and no Data
	 * 
	 * @param radiusBottom
	 * @param radiusTop
	 * @throws GeoTessException
	 */
	public ProfileEmpty(float radiusBottom, float radiusTop)
			throws GeoTessException
	{
		if (radiusTop < radiusBottom)
			throw new GeoTessException("%nradiusTop must be > radiusBottom%n");
		this.radiusBottom = radiusBottom;
		this.radiusTop = radiusTop;
	}

	/**
	 * Constructor that loads required information from an ascii file.
	 * 
	 * @param input
	 * @throws GeoTessException
	 * @throws GeoTessException
	 */
	protected ProfileEmpty(Scanner input) throws GeoTessException
	{
		this(input.nextFloat(), input.nextFloat());
	}

	/**
	 * Constructor that loads required information from a binary file.
	 * 
	 * @param input
	 * @throws GeoTessException
	 * @throws IOException
	 */
	protected ProfileEmpty(DataInputStream input) throws GeoTessException,
			IOException
	{
		this(input.readFloat(), input.readFloat());
	}

	@Override
	protected void write(Writer output) throws IOException
	{
		output.write(String.format("%d %s %s%n", getType().ordinal(),
				Float.toString(radiusBottom), Float.toString(radiusTop)));
	}

	@Override
	protected void write(DataOutputStream output) throws IOException
	{
		output.writeByte((byte) getType().ordinal());
		output.writeFloat(radiusBottom);
		output.writeFloat(radiusTop);
	}

	@Override
	public ProfileType getType()
	{
		return ProfileType.EMPTY;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof ProfileEmpty))
			return false;
		
		return this.radiusBottom == ((ProfileEmpty)other).getRadiusBottom()
				&& this.radiusTop == ((ProfileEmpty)other).getRadiusTop();
	}

	/**
	 * Retrieve the value of the specified attributes at the specified
	 * radius index.
	 * @param attributeIndex
	 * @param radiusIndex
	 * @return the value of the specified attributes at the specified
	 * radius index.
	 */
	@Override
	public double getValue(int attributeIndex, int radiusIndex)
	{
		return Double.NaN;
	}

	@Override
	public boolean isNaN(int nodeIndex, int attributeIndex)
	{
		return true;
	}

	@Override
	public double getValue(InterpolatorType rInterpType, int attributeIndex,
			double radius, boolean allowRadiusOutOfRange)
	{
		return Double.NaN;
	}

	@Override
	public double getValueTop(int attributeIndex)
	{
		return Double.NaN;
	}

	@Override
	public double getValueBottom(int attributeIndex)
	{
		return Double.NaN;
	}

	@Override
	public Data[] getData()
	{
		return new Data[0];
	}

	@Override
	public void setData(Data... data)
	{
		// do nothing
	}

	/**
	 * Replace one of the Data objects currently associated with this Profile
	 * 
	 * @param index
	 * @param data
	 */
	@Override
	public void setData(int index, Data data)
	{
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public Data getData(int node)
	{
		return null;
	}

	@Override
	public double getRadius(int node)
	{
		return node == 0 ? radiusBottom : radiusTop;
	}

	@Override
	public void setRadius(int node, float radius) {
		if (node == 0) radiusBottom = radius;
		else if (node == 1) radiusTop = radius;
	}
	
	@Override
	public double getRadiusTop()
	{
		return radiusTop;
	}

	@Override
	public Data getDataTop()
	{
		return null;
	}

	@Override
	public double getRadiusBottom()
	{
		return radiusBottom;
	}

	@Override
	public Data getDataBottom()
	{
		return null;
	}

	@Override
	public int getNRadii()
	{
		return 2;
	}

	@Override
	public int getNData()
	{
		return 0;
	}

	@Override
	public float[] getRadii()
	{
		return new float[] { radiusBottom, radiusTop };
	}

	/**
	 * Find the index of the node in this Profile that has radius closest to the
	 * supplied radius.
	 * 
	 * @param radius in km
	 * @return the index of the node in this Profile that has radius closest to the
	 * supplied radius.
	 */
	public int findClosestRadiusIndex(double radius)
	{
		return Math.abs(radiusTop - radius) < Math.abs(radiusBottom - radius) ? 1 : 0;
	}

	/**
	 * Set the pointIndex that corresponds to the supplied nodeIndex.  
	 * <p>There is a node index for each Data object in a profile and they are indexed from 0 to 
	 * the number of Data objects managed by a Profile.  There is a pointIndex for every 
	 * Data object in the entire model, indexed from 0 to the number of Data objects in the 
	 * model. 
	 * @param nodeIndex
	 * @param pointIndex
	 */
	public void setPointIndex(int nodeIndex, int pointIndex)
	{
		// do nothing.  empty profiles have no data, hence pointIndex is always -1.
	}
	
	@Override
	public void resetPointIndices() {  }

	/**
	 * Get the pointIndex that corresponds to the supplied nodeIndex.  
	 * <p>There is a node index for each Data object in a profile and they are indexed from 0 to 
	 * the number of Data objects managed by a Profile.  There is a pointIndex for every 
	 * Data object in the entire model, indexed from 0 to the number of Data objects in the 
	 * model. 
	 * @param nodeIndex
	 * @return poitnIndex
	 */
	public int getPointIndex(int nodeIndex) { return -1; }

	@Override
	public void setInterpolationCoefficients(InterpolatorType interpType, 
			ArrayListInt nodeIndexes, ArrayListDouble coefficients, 
			double radius, boolean allowOutOfRange)
	{
		// POISON!
		nodeIndexes.add(0);
		coefficients.add(Double.NaN);
	}

	@Override
	public void getPointIndices(float radius, HashSet<Integer> points) { /* do nothing */ }

	@Override
	public void getWeights(HashMap<Integer, Double> weights, double dkm, double radius, double hcoefficient,
			InterpolatorType radialInterpType)
	{
		// do nothing.
	}

	@Override
	public void getWeights(Map<Integer, Double> weights, double dkm, double radius, double hcoefficient,
			InterpolatorType radialInterpType)
	{
		// do nothing.
	}

	/**
	 * Outputs this Profile as a formatted string.
	 */
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
    buf.append("  Type: " + getType().name() + NL);
    buf.append("    Radii:" + NL + "      Bottom: " +
               String.format("%9.4f", radiusBottom) + NL +
               "      Top:    " +
               String.format("%9.4f", radiusTop) + NL);
    buf.append("    Layer Normal: " + vectorString(layerNormal) + NL);
	  return buf.toString();
	}

	/**
	 * Returns an independent deep copy of this profile.
	 */
	@Override
	public Profile copy() throws GeoTessException
	{
		ProfileEmpty pe = new ProfileEmpty(radiusBottom, radiusTop);

		if (layerNormal != null)
			pe.layerNormal = layerNormal.clone();
		
		return pe;
	}

	@Override
  protected void setLayerNormal(double[] layrNormal)
  {
    layerNormal = layrNormal;
  }
  
	@Override
  protected double[] getLayerNormal()
  {
    return layerNormal;
  }

}
