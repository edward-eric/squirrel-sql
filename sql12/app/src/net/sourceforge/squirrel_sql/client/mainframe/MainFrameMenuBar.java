package net.sourceforge.squirrel_sql.client.mainframe;
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
import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import net.sourceforge.squirrel_sql.fw.gui.action.IHasJDesktopPane;
import net.sourceforge.squirrel_sql.fw.util.Resources;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.action.ActionCollection;
import net.sourceforge.squirrel_sql.client.mainframe.action.AboutAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.CascadeAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.CloseAllSessionsAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ConnectToAliasAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.CopyAliasAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.CopyDriverAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.CreateAliasAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.CreateDriverAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.DeleteAliasAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.DeleteDriverAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.DisplayPluginSummaryAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.DumpApplicationAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ExitAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.GlobalPreferencesAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.InstallDefaultDriversAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.MaximizeAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ModifyAliasAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ModifyDriverAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.NewSessionPropertiesAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.TileAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.TileHorizontalAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.TileVerticalAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewAliasesAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewChangeLogAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewDriversAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewFAQAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewHelpAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewLicenceAction;
import net.sourceforge.squirrel_sql.client.mainframe.action.ViewLogsAction;
import net.sourceforge.squirrel_sql.client.plugin.PluginInfo;
import net.sourceforge.squirrel_sql.client.resources.SquirrelResources;
import net.sourceforge.squirrel_sql.client.session.action.CloseAllSQLResultTabsAction;
import net.sourceforge.squirrel_sql.client.session.action.CloseAllSQLResultWindowsAction;
import net.sourceforge.squirrel_sql.client.session.action.CloseSessionAction;
import net.sourceforge.squirrel_sql.client.session.action.CommitAction;
import net.sourceforge.squirrel_sql.client.session.action.DumpSessionAction;
import net.sourceforge.squirrel_sql.client.session.action.ExecuteSqlAction;
import net.sourceforge.squirrel_sql.client.session.action.ReconnectAction;
import net.sourceforge.squirrel_sql.client.session.action.RefreshObjectTreeAction;
import net.sourceforge.squirrel_sql.client.session.action.RollbackAction;
import net.sourceforge.squirrel_sql.client.session.action.SessionPropertiesAction;
import net.sourceforge.squirrel_sql.client.session.action.ShowNativeSQLAction;
import net.sourceforge.squirrel_sql.client.util.ApplicationFiles;
/**
 * Menu bar for <CODE>MainFrame</CODE>.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */

final class MainFrameMenuBar extends JMenuBar
{
	public interface IMenuIDs
	{
		int PLUGINS_MENU = 1;
		int SESSION_MENU = 2;
	}

	/** Logger for this class. */
	private static final ILogger s_log =
		LoggerController.createLogger(MainFrameMenuBar.class);

	private final IApplication _app;
	private final JMenu _driversMenu;
	private final JMenu _aliasesMenu;
	private final JMenu _pluginsMenu;
	private final JMenu _sessionMenu;
	private final JMenu _windowsMenu;
	private final ActionCollection _actions;

	/**
	 * Ctor.
	 */
	MainFrameMenuBar(IApplication app, JDesktopPane desktopPane, ActionCollection actions)
	{
		super();
		if (app == null)
		{
			throw new IllegalArgumentException("Null IApplication passed");
		}

		if (desktopPane == null)
		{
			throw new IllegalArgumentException("Null JDesktopPane passed");
		}

		if (actions == null)
		{
			throw new IllegalArgumentException("Null ActionCollection passed");
		}

		Resources rsrc = app.getResources();

		if (rsrc == null)
		{
			throw new IllegalStateException("No Resources object in IApplication");
		}

		_actions = actions;

		_app = app;

		add(createFileMenu(rsrc));
		//	  add(createEditMenu());
		add(_driversMenu = createDriversMenu(rsrc));
		add(_aliasesMenu = createAliasesMenu(rsrc));
		add(_pluginsMenu = createPluginsMenu(rsrc));
		add(_sessionMenu = createSessionMenu(rsrc));
		add(_windowsMenu = createWindowsMenu(rsrc, desktopPane));
		add(createHelpMenu(rsrc));
	}

	JMenu getWindowsMenu()
	{
		return _windowsMenu;
	}

	JMenu getSessionMenu()
	{
		return _sessionMenu;
	}

	void addToMenu(int menuId, JMenu menu)
	{
		if (menu == null)
		{
			throw new IllegalArgumentException("Null JMenu passed");
		}

		switch (menuId)
		{
			case IMenuIDs.PLUGINS_MENU :
			{
				_pluginsMenu.add(menu);
				break;
			}

			case IMenuIDs.SESSION_MENU :
			{
				_sessionMenu.add(menu);
				break;
			}

			default:
			{
				throw new IllegalArgumentException("Invalid menuId passed: " + menuId);
			}
		}
	}

	void addToMenu(int menuId, Action action)
	{
		if (action == null)
		{
			throw new IllegalArgumentException("Null Action passed");
		}

		switch (menuId)
		{
			case IMenuIDs.PLUGINS_MENU :
			{
				_pluginsMenu.add(action);
				break;
			}

			case IMenuIDs.SESSION_MENU :
			{
				_sessionMenu.add(action);
				break;
			}

			default :
			{
				throw new IllegalArgumentException("Invalid menuId passed: " + menuId);
			}
		}
	}

	/**
	 * Add a component to the end of the menu.
	 * 
	 * @param	menuId	Defines the menu to add the component to. @see IMenuIDs
	 * @param	comp	Component to add to menu.
	 * 
	 * @throws	IllegalArgumentException if null <TT>Component</TT> passed or
	 * 			an invalid <TT>menuId</TT> passed.
	 */
	void addToMenu(int menuId, Component comp)
	{
		if (comp == null)
		{
			throw new IllegalArgumentException("Component == null");
		}

		switch (menuId)
		{
			case IMenuIDs.PLUGINS_MENU :
			{
				_pluginsMenu.add(comp);
				break;
			}

			case IMenuIDs.SESSION_MENU :
			{
				_sessionMenu.add(comp);
				break;
			}

			default :
			{
				throw new IllegalArgumentException("Invalid menuId passed: " + menuId);
			}

		}

	}

	private JMenu createFileMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.FILE);
		addToMenu(rsrc, GlobalPreferencesAction.class, menu);
		addToMenu(rsrc, NewSessionPropertiesAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, DumpApplicationAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, ExitAction.class, menu);
		return menu;
	}

	//  private JMenu createEditMenu() {
	//	  JMenu menu = s_res.createMenu(MenuResourceKeys.EDIT);
	//	  addToMenu(GlobalPreferencesAction.class, menu);
	//	  return menu;
	//  }

	private JMenu createSessionMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.SESSION);
		addToMenu(rsrc, SessionPropertiesAction.class, menu);
		addToMenu(rsrc, DumpSessionAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, RefreshObjectTreeAction.class, menu);
		addToMenu(rsrc, ExecuteSqlAction.class, menu);
		addToMenu(rsrc, CommitAction.class, menu);
		addToMenu(rsrc, RollbackAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, ShowNativeSQLAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, ReconnectAction.class, menu);
		addToMenu(rsrc, CloseSessionAction.class, menu);
		menu.add(createSQLResultsCloseMenu(rsrc));
		menu.addSeparator();
		menu.setEnabled(false);
		return menu;
	}

	private JMenu createPluginsMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.PLUGINS);
		addToMenu(rsrc, DisplayPluginSummaryAction.class, menu);
		menu.addSeparator();
		return menu;
	}

	private JMenu createAliasesMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.ALIASES);
		addToMenu(rsrc, ConnectToAliasAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, CreateAliasAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, ModifyAliasAction.class, menu);
		addToMenu(rsrc, DeleteAliasAction.class, menu);
		addToMenu(rsrc, CopyAliasAction.class, menu);
		return menu;
	}

	private JMenu createDriversMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.DRIVERS);
		addToMenu(rsrc, CreateDriverAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, ModifyDriverAction.class, menu);
		addToMenu(rsrc, DeleteDriverAction.class, menu);
		addToMenu(rsrc, CopyDriverAction.class, menu);
		menu.addSeparator();
		addToMenu(rsrc, InstallDefaultDriversAction.class, menu);
		return menu;
	}

	private JMenu createWindowsMenu(Resources rsrc, JDesktopPane desktopPane)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.WINDOWS);
		addToMenu(rsrc, ViewAliasesAction.class, menu);
		addToMenu(rsrc, ViewDriversAction.class, menu);
		addToMenu(rsrc, ViewLogsAction.class, menu);
		menu.addSeparator();
		addDesktopPaneActionToMenu(rsrc, TileAction.class, menu, desktopPane);
		addDesktopPaneActionToMenu(rsrc, TileHorizontalAction.class, menu, desktopPane);
		addDesktopPaneActionToMenu(rsrc, TileVerticalAction.class, menu, desktopPane);
		addDesktopPaneActionToMenu(rsrc, CascadeAction.class, menu, desktopPane);
		addDesktopPaneActionToMenu(rsrc, MaximizeAction.class, menu, desktopPane);
		menu.addSeparator();
		addToMenu(rsrc, CloseAllSessionsAction.class, menu);
		menu.addSeparator();
		return menu;
	}

	private JMenu createHelpMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.HELP);
		addToMenu(rsrc, ViewHelpAction.class, menu);

		ApplicationFiles appFiles = new ApplicationFiles();
		Action action = new ViewFAQAction(_app);
		menu.add(action);
		action = new ViewChangeLogAction(_app, appFiles.getChangeLogFile());
		menu.add(action);
		action = new ViewLicenceAction(_app, appFiles.getLicenceFile());
		menu.add(action);

		// Add plugin help files, change logs and licences to menu.
		JMenu pluginChangeLog = rsrc.createMenu(
						SquirrelResources.IMenuResourceKeys.PLUGIN_CHANGE_LOG);
		JMenu pluginHelp = rsrc.createMenu(
						SquirrelResources.IMenuResourceKeys.PLUGIN_HELP);
		JMenu pluginLicence = rsrc.createMenu(
						SquirrelResources.IMenuResourceKeys.PLUGIN_LICENCE);
		PluginInfo[] pi = _app.getPluginManager().getPluginInformation();
		for (int i = 0; i < pi.length; ++i)
		{
			try
			{
				final File dir = pi[i].getPlugin().getPluginAppSettingsFolder();
				final String title = pi[i].getDescriptiveName();

				String fn = pi[i].getChangeLogFileName();
				if (fn != null)
				{
					action = new ViewChangeLogAction(_app,new File(dir, fn));
					action.putValue(Action.NAME, title);
					pluginChangeLog.add(action);
				}

				fn = pi[i].getHelpFileName();
				if (fn != null)
				{
					action = new ViewHelpAction(_app, new File(dir, fn));
					action.putValue(Action.NAME, title);
					pluginHelp.add(action);
				}

				fn = pi[i].getLicenceFileName();
				if (fn != null)
				{
					action = new ViewLicenceAction(_app, new File(dir, fn));
					action.putValue(Action.NAME, title);
					pluginLicence.add(action);
				}
			}
			catch (IOException ex)
			{
				s_log.error("Error creating menu item for plugin"
									+ pi[i].getDescriptiveName(), ex);
			}
		}
		if (pluginHelp.getMenuComponentCount() > 0)
		{
			menu.add(pluginHelp);
		}
		if (pluginChangeLog.getMenuComponentCount() > 0)
		{
			menu.add(pluginChangeLog);
		}
		if (pluginLicence.getMenuComponentCount() > 0)
		{
			menu.add(pluginLicence);
		}

		menu.addSeparator();
		addToMenu(rsrc, AboutAction.class, menu);
		return menu;
	}

	private JMenu createSQLResultsCloseMenu(Resources rsrc)
	{
		JMenu menu = rsrc.createMenu(SquirrelResources.IMenuResourceKeys.CLOSE_ALL_SQL_RESULTS);
		addToMenu(rsrc, CloseAllSQLResultTabsAction.class, menu);
		addToMenu(rsrc, CloseAllSQLResultWindowsAction.class, menu);
		return menu;
	}

	private Action addDesktopPaneActionToMenu(Resources rsrc, Class actionClass,
											JMenu menu, JDesktopPane desktopPane)
	{
		Action act = addToMenu(rsrc, actionClass, menu);
		if (act != null)
		{
			if (act instanceof IHasJDesktopPane)
			{
				((IHasJDesktopPane)act).setJDesktopPane(desktopPane);
			}
			else
			{
				s_log.error("Tryimg to add non IHasJDesktopPane ("
						+ actionClass.getName()
						+ ") in MainFrameMenuBar.addDesktopPaneActionToMenu");
			}
		}
		return act;
	}

	private Action addToMenu(Resources rsrc, Class actionClass, JMenu menu)
	{
		Action act = _actions.get(actionClass);
		if (act != null)
		{
			rsrc.addToMenu(act, menu);
		}
		else
		{
			s_log.error(
				"Could not retrieve instance of "
					+ actionClass.getName()
					+ ") in MainFrameMenuBar.addToMenu");
		}

		return act;
	}
}
