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
package org.xwiki.query.internal;

import java.util.List;

import org.xwiki.query.QueryFilter;

/**
 * A query filter that doesn't do anything; useful to avoid checking for null (implements the Null Object Design
 * Pattern).
 *
 * @version $Id: 3daea3ebf3d6ba7577f4fca0d9cc07a5a70d1f8d $
 * @since 5.1M2
 */
public class NoOpQueryFilter implements QueryFilter
{
    @Override
    public String filterStatement(String statement, String language)
    {
        return statement;
    }

    @Override
    public List filterResults(List results)
    {
        return results;
    }
}
