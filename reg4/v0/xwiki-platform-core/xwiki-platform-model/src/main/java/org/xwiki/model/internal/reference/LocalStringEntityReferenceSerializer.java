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
package org.xwiki.model.internal.reference;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.EntityReference;

/**
 * Generate a string representation of an entity reference but without the wiki reference name specified (eg
 * "space.page" for a document reference). This is an implementation use for backward compatibility only and it should
 * be dropped in the future since there's no reason to not output the wiki name systematically (usually we don't want to
 * print it but only if it's the same as the current wiki).
 * 
 * @version $Id: 175a9d4d618fb5df0d1c8333383b946e348ca7ea $
 * @since 2.2M1
 */
@Component
@Named("local")
@Singleton
public class LocalStringEntityReferenceSerializer extends DefaultStringEntityReferenceSerializer
{
    /**
     * Empty constructor, to be used by the Component Manager, which will also inject the Symbol Scheme.
     */
    public LocalStringEntityReferenceSerializer()
    {
        // Empty constructor, to be used by the Component Manager, which will also inject the Symbol Scheme
    }

    /**
     * Constructor to be used when using this class as a POJO and not as a component.
     *
     * @param symbolScheme the scheme to use for serializing the passed references (i.e. defines the separators to use
     *        between the Entity types, and the characters to escape and how to escape them)
     */
    public LocalStringEntityReferenceSerializer(SymbolScheme symbolScheme)
    {
        super(symbolScheme);
    }

    @Override
    protected void serializeEntityReference(EntityReference currentReference, StringBuilder representation,
        boolean isLastReference, Object... parameters)
    {
        if (currentReference.getType() != EntityType.WIKI) {
            super.serializeEntityReference(currentReference, representation, isLastReference, parameters);
        }
    }
}
