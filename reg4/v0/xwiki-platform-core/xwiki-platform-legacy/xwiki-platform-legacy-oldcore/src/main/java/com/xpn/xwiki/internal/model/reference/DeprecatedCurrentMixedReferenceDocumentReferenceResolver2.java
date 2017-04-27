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
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReference;

/**
 * Same as CompactWikiStringEntityReferenceSerializer but with the extended type in both the role hint and the role
 * type.
 * 
 * @version $Id: 99090147bc568d2bd85a0d00dcea271befe2c4b5 $
 * @since 2.3M1
 * @deprecated use {@link CompactWikiStringEntityReferenceSerializer} instead.
 */
@Component
@Named("currentmixed/reference")
@Singleton
@Deprecated
public class DeprecatedCurrentMixedReferenceDocumentReferenceResolver2 implements
    DocumentReferenceResolver<EntityReference>
{
    @Inject
    @Named("currentmixed")
    private DocumentReferenceResolver<EntityReference> resolver;

    @Override
    public DocumentReference resolve(EntityReference documentReferenceRepresentation, Object... parameters)
    {
        return this.resolver.resolve(documentReferenceRepresentation, parameters);
    }
}
