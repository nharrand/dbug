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
package org.xwiki.uiextension.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.CompositeBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.uiextension.UIExtension;

/**
 * UI Extension
 * 
 * @version $Id: dda6736b0c197f3de1ef1981c3209a83fc05e2a6 $
 * @since 4.2M3
 */
@Component
@Named("helloWorld")
@Singleton
public class HelloWorldUIExtension implements UIExtension
{
    @Override
    public String getId()
    {
        return "my extension";
    }

    @Override
    public String getExtensionPointId()
    {
        return "hello";
    }

    @Override
    public Map<String, String> getParameters()
    {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("HelloWorldKey", "HelloWorldValue");
        return parameters;
    }

    @Override
    public Block execute()
    {
        List<Block> blocks = new ArrayList<Block>();
        blocks.add(new WordBlock("HelloWorld"));
        return new CompositeBlock(blocks);
    }
}
