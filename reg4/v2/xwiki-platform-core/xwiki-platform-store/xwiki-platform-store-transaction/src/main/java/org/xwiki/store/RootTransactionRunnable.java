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

/**
 * A TransactionRunnable which cannot be run inside of another runnable.
 * A TransactionRunnable must extend this if it cannot safely rollback after committing.
 *
 * @param <T> see: {@link TransactionRunnable}
 * @version $Id: 4fea91c881c7d8f506c924e25864310d3a424354 $
 * @since 3.0M2
 */
public class RootTransactionRunnable<T> extends StartableTransactionRunnable<T>
{
    /**
     * {@inheritDoc}
     * <p>
     * This implementation throws an exception because it may not be used in a rootTR.
     * </p>
     *
     * @see TransactionRunnable#runIn(TransactionRunnable)
     */
    @Override
    public <U extends T> TransactionRunnable<U> runIn(final TransactionRunnable<U> parentRunnable)
    {
        throw new IllegalArgumentException("A RootTransactionRunnable cannot safely be runIn() any other "
            + "TransactionRunnable.");
    }
}
