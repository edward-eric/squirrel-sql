package net.sourceforge.squirrel_sql.client.gui;
/*
 * Copyright (C) 2002 Colin Bell
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
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import net.sourceforge.squirrel_sql.fw.gui.GUIUtils;

import net.sourceforge.squirrel_sql.client.mainframe.MainFrame;
/**
 * This factory stores instances of <TT>HtmlViewerSheet</TT> (viewer) objects
 * keyed by the URL of the file they are displaying. When a viewer is requested
 * the collection of existing viewers is checked and if one exists it is
 * returned, otherwise a new one is created and returned. When a viewer is
 * closed iy is removed from the collection.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class FileViewerFactory
{
	/** Single instance of this class. */
	private static final FileViewerFactory s_instance = new FileViewerFactory();

	/**
	 * Collection of <TT>HtmlViewer</TT> objects keyed by the URL they are
	 * displaying.
	 */
	private Map _sheets = new HashMap();

	/** Listener used to cleanup instances of viewers after they are closed. */
	private MyInternalFrameListener _lis = new MyInternalFrameListener();

	/**
	 * private default ctor as class is a singleton.
	 */
	private FileViewerFactory()
	{
		super();
	}

	/**
	 * Return the single instance of this class.
	 * 
	 * @return	The singleton instance of this class.
	 */
	public static FileViewerFactory getInstance()
	{
		return s_instance;
	}

	/**
	 * Return the viewer for the passed URL.
	 * 
	 * @param	parent	MDI parent frame to hold the new viewer.
	 * @param	url		The URL to be displayed.
	 * 
	 * @return	the viewer for the passed URL.
	 * 
	 * @throws	IllegalArgumentException
	 * 			Thrown if null MainFrame or URL passed.
	 */
	public synchronized HtmlViewerSheet getViewer(MainFrame parent, URL url)
		throws IOException
	{
		if (parent == null)
		{
			throw new IllegalArgumentException("MainFrame == null");
		}
		if (url == null)
		{
			throw new IllegalArgumentException("URL == null");
		}

		HtmlViewerSheet viewer = (HtmlViewerSheet)_sheets.get(url.toString());
		if (viewer == null)
		{
			viewer = new HtmlViewerSheet(url.toString(), url);
			viewer.addInternalFrameListener(_lis);
			viewer.setSize(600, 400);
			parent.addInternalFrame(viewer, true, null);
			GUIUtils.centerWithinDesktop(viewer);
			_sheets.put(url.toString(), viewer);
		}
		return viewer;
	}

	private synchronized void removeViewer(HtmlViewerSheet viewer)
	{
		viewer.removeInternalFrameListener(_lis);
		_sheets.remove(viewer.getURL().toString());
	}

	private final class MyInternalFrameListener extends InternalFrameAdapter
	{
		/**
		 * Viewer has been closed so allow it to be garbage collected.
		 */
		public void internalFrameClosed(InternalFrameEvent evt)
		{
			removeViewer((HtmlViewerSheet)evt.getInternalFrame());
			super.internalFrameClosed(evt);
		}
	}
}
