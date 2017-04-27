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
package org.xwiki.gwt.wysiwyg.client.plugin.link;

import org.xwiki.gwt.user.client.ui.rta.cmd.internal.AbstractInsertElementExecutable.AbstractConfigJSONSerializer;

/**
 * Serializes a {@link LinkConfig} to JSON.
 * 
 * @version $Id: 50101b0b311e8ecfe08dec68340ae45ae081f75b $
 */
public class LinkConfigJSONSerializer extends AbstractConfigJSONSerializer<LinkConfig>
{
    /**
     * {@inheritDoc}
     * 
     * @see AbstractConfigJSONSerializer#serialize(Object)
     */
    public String serialize(LinkConfig linkConfig)
    {
        StringBuffer result = new StringBuffer();
        append(result, serialize("reference", linkConfig.getReference()));
        append(result, serialize("url", linkConfig.getUrl()));
        append(result, serialize("label", linkConfig.getLabel()));
        append(result, serialize("labelText", linkConfig.getLabelText()));
        append(result, serialize("readOnlyLabel", linkConfig.isReadOnlyLabel()));
        append(result, serialize("type", linkConfig.getType()));
        append(result, serialize("openInNewWindow", linkConfig.isOpenInNewWindow()));
        append(result, serialize("tooltip", linkConfig.getTooltip()));

        return "{" + result.toString() + "}";
    }
}
