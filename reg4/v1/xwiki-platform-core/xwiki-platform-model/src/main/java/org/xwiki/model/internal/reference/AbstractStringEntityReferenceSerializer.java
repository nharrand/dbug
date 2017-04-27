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

import org.xwiki.model.reference.EntityReference;
import org.xwiki.model.reference.EntityReferenceSerializer;

/**
 * Generic implementation deferring single reference serialization.
 *
 * @version $Id: 027d4b5866ce62e1c0b4d6ed07c3b74f06632e01 $
 * @since 3.3M2
 */
public abstract class AbstractStringEntityReferenceSerializer implements EntityReferenceSerializer<String>
{
    @Override
    public String serialize(EntityReference reference, Object... parameters)
    {
        if (reference == null) {
            return null;
        }

        StringBuilder representation = new StringBuilder();

        for (EntityReference currentReference : reference.getReversedReferenceChain()) {
            serializeEntityReference(currentReference, representation, currentReference == reference, parameters);
        }

        return representation.toString();
    }

    /**
     * Serialize a single reference element into the representation string builder.
     *
     * @param currentReference the reference to serialize
     * @param representation the builder where to write the serialized member to (this is an output parameter)
     * @param isLastReference indicate if it's the last member of the reference
     * @param parameters optional parameters
     */
    protected abstract void serializeEntityReference(EntityReference currentReference, StringBuilder representation,
        boolean isLastReference, Object... parameters);   
}