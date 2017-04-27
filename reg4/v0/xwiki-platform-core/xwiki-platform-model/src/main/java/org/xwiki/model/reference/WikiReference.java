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
package org.xwiki.model.reference;

import java.lang.reflect.Type;

import javax.inject.Provider;

import org.xwiki.component.util.DefaultParameterizedType;
import org.xwiki.model.EntityType;

/**
 * Represents a reference to a wiki (wiki name). This is the topmost reference and it doesn't have a parent reference.
 *
 * @version $Id: 0bedc1f735306105b5f171fc1b1f826d1f10b05c $
 * @since 2.2M1
 */
public class WikiReference extends EntityReference
{
    /**
     * The {@link Type} for a {@code Provider<WikiReference>}.
     * 
     * @since 7.2M1
     */
    public static final Type TYPE_PROVIDER = new DefaultParameterizedType(null, Provider.class, WikiReference.class);

    /**
     * Special constructor that transforms a generic entity reference into a {@link WikiReference}. It checks the
     * validity of the passed reference (ie correct type).
     *
     * @param reference the raw reference to build this wiki reference from
     * @throws IllegalArgumentException if the passed reference is not a valid wiki reference
     */
    public WikiReference(EntityReference reference)
    {
        super(reference);
    }

    /**
     * @param wikiName the name of the wiki referenced
     */
    public WikiReference(String wikiName)
    {
        super(wikiName, EntityType.WIKI);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden in order to verify the validity of the passed type.
     * </p>
     *
     * @throws IllegalArgumentException if the passed type is not a wiki type
     */
    @Override
    protected void setType(EntityType type)
    {
        if (type != EntityType.WIKI) {
            throw new IllegalArgumentException("Invalid type [" + type + "] for a wiki reference");
        }

        super.setType(EntityType.WIKI);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden in order to verify the validity of the passed parent.
     * </p>
     *
     * @throws IllegalArgumentException if the passed type is not a wiki type
     */
    @Override
    protected void setParent(EntityReference parent)
    {
        super.setParent(parent);
        if (parent != null) {
            throw new IllegalArgumentException("Unexpected parent [" + parent + "] in a wiki reference");
        }
    }
}
