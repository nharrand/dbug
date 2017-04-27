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
package org.xwiki.activeinstalls.internal.client;

import org.xwiki.component.annotation.Role;

/**
 * Sends a ping to a remote instance that stores it, thus allowing us to have stats on the number of active XWiki
 * installations out there.
 *
 * @version $Id: 8c353c42aa5012c87fac1b6c9016f6bfe4d6ee6a $
 * @since 5.2M2
 */
@Role
public interface PingSender
{
    /**
     * Send the ping.
     *
     * @throws Exception in case an error happened during the send
     */
    void sendPing() throws Exception;
}
