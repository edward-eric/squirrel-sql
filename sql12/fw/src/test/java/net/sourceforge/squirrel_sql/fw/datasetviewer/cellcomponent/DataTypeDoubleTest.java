package net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent;

import org.junit.Before;

import net.sourceforge.squirrel_sql.fw.datasetviewer.ColumnDisplayDefinition;

/*
 * Copyright (C) 2006 Rob Manning
 * manningr@users.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * JUnit test for DataTypeDouble class.
 * 
 * @author manningr
 */
public class DataTypeDoubleTest extends FloatingPointBaseTest<Double> {

	/**
	 * @see net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.AbstractDataTypeComponentTest#getWhereClauseValueObject()
	 */
	@Override
	protected Object getWhereClauseValueObject()
	{
		return null;
	}

	@Before
	public void setUp() throws Exception {
		initClassUnderTest();
		super.setUp();
	}

	@Override
	protected Object getEqualsTestObject()
	{
		return new Double(0);
	}

	@Override
	protected void initClassUnderTest() {
		ColumnDisplayDefinition columnDisplayDefinition = 
			super.getMockColumnDisplayDefinition();
		mockHelper.replayAll();
		classUnderTest = new DataTypeDouble(null, columnDisplayDefinition);
		mockHelper.resetAll();
	}

	@Override
	protected Double getValueForRenderingTests() {
		return new Double(1234.1456789);
	}


}
