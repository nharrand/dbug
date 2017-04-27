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
package org.xwiki.linkchecker.internal;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.internal.transformation.linkchecker.DefaultLinkCheckerTransformationConfiguration;

/**
 * Overrides the Rendering implementation in order to specify the XWiki.ExternalLinksJSON page to be excluded from link
 * checking.
 *
 * @version $Id: d18b2d064f4dec337dfd6de9b1c65fd34c870e38 $
 * @since 5.3RC1
 */
@Component
@Singleton
public class XWikiLinkCheckerTransformationConfiguration extends DefaultLinkCheckerTransformationConfiguration
{
    /**
     * Exclude XWiki.ExternalLinksJSON from being checked (since it lists all link statuses found in the wiki)...
     */
    private static final List<Pattern> DEFAULT_EXCLUDES =
        Arrays.asList(Pattern.compile(".*:XWiki\\.ExternalLinksJSON"));

    @Override
    protected List<Pattern> getDefaultExcludedReferencePatterns()
    {
        return DEFAULT_EXCLUDES;
    }
}
