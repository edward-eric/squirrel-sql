package net.sourceforge.squirrel_sql.plugins.codecompletion.prefs;

/* 
 * Copyright (C) 2008 Rob Manning 
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

import static org.junit.Assert.assertEquals;
import net.sourceforge.squirrel_sql.BaseSQuirreLJUnit4TestCase;

import org.junit.Test;

/**
 *   Test class for CodeCompletionPreferences
 */
public class CodeCompletionPreferencesTest extends BaseSQuirreLJUnit4TestCase {

	CodeCompletionPreferences classUnderTest = new CodeCompletionPreferences();

	@Test
	public void testGetGeneralCompletionConfig() throws Exception
	{
		classUnderTest.setGeneralCompletionConfig(10);
		assertEquals(10, classUnderTest.getGeneralCompletionConfig());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetPrefixedConfigs() throws Exception
	{
		classUnderTest.setPrefixedConfigs(null);
		assertEquals(null, classUnderTest.getPrefixedConfigs());
	}

	@Test
	public void testGetMaxLastSelectedCompletionNames() throws Exception
	{
		classUnderTest.setMaxLastSelectedCompletionNames(10);
		assertEquals(10, classUnderTest.getMaxLastSelectedCompletionNames());
	}

}
