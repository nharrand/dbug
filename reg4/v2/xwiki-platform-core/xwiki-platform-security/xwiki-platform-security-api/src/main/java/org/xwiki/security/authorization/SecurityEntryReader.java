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
package org.xwiki.security.authorization;

import org.xwiki.component.annotation.Role;
import org.xwiki.security.SecurityReference;

/**
 * A security rules reader reads rules attached to a given entity.
 *
 * This interface decouple the rules storage from the security cache loader.
 *
 * @version $Id: 61a302a528f46820efcdc9f837fbe4cae1c3ca6d $
 * @since 4.0M2
 */
@Role
public interface SecurityEntryReader
{
    /**
     * Read a collection of rules attached to a given entity.
     *
     * @param entityReference reference to the entity.
     * @return the access rules read from the given reference.
     * @throws AuthorizationException on error.
     */
    SecurityRuleEntry read(SecurityReference entityReference) throws AuthorizationException;
}
