package net.sourceforge.squirrel_sql.client.session.objectstree;
/*
 * Copyright (C) 2001-2002 Colin Bell
 * colbell@users.sourceforge.net
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
import javax.swing.JComponent;

import net.sourceforge.squirrel_sql.fw.sql.IUDTInfo;

import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.session.ISession;

public class UDTNode extends BaseNode implements IUDTInfo
{

	private interface ISessionKeys
	{
		String DETAIL_PANEL_KEY = UDTNode.class.getName() + "_DETAIL_PANEL_KEY";
	}

	private final IUDTInfo _udtInfo;

	public UDTNode(
		ISession session,
		ObjectsTreeModel treeModel,
		IUDTInfo udtInfo)
		throws IllegalArgumentException
	{
		super(session, treeModel, getNodeText(udtInfo));
		_udtInfo = udtInfo;
	}

	public String getCatalogName()
	{
		return _udtInfo.getCatalogName();
	}

	public String getSchemaName()
	{
		return _udtInfo.getSchemaName();
	}

	public String getSimpleName()
	{
		return _udtInfo.getSimpleName();
	}

	public String getQualifiedName()
	{
		return _udtInfo.getQualifiedName();
	}

	public int getDatabaseObjectType()
	{
		return _udtInfo.getDatabaseObjectType();
	}

	public String getJavaClassName()
	{
		return _udtInfo.getJavaClassName();
	}

	public String getDataType()
	{
		return _udtInfo.getDataType();
	}

	public String getRemarks()
	{
		return _udtInfo.getRemarks();
	}

	public JComponent getDetailsPanel()
	{
		final ISession session = getSession();
		final IPlugin plugin = session.getApplication().getDummyAppPlugin();
		UDTPanel pnl =
			(UDTPanel) session.getPluginObject(
				plugin,
				ISessionKeys.DETAIL_PANEL_KEY);
		if (pnl == null)
		{
			pnl = new UDTPanel(session);
			session.putPluginObject(plugin, ISessionKeys.DETAIL_PANEL_KEY, pnl);
		}
		pnl.setUDTInfo(this);
		return pnl;
	}

	private static String getNodeText(IUDTInfo udtInfo)
		throws IllegalArgumentException
	{
		if (udtInfo == null)
		{
			throw new IllegalArgumentException("Null IUDTInfo passed");
		}
		return udtInfo.getSimpleName();
	}

	/*
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof UDTNode)
		{
			return ((UDTNode) obj)._udtInfo.equals(_udtInfo);
		}
		return false;
	}

	public int compareTo(Object o)
	{
		UDTNode other = (UDTNode) o;
		return _udtInfo.compareTo(other._udtInfo);
	}
}
