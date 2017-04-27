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

package org.xwiki.security.authorization.testwikis.internal.entities;

import org.xwiki.model.reference.EntityReference;
import org.xwiki.security.authorization.testwikis.TestEntity;
import org.xwiki.security.authorization.testwikis.TestGroupDocument;

/**
 * Entity representing a document that represent a group.
 *
 * @version $Id: 18134311ac7c4ac23ba12e4ecbc22ce99c70ed30 $
 * @since 5.0M2
 */
public class DefaultTestGroupDocument extends DefaultTestUserDocument implements TestGroupDocument
{
    /**
     * Create a new document entity for a group.
     * @param reference reference of document represented by this entity.
     * @param creator creator of this document.
     * @param description alternate description of this entity.
     * @param parent parent entity of this entity.
     */
    public DefaultTestGroupDocument(EntityReference reference, EntityReference creator, String description,
        TestEntity parent) {
        super(reference, creator, description, parent);
    }
}
