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
package org.xwiki.wysiwyg.server.internal.cleaner;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.w3c.dom.Document;
import org.xwiki.component.annotation.Component;
import org.xwiki.gwt.wysiwyg.client.cleaner.HTMLCleaner;
import org.xwiki.xml.html.HTMLCleanerConfiguration;
import org.xwiki.xml.html.HTMLUtils;

/**
 * Default HTML cleaner for the WYSIWYG editor's output.
 * 
 * @version $Id: b439fb2704a6e09c40d5aef0354ed5e76febeb35 $
 */
@Component
@Singleton
public class DefaultHTMLCleaner implements HTMLCleaner
{
    /**
     * The component used to clean the HTML.
     */
    @Inject
    private org.xwiki.xml.html.HTMLCleaner cleaner;

    /**
     * The list of WYSIWYG editor specific HTML cleaning filters.
     */
    @Inject
    private List<HTMLFilter> specificFilters;

    @Override
    public String clean(String dirtyHTML)
    {
        // Sort the list of specific filters based on their priority.
        Collections.sort(specificFilters, new Comparator<HTMLFilter>()
        {
            @Override
            public int compare(HTMLFilter alice, HTMLFilter bob)
            {
                return alice.getPriority() - bob.getPriority();
            }
        });

        // We have to remove or replace the HTML elements that were added by the WYSIWYG editor only for internal
        // reasons, before any cleaning filter is applied. Otherwise cleaning filters might transform these
        // WYSIWYG-specific HTML elements making their removal difficult. We cannot transform the WYSIWYG output on the
        // client side because the editor is a widget that can be used independently inside or outside an HTML form and
        // thus it doesn't know when its current value is submitted.
        HTMLCleanerConfiguration config = cleaner.getDefaultConfiguration();
        List<org.xwiki.xml.html.filter.HTMLFilter> filters = new ArrayList<org.xwiki.xml.html.filter.HTMLFilter>();
        filters.addAll(specificFilters);
        filters.addAll(config.getFilters());
        config.setFilters(filters);

        Document document = cleaner.clean(new StringReader(dirtyHTML), config);
        return HTMLUtils.toString(document);
    }
}
