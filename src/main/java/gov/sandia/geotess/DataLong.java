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
import java.util.Scanner;

/**
 * A single value of type long will be associated with each node of the model.
 */
public class DataLong extends Data
{

	long value;

	/**
	 * Constructor.
	 * 
	 * @param value
	 */
	public DataLong(long value)
	{
		this.value = value;
	}

	/**
	 * Constructor that reads a single value from an ascii file.
	 * 
	 * @param input
	 */
	protected DataLong(Scanner input)
	{
		value = input.nextLong();
	}

	/**
	 * Constructor that reads a single value from a binary file.
	 * 
	 * @param input
	 * @throws IOException
	 */
	protected DataLong(DataInputStream input) throws IOException
	{
		value = input.readLong();
	}

	/**
	 * Returns DataType.LONG
	 * 
	 * @return DataType.LONG
	 */
	@Override
	public DataType getDataType()
	{
		return DataType.LONG;
	}

	/**
	 * Retrieve the size of this Data object (always 1).
	 * 
	 * @return 1
	 */
	@Override
	public int size()
	{
		return 1;
	}

	/**
	 * Returns true if this and other are of the same DataType, both have a
	 * single element and those elements are ==
	 * 
	 * @param other
	 * @return true if this and other are of the same DataType, both have a
	 *         single element and those elements are ==
	 */
	@Override
	public boolean equals(Object other)
	{
		return other != null && other instanceof DataLong
				&& this.value == ((DataLong) other).value;
	}

	/**
	 * Retrieve value
	 * 
	 * @param attributeIndex
	 * @return value
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public double getDouble(int attributeIndex)
	{
		if (attributeIndex == 0)
			return value;
		
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Retrieve value
	 * 
	 * @param attributeIndex
	 * @return value
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public float getFloat(int attributeIndex)
	{
		if (attributeIndex == 0)
			return value;
		
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Retrieve value
	 * 
	 * @param attributeIndex
	 * @return value
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public long getLong(int attributeIndex)
	{
		if (attributeIndex == 0)
			return value;
		
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Retrieve value
	 * 
	 * @param attributeIndex
	 * @return value
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public int getInt(int attributeIndex)
	{
		if (attributeIndex == 0)
			return (int) value;
		
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Retrieve value
	 * 
	 * @param attributeIndex
	 * @return value
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public short getShort(int attributeIndex)
	{
		if (attributeIndex == 0)
			return (short) value;
		
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Retrieve value
	 * 
	 * @param attributeIndex
	 * @return value
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public byte getByte(int attributeIndex)
	{
		if (attributeIndex == 0)
			return (byte) value;
		
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Set this.value equal to supplied value
	 * 
	 * @param attributeIndex
	 * @param value the new value to be set
	 * @return a reference to this
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public Data setValue(int attributeIndex, double value)
	{
		if (attributeIndex != 0) throw new IndexOutOfBoundsException();
		this.value = (long) value;
		return this;
	}

	/**
	 * Set this.value equal to supplied value
	 * 
	 * @param attributeIndex
	 * @param value the new value to be set
	 * @return a reference to this
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public Data setValue(int attributeIndex, float value)
	{
		if (attributeIndex != 0) throw new IndexOutOfBoundsException();
		this.value = (long) value;
		return this;
	}

	/**
	 * Set this.value equal to supplied value
	 * 
	 * @param attributeIndex
	 * @param value the new value to be set
	 * @return a reference to this
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public Data setValue(int attributeIndex, long value)
	{
		if (attributeIndex != 0) throw new IndexOutOfBoundsException();
		this.value = (long) value;
		return this;
	}

	/**
	 * Set this.value equal to supplied value
	 * 
	 * @param attributeIndex
	 * @param value the new value to be set
	 * @return a reference to this
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public Data setValue(int attributeIndex, int value)
	{
		if (attributeIndex != 0) throw new IndexOutOfBoundsException();
		this.value = (long) value;
		return this;
	}

	/**
	 * Set this.value equal to supplied value
	 * 
	 * @param attributeIndex
	 * @param value the new value to be set
	 * @return a reference to this
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public Data setValue(int attributeIndex, short value)
	{
		if (attributeIndex != 0) throw new IndexOutOfBoundsException();
		this.value = (long) value;
		return this;
	}

	/**
	 * Set this.value equal to supplied value
	 * 
	 * @param attributeIndex
	 * @param value the new value to be set
	 * @return a reference to this
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public Data setValue(int attributeIndex, byte value)
	{
		if (attributeIndex != 0) throw new IndexOutOfBoundsException();
		this.value = (long) value;
		return this;
	}

//	@Override
//	public void write(Writer output) throws IOException
//	{
//		output.write(" " + Long.toString(value));
//	}

	@Override
	public void write(DataOutputStream output) throws IOException
	{
		output.writeLong(value);
	}

	@Override
	public Data fill(Number defaultValue)
	{
		value = defaultValue.longValue();
		return this;
	}

	@Override
	public String toString()
	{
		return Long.toString(value);
	}

	@Override
	public Data copy()
	{
		return new DataLong(value);
	}

}
