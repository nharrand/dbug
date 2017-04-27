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
package org.xwiki.search.solr.internal;

import java.util.Arrays;

import org.junit.Test;
import org.xwiki.model.reference.ClassPropertyReference;
import org.xwiki.model.reference.DocumentReference;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link SolrFieldStringEntityReferenceSerializer}.
 * 
 * @version $Id: f6fed7a8a76b794273699a2ded27b1460aa2f5e7 $
 * @since 5.3RC1
 */
public class SolrFieldStringEntityReferenceSerializerTest
{
    /**
     * The object being tested.
     */
    private SolrFieldStringEntityReferenceSerializer serializer = new SolrFieldStringEntityReferenceSerializer();

    @Test
    public void serialize()
    {
        assertNull(serializer.serialize(null));
        assertEquals("math.My App.Code.Some Class.title", serializer.serialize(new ClassPropertyReference("title",
            new DocumentReference("math", Arrays.asList("My App", "Code"), "Some Class"))));
        assertEquals("ma..th.My..App.Co....de.Some..Class.ti..tle", serializer.serialize(new ClassPropertyReference(
            "ti.tle", new DocumentReference("ma.th", Arrays.asList("My.App", "Co..de"), "Some.Class"))));
    }
}
