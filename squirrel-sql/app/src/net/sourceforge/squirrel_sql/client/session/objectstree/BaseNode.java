package net.sourceforge.squirrel_sql.client.session.objectstree;
/*
 * Copyright (C) 2001 Colin Bell
 * colbell@users.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import net.sourceforge.squirrel_sql.fw.sql.BaseSQLException;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import net.sourceforge.squirrel_sql.fw.util.BaseException;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

import net.sourceforge.squirrel_sql.client.session.ISession;

/**
 * This is the base class for all nodes in the Objects Tree.
 */
public class BaseNode extends DefaultMutableTreeNode {
	/** Logger for this class. */
	private static ILogger s_log = LoggerController.createLogger(BaseNode.class);

	/*
	 * BaseNode expand listeners array
	 */ 
	private List _expandListeners;
	
    /**
     * Empty panel. Used by those nodes that don't want to display anything
     * in the main display area if they are selected.
     */
    private static final JPanel s_emptyPnl = new JPanel();

    /** Current session. */
    private final ISession _session;

    /** Tree model. */
    private final ObjectsTreeModel _treeModel;

    public BaseNode(ISession session, ObjectsTreeModel treeModel,
                        Object userObject) {
        super(userObject);
        if (session == null) {
            throw new IllegalArgumentException("null ISession passed");
        }
        if (treeModel == null) {
            throw new IllegalArgumentException("null ObjectsTreeModel passed");
        }

        _session = session;
        _treeModel = treeModel;
        _expandListeners = new ArrayList();
    }
	public void addBaseNodeExpandListener(BaseNodeExpandedListener listener)
	{
		if(listener != null && !_expandListeners.contains(listener))
		{
			_expandListeners.add(listener);
		}
	}
	public void removeBaseNodeExpandListener(BaseNodeExpandedListener listener)
	{
		if(listener != null)
		{
			_expandListeners.remove(listener);
		}
	}
	protected void fireExpanded()
	{
		for(int i=_expandListeners.size();--i>=0;)
		{
			((BaseNodeExpandedListener)_expandListeners.get(i)).nodeExpanded(this);
		}
	}
    public void expand() throws BaseSQLException 
    {
    	fireExpanded();
    }

    public JComponent getDetailsPanel() {
        return s_emptyPnl;
    }

    protected ISession getSession() {
        return _session;
    }

    protected ObjectsTreeModel getTreeModel() {
        return _treeModel;
    }

    protected String getSafeString(String str) {
        return str != null ? str : "";
    }
    
    public DefaultMutableTreeNode addLoadingNode()
	{
        	ObjectsTreeModel model = getTreeModel();
/* i18n*/ 	DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode("Loading...");
        	model.insertNodeInto(dmtn, this, this.getChildCount());
        	return dmtn;
	}
    
	protected abstract class TreeNodesLoader implements Runnable
	{
		private MutableTreeNode _loading;

		TreeNodesLoader(MutableTreeNode loading)
		{
			if(loading == null) throw new IllegalArgumentException("loading node is null");
			_loading = loading;
		}
		
		public void run()
		{
			final ISession session = getSession();
			final ObjectsTreeModel model = getTreeModel();
			try 
           {
				final SQLConnection conn = session.getSQLConnection();
				final List nodes = getNodeList(session, conn,model);
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						// Maybe this shouldn't be adding nodes one by one but just setting
						// the childarray at onces, Then only one fire in the model has to be processed.
						model.removeNodeFromParent(_loading);
						for (int i=0;i<nodes.size();i++)
						{
							model.insertNodeInto((MutableTreeNode)nodes.get(i), BaseNode.this, getChildCount());
						}
						fireExpanded();
					}
				});

			} 
			catch(final BaseSQLException ex)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{ 
						model.removeNodeFromParent(_loading);
						fireExpanded();
						_session.getMessageHandler().showMessage(ex);
						s_log.error("Error occured expanding " + BaseNode.this.getClass().getName(), ex);
					}
				});
			}
		}
		
		public abstract List getNodeList(ISession session, SQLConnection conn,ObjectsTreeModel model) throws BaseSQLException;
	}
    
}
