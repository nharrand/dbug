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
package org.xwiki.store;

import org.junit.Test;

/**
 * Make sure RootTransactionRunnable cannot be run in a larger TR.
 *
 * @version $Id: ab10a90159920955765c055106676b1ae92f0928 $
 * @since 3.0M2
 */
public class RootTransactionRunnableTest
{
    @Test(expected = IllegalArgumentException.class)
    public void runInTest() throws Exception
    {
        new RootTransactionRunnable().runIn(new TransactionRunnable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void runInAsProviderTest() throws Exception
    {
        new RootTransactionRunnable().asProvider().runIn(new TransactionRunnable());
    }
}
