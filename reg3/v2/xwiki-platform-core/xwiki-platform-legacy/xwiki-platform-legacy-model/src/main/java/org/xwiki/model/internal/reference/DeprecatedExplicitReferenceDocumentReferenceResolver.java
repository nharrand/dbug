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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReference;

/**
 * Same as ExplicitReferenceDocumentReferenceResolver but with the extended type in the role hint instead of the role type.
 * 
 * @version $Id: fc5960212081ab628e9046b22c5a55ab3ee8a47f $
 * @since 2.2.3
 * @deprecated use {@link ExplicitReferenceDocumentReferenceResolver} instead.
 */
@Component
@Named("explicit/reference")
@Singleton
@Deprecated
public class DeprecatedExplicitReferenceDocumentReferenceResolver implements DocumentReferenceResolver
{
    @Inject
    @Named("explicit")
    private DocumentReferenceResolver<EntityReference> resolver;

    @Override
    public DocumentReference resolve(Object documentReferenceRepresentation, Object... parameters)
    {
        return resolver.resolve((EntityReference) documentReferenceRepresentation, parameters);
    }
}
