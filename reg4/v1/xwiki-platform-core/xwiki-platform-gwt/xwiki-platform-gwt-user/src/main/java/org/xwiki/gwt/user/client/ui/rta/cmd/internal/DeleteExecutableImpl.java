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
package org.xwiki.gwt.user.client.ui.rta.cmd.internal;

import org.xwiki.gwt.dom.client.Selection;

/**
 * Base class for browser specific implementations of {@link DeleteExecutable}.
 * 
 * @version $Id: 5b28ac51d9dc4b96c0a9ec8dc6f63587fb98ba6c $
 */
public class DeleteExecutableImpl
{
    /**
     * Deletes the specified selection.
     * 
     * @param selection the selection to be deleted
     * @return {@code true} if the delete succeeded, {@code false} otherwise
     */
    public boolean deleteSelection(Selection selection)
    {
        return false;
    }
}
