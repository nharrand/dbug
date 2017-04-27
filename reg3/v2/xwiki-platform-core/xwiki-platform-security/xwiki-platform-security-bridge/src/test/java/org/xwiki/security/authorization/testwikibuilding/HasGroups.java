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
package org.xwiki.security.authorization.testwikibuilding;

/**
 * This interface is used for building a mocked test setup for testing the authorization manager.
 *
 * Interface for entities that contain users.  I.e., a wiki.
 *
 * @since 4.2
 * @version $Id: bfaf03e38cccfbf76c77bf73ac18f0117dbeba11 $
 */
public interface HasGroups
{

    /**
     * @param name The group name to add.
     * @return a user holder representing the group.
     */
    HasUsers addGroup(String name);

}
