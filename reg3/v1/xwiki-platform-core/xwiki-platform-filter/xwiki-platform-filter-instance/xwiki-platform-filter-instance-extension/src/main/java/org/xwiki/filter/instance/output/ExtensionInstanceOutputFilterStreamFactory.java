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
package org.xwiki.filter.instance.output;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.filter.event.extension.ExtensionFilter;
import org.xwiki.filter.instance.internal.InstanceUtils;
import org.xwiki.filter.instance.internal.output.AbstractBeanOutputInstanceFilterStreamFactory;

/**
 * @version $Id: 5ffc5837cc2511095cbc0a766d4f4a3aa47a68b4 $
 * @since 6.2M1
 */
@Component
@Named(ExtensionInstanceOutputFilterStreamFactory.ROLEHINT)
@Singleton
public class ExtensionInstanceOutputFilterStreamFactory extends
    AbstractBeanOutputInstanceFilterStreamFactory<ExtensionInstanceOutputProperties, ExtensionFilter>
{
    /**
     * The id of this {@link org.xwiki.filter.instance.output.OutputInstanceFilterStreamFactory}.
     */
    public static final String ID = "extension";

    /**
     * The role hint of this {@link org.xwiki.filter.output.OutputFilterStreamFactory}.
     */
    public static final String ROLEHINT = InstanceUtils.ROLEHINT + '+' + ID;

    /**
     * The default constructor.
     */
    public ExtensionInstanceOutputFilterStreamFactory()
    {
        super(ID);

        setName("XWiki extensions instance output stream");
        setDescription("Specialized version of the XWiki instance output stream for extensions.");
    }
}
