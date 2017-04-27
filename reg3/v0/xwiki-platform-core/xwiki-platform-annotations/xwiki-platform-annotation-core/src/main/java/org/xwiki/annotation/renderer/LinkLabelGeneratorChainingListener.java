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
package org.xwiki.annotation.renderer;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.InlineFilterListener;
import org.xwiki.rendering.listener.WrappingListener;
import org.xwiki.rendering.listener.chaining.AbstractChainingListener;
import org.xwiki.rendering.listener.chaining.EmptyBlockChainingListener;
import org.xwiki.rendering.listener.chaining.ListenerChain;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.listener.reference.ResourceType;
import org.xwiki.rendering.parser.ParseException;
import org.xwiki.rendering.parser.StreamParser;
import org.xwiki.rendering.renderer.reference.link.LinkLabelGenerator;

/**
 * Chaining listener to generate labels for the links without labels and send these labels as events in the chain.
 *
 * @version $Id: b2b313b4135144a64e3ad27187b6e6038057ad0f $
 * @since 2.3M1
 */
public class LinkLabelGeneratorChainingListener extends AbstractChainingListener
{
    /**
     * The generator for the link labels.
     */
    protected LinkLabelGenerator linkLabelGenerator;

    /**
     * The parser for the link labels, used to parse the generated labels and create events to the next listener in the
     * chain.
     */
    protected StreamParser linkLabelParser;

    /**
     * Creates a new link generator chaining listener.
     *
     * @param linkLabelGenerator the generator used to generate link labels
     * @param linkLabelParser the parser for the link labels, normally a plain text parser
     * @param listenerChain the chain this listener is part of
     */
    public LinkLabelGeneratorChainingListener(LinkLabelGenerator linkLabelGenerator, StreamParser linkLabelParser,
        ListenerChain listenerChain)
    {
        setListenerChain(listenerChain);
        this.linkLabelGenerator = linkLabelGenerator;
        this.linkLabelParser = linkLabelParser;
    }

    /**
     * @return the empty block chaining listener in this chain
     */
    protected EmptyBlockChainingListener getEmptyBlockState()
    {
        return (GeneratorEmptyBlockChainingListener) getListenerChain().getListener(
            GeneratorEmptyBlockChainingListener.class);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.xwiki.rendering.listener.chaining.AbstractChainingListener#endLink(
     *      org.xwiki.rendering.listener.reference.ResourceReference , boolean, java.util.Map)
     * @since 2.5RC1
     */
    @Override
    public void endLink(ResourceReference reference, boolean isFreeStandingURI, Map<String, String> parameters)
    {
        // if the link has no label, generate one for it and send the generated link as events to this listener default
        // behaviour
        if (getEmptyBlockState().isCurrentContainerBlockEmpty()) {
            // get the link label

            // FIXME: this should be generated by the link label generator, for all cases, so that the
            // link label can be changed with whichever generator, that can handle all cases
            String linkLabel = reference.getReference();
            ResourceType resourceType = reference.getType();
            if (ResourceType.DOCUMENT.equals(resourceType) || ResourceType.SPACE.equals(resourceType)) {
                linkLabel = linkLabelGenerator.generate(reference);
            }

            // create the span around the label signaling that this is a generated label
            Map<String, String> formatParams = new HashMap<String, String>();
            formatParams.put("class", "wikigeneratedlinkcontent");
            // the same as this.format. TODO: decide which one is more semantic.
            super.beginFormat(Format.NONE, formatParams);

            // parse the linkLabel with a stream parser and an inline filter
            WrappingListener inlineFilterListener = new InlineFilterListener();
            inlineFilterListener.setWrappedListener(getListenerChain().getNextListener(getClass()));

            try {
                linkLabelParser.parse(new StringReader(linkLabel), inlineFilterListener);
            } catch (ParseException e) {
                // couldn't parse it, send it raw (interesting)
                super.onRawText(linkLabel, linkLabelParser.getSyntax());
            }

            super.endFormat(Format.NONE, formatParams);
        }

        // end the link
        super.endLink(reference, isFreeStandingURI, parameters);
    }
}
