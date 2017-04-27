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
package com.xpn.xwiki.internal.model.reference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.model.reference.EntityReferenceResolver;

/**
 * Same as CurrentReferenceEntityReferenceResolver but with the extended type in the role hint instead of the role type.
 * 
 * @version $Id: 0c99b56c57467ca553c3d1fbb1bf55c0dfb6727b $
 * @since 2.2M1
 * @deprecated use {@link CurrentReferenceEntityReferenceResolver} instead.
 */
@Component
@Named("current/reference")
@Singleton
@Deprecated
public class DeprecatedCurrentReferenceEntityReferenceResolver implements EntityReferenceResolver
{
    @Inject
    @Named("current")
    private EntityReferenceResolver<EntityReference> resolver;

    @Override
    public EntityReference resolve(Object entityReferenceRepresentation, EntityType type, Object... parameters)
    {
        return this.resolver.resolve((EntityReference) entityReferenceRepresentation, type, parameters);
    }
}
