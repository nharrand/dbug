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
package org.xwiki.gwt.user.client;

import java.util.ArrayList;

/**
 * A collection of timer listeners.
 * 
 * @version $Id: 26e6602bb24b9d3df7d6df54e275adc2217552ec $
 */
public class TimerListenerCollection extends ArrayList<TimerListener>
{
    /**
     * Field required by all {@link java.io.Serializable} classes.
     */
    private static final long serialVersionUID = 2221979461857283590L;

    /**
     * Notify all the listeners from this collection. This method should be called whenever the given timer's delay
     * period elapses.
     * 
     * @param sender The timer who has generated the event.
     */
    public void fireTimer(Timer sender)
    {
        for (TimerListener listener : this) {
            listener.onElapsed(sender);
        }
    }
}