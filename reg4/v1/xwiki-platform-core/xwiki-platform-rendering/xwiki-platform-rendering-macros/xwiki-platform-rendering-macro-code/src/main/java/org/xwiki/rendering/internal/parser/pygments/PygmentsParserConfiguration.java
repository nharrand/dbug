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
package org.xwiki.rendering.internal.parser.pygments;

import org.xwiki.component.annotation.Role;

/**
 * Configuration properties for the Pygments based highlight parser.
 * <p>
 * You can override the default values for each of the configuration properties below by defining them in XWiki's global
 * configuration file using a prefix of "macro.code.pygments" followed by the property name. For example:
 * <code>macro.code.pygments.style = colorful</code>
 * 
 * @version $Id: 190d5673f10bb3cfcfacf239d630521d974b8a04 $
 * @since 2.0M1
 */
@Role
public interface PygmentsParserConfiguration
{
    /**
     * @return the id of the Pygments style to use.
     */
    String getStyle();
}