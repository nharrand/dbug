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
package org.xwiki.annotation.event;

import org.xwiki.observation.event.filter.EventFilter;

/**
 * An event triggered when an annotation is added.
 * <p>
 * The event also send the following parameters:
 * </p>
 * <ul>
 * <li>source: the current {com.xpn.xwiki.doc.XWikiDocument} instance</li>
 * <li>data: the current {com.xpn.xwiki.XWikiContext} instance</li>
 * </ul>
 * 
 * @version $Id: 75d3f32e1ee6515d6f61fab5bd1337f9a73910c6 $
 * @since 2.6RC2
 */
public class AnnotationAddedEvent extends AbstractAnnotationEvent
{
    /**
     * The version identifier for this Serializable class. Increment only if the <i>serialized</i> form of the class
     * changes.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor initializing the event filter with an
     * {@link org.xwiki.observation.event.filter.AlwaysMatchingEventFilter}, meaning that this event will match any
     * other annotation add event.
     */
    public AnnotationAddedEvent()
    {
        super();
    }

    /**
     * Constructor initializing the event filter with a {@link org.xwiki.observation.event.filter.FixedNameEventFilter},
     * meaning that this event will match only annotation add events affecting the document matching the passed document
     * name.
     * 
     * @param documentName the name of the document to match
     * @param identifier the identifier of the added annotation
     */
    public AnnotationAddedEvent(String documentName, String identifier)
    {
        super(documentName, identifier);
    }

    /**
     * Constructor using a custom {@link EventFilter}.
     * 
     * @param eventFilter the filter to use for matching events
     */
    public AnnotationAddedEvent(EventFilter eventFilter)
    {
        super(eventFilter);
    }
}
