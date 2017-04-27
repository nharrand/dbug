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
package org.xwiki.webjars.internal;

/**
 * Used for unit tests to make {@link WebJarsResourceReferenceHandler} more easily testable.
 *
 * @version $Id: 17dc7015e53437c57375ed75578b687f24af1f74 $
 * @since 6.1M2
 */
public class TestableWebJarsResourceReferenceHandler extends WebJarsResourceReferenceHandler
{
    private ClassLoader classLoader;

    @Override
    protected ClassLoader getClassLoader()
    {
        return this.classLoader;
    }

    public void setClassLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }
}
