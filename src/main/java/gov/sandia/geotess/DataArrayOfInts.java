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

import gov.sandia.gmp.util.globals.DataType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A int[] with nAttributes elements will be associated with each node of the
 * model.
 */
public class DataArrayOfInts extends Data
{

	int[] values;

	/**
	 * Constructor.
	 * 
	 * @param values
	 */
	public DataArrayOfInts(int... values)
	{
		this.values = values;
	}

	/**
	 * Constructor that reads the array of values from an ascii file.
	 * 
	 * @param input
	 * @param nAttributes
	 */
	protected DataArrayOfInts(Scanner input, int nAttributes)
	{
		values = new int[nAttributes];
		for (int i = 0; i < values.length; ++i)
			values[i] = input.nextInt();
	}

	/**
	 * Constructor that reads the array of values from a binary file.
	 * 
	 * @param input
	 * @param nAttributes
	 */
	protected DataArrayOfInts(DataInputStream input, int nAttributes)
			throws IOException
	{
		values = new int[nAttributes];
		for (int i = 0; i < values.length; ++i)
			values[i] = input.readInt();
	}

	/**
	 * Returns DataType.INT
	 * 
	 * @return DataType.INT
	 */
	@Override
	public DataType getDataType()
	{
		return DataType.INT;
	}

	/**
	 * Returns true if this and other are of the same DataType, have the same
	 * number of elements, and all values are ==
	 * 
	 * @param other
	 * @return true if this and other are of the same DataType, have the same
	 *         number of elements, and all values are ==
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof DataArrayOfInts)
				|| ((DataArrayOfInts)other).values.length != this.values.length)
			return false;
		
		for (int i = 0; i < values.length; ++i)
			if (this.values[i] != ((DataArrayOfInts)other).values[i])
				return false;
		return true;
	}

	@Override
	public double getDouble(int attributeIndex)
	{
		return values[attributeIndex];
	}

	@Override
	public float getFloat(int attributeIndex)
	{
		return values[attributeIndex];
	}

	@Override
	public long getLong(int attributeIndex)
	{
		return values[attributeIndex];
	}

	@Override
	public int getInt(int attributeIndex)
	{
		return values[attributeIndex];
	}

	@Override
	public short getShort(int attributeIndex)
	{
		return (short) values[attributeIndex];
	}

	@Override
	public byte getByte(int attributeIndex)
	{
		return (byte) values[attributeIndex];
	}

	@Override
	public Data setValue(int attributeIndex, double value)
	{
		values[attributeIndex] = (int) value;
		return this;
	}

	@Override
	public Data setValue(int attributeIndex, float value)
	{
		values[attributeIndex] = (int) value;
		return this;
	}

	@Override
	public Data setValue(int attributeIndex, long value)
	{
		values[attributeIndex] = (int) value;
		return this;
	}

	@Override
	public Data setValue(int attributeIndex, int value)
	{
		values[attributeIndex] = value;
		return this;
	}

	@Override
	public Data setValue(int attributeIndex, short value)
	{
		values[attributeIndex] = value;
		return this;
	}

	@Override
	public Data setValue(int attributeIndex, byte value)
	{
		values[attributeIndex] = value;
		return this;
	}

//	@Override
//	public void write(Writer output) throws IOException
//	{
//		for (int i = 0; i < values.length; ++i)
//			output.write(" " + Integer.toString(values[i]));
//	}

	@Override
	public void write(DataOutputStream output) throws IOException
	{
		for (int i = 0; i < values.length; ++i)
			output.writeInt(values[i]);
	}

	@Override
	public int size()
	{
		return values.length;
	}

	@Override
	public Data fill(Number defaultValue)
	{
		Arrays.fill(values, defaultValue.intValue());
		return this;
	}

	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append(Integer.toString(values[0]));
		for (int i=1; i<values.length; ++i)
			buf.append(" ").append(Integer.toString(values[i]));
		return buf.toString();
	}

	@Override
	public String toString(int attributeIndex)
	{
		return Integer.toString(values[attributeIndex]);
	}

	@Override
	public Data copy()
	{
		return new DataArrayOfInts(values.clone());
	}

}
