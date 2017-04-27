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
package org.xwiki.test.page;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.xwiki.rendering.internal.parser.reference.DefaultImageReferenceParser;
import org.xwiki.rendering.internal.parser.reference.DefaultLinkReferenceParser;
import org.xwiki.rendering.internal.parser.reference.DefaultResourceReferenceParser;
import org.xwiki.rendering.internal.parser.reference.DefaultUntypedImageReferenceParser;
import org.xwiki.rendering.internal.parser.xwiki21.XWiki21Parser;
import org.xwiki.rendering.internal.renderer.xwiki21.XWikiSyntaxBlockRenderer;
import org.xwiki.rendering.internal.renderer.xwiki21.XWikiSyntaxRenderer;
import org.xwiki.rendering.internal.renderer.xwiki21.XWikiSyntaxRendererFactory;
import org.xwiki.rendering.internal.renderer.xwiki21.reference.XWikiSyntaxImageReferenceSerializer;
import org.xwiki.rendering.internal.renderer.xwiki21.reference.XWikiSyntaxLinkReferenceSerializer;
import org.xwiki.test.annotation.ComponentList;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Pack of default Component implementations that are needed for Parsing and Rendering in XWiki Syntax 2.1.
 *
 * @version $Id: 951c47dfb19b1ca476fefcbe99744d1f69d77605 $
 * @since 8.3M2
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD, ANNOTATION_TYPE })
@XWikiSyntax20ComponentList
@ComponentList({
    XWiki21Parser.class,
    DefaultLinkReferenceParser.class,
    DefaultImageReferenceParser.class,
    DefaultResourceReferenceParser.class,
    DefaultUntypedImageReferenceParser.class,
    XWikiSyntaxBlockRenderer.class,
    XWikiSyntaxRendererFactory.class,
    XWikiSyntaxRenderer.class,
    XWikiSyntaxLinkReferenceSerializer.class,
    XWikiSyntaxImageReferenceSerializer.class,
})
@Inherited
public @interface XWikiSyntax21ComponentList
{
}
