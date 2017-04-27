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
package org.xwiki.index.tree.internal.nestedspaces.parentchild;

import javax.inject.Inject;
import javax.inject.Named;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.InstantiationStrategy;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.index.tree.internal.nestedspaces.NestedSpacesTree;
import org.xwiki.tree.Tree;
import org.xwiki.tree.TreeNode;

/**
 * The hierarchy of XWiki pages based on the (now deprecated) parent field, grouped under nested spaces.
 * 
 * @version $Id: 5a3c5b9eec932fd5e201f517cf43c601df146bb2 $
 * @since 8.3M2, 7.4.5
 */
@Component(roles = {Tree.class})
@Named("parentChildOnNestedSpaces")
@InstantiationStrategy(ComponentInstantiationStrategy.PER_LOOKUP)
public class ParentChildOnNestedSpacesTree extends NestedSpacesTree
{
    @Inject
    @Named("space/parentChildOnNestedSpaces")
    private TreeNode spaceNode;

    @Inject
    @Named("document/parentChildOnNestedSpaces")
    private TreeNode documentNode;

    @Override
    public void initialize() throws InitializationException
    {
        super.initialize();

        this.treeNodeByNodeType.put("space", this.spaceNode);
        this.treeNodeByNodeType.put("document", this.documentNode);
    }
}
