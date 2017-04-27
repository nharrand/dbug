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
package org.xwiki.extension.script.internal.safe;

import java.io.IOException;
import java.io.InputStream;

import org.xwiki.extension.ExtensionFile;
import org.xwiki.script.internal.safe.AbstractSafeObject;

/**
 * Provide a public script access to an extension file.
 * 
 * @param <T> the extension type
 * @version $Id: 91248ce025c1512b08e51e4d9296ab4cfb2075a2 $
 * @since 4.0M2
 */
public class SafeExtensionFile<T extends ExtensionFile> extends AbstractSafeObject<T> implements ExtensionFile
{
    /**
     * @param file he wrapped file
     */
    public SafeExtensionFile(T file)
    {
        super(file, null);
    }

    // ExtensionFile

    @Override
    public long getLength()
    {
        return getWrapped().getLength();
    }

    @Override
    public InputStream openStream() throws IOException
    {
        return getWrapped().openStream();
    }
}
