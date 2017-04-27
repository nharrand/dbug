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
package com.xpn.xwiki.internal.plugin.rightsmanager;

import org.xwiki.model.reference.DocumentReference;

import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;

/**
 * Simply return the {@link DocumentReference} for the users.
 *
 * @version $Id: d0d662621d4e3b9dc536d4535f7ff5e4fda6476b $
 * @since 6.4.2
 * @since 7.0M2
 */
public class ReferenceUserDataExtractor implements UserDataExtractor<DocumentReference>
{
    @Override
    public DocumentReference extractFromSuperadmin(DocumentReference reference)
    {
        return reference;
    }

    @Override
    public DocumentReference extractFromGuest(DocumentReference reference)
    {
        return reference;
    }

    @Override
    public DocumentReference extract(DocumentReference reference, XWikiDocument document, BaseObject userObject)
    {
        return reference;
    }
}
