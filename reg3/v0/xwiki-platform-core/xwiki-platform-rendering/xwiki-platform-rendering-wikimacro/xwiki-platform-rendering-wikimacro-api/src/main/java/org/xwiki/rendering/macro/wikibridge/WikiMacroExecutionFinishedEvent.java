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
package org.xwiki.rendering.macro.wikibridge;

/**
 * Sent when a wiki macro finished execution.
 * <p>
 * The event also send the following parameters:
 * <ul>
 * <li>source: the related {@link org.xwiki.rendering.macro.wikibridge.WikiMacro} instance</li>
 * <li>data: null</li>
 * </ul>
 * 
 * @version $Id: 5302988db34bbe28f25e6644baae1d12c68a1567 $
 * @since 2.6M1
 */
public class WikiMacroExecutionFinishedEvent extends AbstractWikiMacroExecutionEvent
{

}
