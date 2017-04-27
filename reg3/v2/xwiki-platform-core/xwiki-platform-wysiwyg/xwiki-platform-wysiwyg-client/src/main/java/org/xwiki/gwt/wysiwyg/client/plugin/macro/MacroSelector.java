/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.gwt.wysiwyg.client.plugin.macro;

import java.util.ArrayList;
import java.util.List;

import org.xwiki.gwt.dom.client.DOMUtils;
import org.xwiki.gwt.dom.client.Element;
import org.xwiki.gwt.dom.client.Range;
import org.xwiki.gwt.dom.client.Selection;
import org.xwiki.gwt.user.client.DeferredUpdater;
import org.xwiki.gwt.user.client.HandlerRegistrationCollection;
import org.xwiki.gwt.user.client.Updatable;
import org.xwiki.gwt.user.client.ui.rta.cmd.Command;
import org.xwiki.gwt.user.client.ui.rta.cmd.CommandListener;
import org.xwiki.gwt.user.client.ui.rta.cmd.CommandManager;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

/**
 * Controls the currently selected macros.
 * 
 * @version $Id: 7dd408abfd8d163d84ab0b4225d1a8546748a1e9 $
 */
public class MacroSelector implements Updatable, MouseUpHandler, KeyUpHandler, CommandListener
{
    /**
     * The command used to notify all the rich text area listeners when its content has been reset.
     */
    private static final Command RESET = new Command("reset");

    /**
     * The displayer used to select macros.
     */
    private final MacroDisplayer displayer;

    /**
     * Schedules updates and executes only the most recent one.
     */
    private final DeferredUpdater updater = new DeferredUpdater(this);

    /**
     * The list of currently selected macro containers.
     */
    private final List<Element> selectedMacros = new ArrayList<Element>();

    /**
     * The list of handler registrations that have to be removed when this object is destroyed.
     */
    private final HandlerRegistrationCollection registrations = new HandlerRegistrationCollection();

    /**
     * Creates a new macro selector.
     * 
     * @param displayer the displayer to be used for selecting the macros
     */
    public MacroSelector(MacroDisplayer displayer)
    {
        this.displayer = displayer;

        // Listen to events generated by the rich text area in order to keep track of the select macros.
        registrations.add(displayer.getTextArea().addMouseUpHandler(this));
        registrations.add(displayer.getTextArea().addKeyUpHandler(this));
        displayer.getTextArea().getCommandManager().addCommandListener(this);
    }

    /**
     * Destroys this selector.
     */
    public void destroy()
    {
        selectedMacros.clear();
        registrations.removeHandlers();
        displayer.getTextArea().getCommandManager().removeCommandListener(this);
    }

    @Override
    public void onMouseUp(MouseUpEvent event)
    {
        if (event.getSource() == displayer.getTextArea()) {
            // See if the target is inside a macro.
            Element macro = getMacroContaining((Node) event.getNativeEvent().getEventTarget().cast());
            if (macro != null) {
                // Update the list of selected macros immediately.
                update();
            } else {
                // Otherwise just schedule an update for the list of selected macros.
                updater.deferUpdate();
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event)
    {
        if (event.getSource() == displayer.getTextArea()) {
            updater.deferUpdate();
        }
    }

    @Override
    public boolean onBeforeCommand(CommandManager sender, Command command, String param)
    {
        if (RESET.equals(command)) {
            // Clear the list of selected macro containers each time the content is reset to release the referenced DOM
            // nodes. Accessing these nodes after the rich text area has been reloaded can lead to "Access Denied"
            // JavaScript exceptions in IE.
            selectedMacros.clear();
        }
        return false;
    }

    @Override
    public void onCommand(CommandManager sender, Command command, String param)
    {
        if (sender == displayer.getTextArea().getCommandManager()) {
            updater.deferUpdate();
        }
    }

    @Override
    public void update()
    {
        // Clear previously selected macros.
        for (Element container : selectedMacros) {
            displayer.setSelected(container, false);
        }
        selectedMacros.clear();

        // Mark currently selected macros.
        Selection selection = displayer.getTextArea().getDocument().getSelection();
        for (int i = 0; i < selection.getRangeCount(); i++) {
            // We need to shrink the range because sometimes when you double click on a macro the browser selects the
            // macro container and although the selection end points are not inside the macro container the selection
            // includes only the macro.
            Range shrunkenRange = DOMUtils.getInstance().shrinkRange(selection.getRangeAt(i));
            Element macro = getMacroContaining(shrunkenRange.getCommonAncestorContainer());
            if (macro != null) {
                selectedMacros.add(macro);
                displayer.setSelected(macro, true);
            }
        }
    }

    @Override
    public boolean canUpdate()
    {
        return displayer.getTextArea().isAttached() && displayer.getTextArea().isEnabled();
    }

    /**
     * @return the number of macros currently selected
     */
    public int getMacroCount()
    {
        return selectedMacros.size();
    }

    /**
     * @param index the index of the selected macro to return
     * @return the selected macro at the specified index
     */
    public Element getMacro(int index)
    {
        return selectedMacros.get(index);
    }

    /**
     * @return the displayer used to select and detect macros
     */
    public MacroDisplayer getDisplayer()
    {
        return displayer;
    }

    /**
     * @param node a DOM node
     * @return the macro that contains the given node, {@code null} if the given node is not inside a macro
     */
    private Element getMacroContaining(Node node)
    {
        Node ancestor = node;
        while (ancestor != null) {
            if (displayer.isMacroContainer(ancestor)) {
                return Element.as(ancestor);
            }
            ancestor = ancestor.getParentNode();
        }
        return null;
    }
}
