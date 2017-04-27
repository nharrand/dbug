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
package org.xwiki.model.reference;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.xwiki.model.EntityType;
import org.xwiki.model.internal.reference.DefaultSymbolScheme;
import org.xwiki.model.internal.reference.RelativeStringEntityReferenceResolver;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Validate {@link EntityReferenceSet}.
 * 
 * @version $Id: 6ebdad43794ed6070a9657b9170eb0ea4aa85283 $
 */
@ComponentList({
    DefaultSymbolScheme.class
})
public class EntityReferenceSetTest
{
    @Rule
    public MockitoComponentMockingRule<RelativeStringEntityReferenceResolver> resolverMocker =
        new MockitoComponentMockingRule<>(RelativeStringEntityReferenceResolver.class);

    private EntityReferenceSet set = new EntityReferenceSet();

    private void assertMatches(EntityReference reference)
    {
        assertTrue(this.set.matches(reference));
    }

    private void assertNotMatches(EntityReference reference)
    {
        assertFalse(this.set.matches(reference));
    }

    private void assertMatches(String reference, EntityType type) throws Exception
    {
        assertMatches(this.resolverMocker.getComponentUnderTest().resolve(reference, type));
    }

    private void assertNotMatches(String reference, EntityType type) throws Exception
    {
        assertNotMatches(this.resolverMocker.getComponentUnderTest().resolve(reference, type));
    }

    private void assertMatchesWiki(String reference) throws Exception
    {
        assertMatches(reference, EntityType.WIKI);
    }

    private void assertNotMatchesWiki(String reference) throws Exception
    {
        assertNotMatches(reference, EntityType.WIKI);
    }

    private void assertMatchesSpace(String reference) throws Exception
    {
        assertMatches(reference, EntityType.SPACE);
    }

    private void assertNotMatchesSpace(String reference) throws Exception
    {
        assertNotMatches(reference, EntityType.SPACE);
    }

    private void assertMatchesDocument(String reference) throws Exception
    {
        assertMatches(reference, EntityType.DOCUMENT);
    }

    private void assertNotMatchesDocument(String reference) throws Exception
    {
        assertNotMatches(reference, EntityType.DOCUMENT);
    }

    // Includes

    private void includes(EntityReference reference)
    {
        this.set.includes(reference);
    }

    private void includes(String reference, EntityType type) throws Exception
    {
        includes(this.resolverMocker.getComponentUnderTest().resolve(reference, type));
    }

    private void includesWiki(String reference) throws Exception
    {
        includes(reference, EntityType.WIKI);
    }

    private void includesSpace(String reference) throws Exception
    {
        includes(reference, EntityType.SPACE);
    }

    private void includesDocument(String reference) throws Exception
    {
        includes(reference, EntityType.DOCUMENT);
    }

    // Excludes

    private void excludes(EntityReference reference)
    {
        this.set.excludes(reference);
    }

    private void excludes(String reference, EntityType type) throws Exception
    {
        excludes(this.resolverMocker.getComponentUnderTest().resolve(reference, type));
    }

    private void excludesWiki(String reference) throws Exception
    {
        excludes(reference, EntityType.WIKI);
    }

    private void excludesSpace(String reference) throws Exception
    {
        excludes(reference, EntityType.SPACE);
    }

    private void excludesDocument(String reference) throws Exception
    {
        excludes(reference, EntityType.DOCUMENT);
    }

    // Tests

    @Test
    public void includeWiki() throws Exception
    {
        includesWiki("wiki");

        assertMatchesWiki("wiki");
        assertNotMatchesWiki("notwiki");

        assertMatchesSpace("wiki:space");
        assertNotMatchesSpace("notwiki:space");

        includesWiki("otherwiki");

        assertMatchesWiki("wiki");

        assertMatchesWiki("otherwiki");

        assertNotMatchesWiki("notwiki");
    }

    @Test
    public void includeSpace() throws Exception
    {
        includesSpace("wiki:space");

        assertMatchesSpace("wiki:space");

        assertNotMatchesSpace("notwiki:space");
        assertNotMatchesSpace("wiki:notspace");

        includesSpace("wiki:otherspace");

        assertMatchesSpace("wiki:space");

        assertMatchesSpace("wiki:otherspace");

        assertNotMatchesSpace("wiki:notspace");
    }

    @Test
    public void includeNestedSpace() throws Exception
    {
        includesSpace("wiki:space.nested");

        assertMatchesSpace("wiki:space");

        assertMatchesSpace("wiki:space.nested");
        assertMatchesDocument("wiki:space.nested.document");

        assertNotMatchesSpace("notwiki:space");
        assertNotMatchesSpace("wiki:notspace");
        assertNotMatchesSpace("wiki:space.notnested");
        assertNotMatchesDocument("wiki:space.document");

        includesSpace("wiki:otherspace");

        assertMatchesSpace("wiki:space");

        assertMatchesSpace("wiki:otherspace");

        assertNotMatchesSpace("wiki:notspace");
    }

    @Test
    public void includePartialOnlySpace() throws Exception
    {
        includesSpace("space");

        assertMatchesSpace("wiki:space");
        assertMatchesSpace("space");

        assertNotMatchesSpace("wiki:notspace");
        assertNotMatchesSpace("notspace");
    }

    @Test
    public void includeDocument() throws Exception
    {
        includesDocument("wiki:space.document");

        assertMatchesDocument("wiki:space.document");

        assertNotMatchesDocument("notwiki:space.document");
        assertNotMatchesDocument("wiki:notspace.document");
        assertNotMatchesDocument("wiki:space.notdocument");
    }

    @Test
    public void includeLocalDocumentLocale()
    {
        includes(new LocalDocumentReference("space", "document", Locale.ROOT));

        assertMatches(new LocalDocumentReference("space", "document"));
        assertMatches(new LocalDocumentReference("space", "document", Locale.ROOT));

        assertNotMatches(new LocalDocumentReference("space", "document", Locale.FRENCH));

        includes(new LocalDocumentReference("space", "document", Locale.ENGLISH));

        assertMatches(new LocalDocumentReference("space", "document"));
        assertMatches(new LocalDocumentReference("space", "document", Locale.ROOT));
        assertMatches(new LocalDocumentReference("space", "document", Locale.ENGLISH));

        assertNotMatches(new LocalDocumentReference("space", "document", Locale.FRENCH));
    }

    @Test
    public void includeDocumentInNestedSpace() throws Exception
    {
        includesDocument("wiki:space.nestedspace.document");

        assertMatchesDocument("wiki:space.nestedspace.document");

        assertNotMatchesDocument("wiki:space.othernestedspace.document");
        assertNotMatchesDocument("wiki:space.document");
        assertNotMatchesDocument("notwiki:space.nestedspace.document");
        assertNotMatchesDocument("wiki:notspace.nestedspace.document");
        assertNotMatchesDocument("wiki:space.nestedspace.notdocument");
        assertNotMatchesDocument("wiki:space.nestedspace.othernestedspace.document");
    }

    @Test
    public void includeDocumentsInNestedSpacesWithShortAfterLong() throws Exception
    {
        includesDocument("wiki:space.nestedspace.document");
        includesDocument("wiki:space.document");

        assertMatchesDocument("wiki:space.document");
        assertMatchesDocument("wiki:space.nestedspace.document");

        assertNotMatchesDocument("wiki:space.othernestedspace.document");
        assertNotMatchesDocument("notwiki:space.nestedspace.document");
        assertNotMatchesDocument("wiki:notspace.nestedspace.document");
        assertNotMatchesDocument("wiki:space.nestedspace.notdocument");
        assertNotMatchesDocument("wiki:space.nestedspace.othernestedspace.document");
    }

    @Test
    public void includeDocumentsInNestedSpacesWithLongAfterShort() throws Exception
    {
        includesDocument("wiki:space.document");
        includesDocument("wiki:space.nestedspace.document");

        assertMatchesDocument("wiki:space.document");
        assertMatchesDocument("wiki:space.nestedspace.document");

        assertNotMatchesDocument("wiki:space.othernestedspace.document");
        assertNotMatchesDocument("notwiki:space.nestedspace.document");
        assertNotMatchesDocument("wiki:notspace.nestedspace.document");
        assertNotMatchesDocument("wiki:space.nestedspace.notdocument");
        assertNotMatchesDocument("wiki:space.nestedspace.othernestedspace.document");
    }

    @Test
    public void excludeWiki() throws Exception
    {
        excludesWiki("wiki");

        assertNotMatchesWiki("wiki");

        assertMatchesWiki("otherwiki");

        assertMatchesWiki("notwiki");

        set.excludes(new EntityReference("otherwiki", EntityType.WIKI));

        assertNotMatchesWiki("wiki");

        assertNotMatchesWiki("otherwiki");

        assertMatchesWiki("notwiki");
    }

    @Test
    public void excludeSpace() throws Exception
    {
        excludesSpace("wiki:space");

        assertNotMatchesSpace("wiki:space");

        assertMatchesWiki("wiki");
        assertMatchesSpace("otherwiki:space");
        assertMatchesSpace("wiki:otherspace");
    }

    @Test
    public void excludeNestedSpace() throws Exception
    {
        excludesSpace("wiki:space.nested");

        assertNotMatchesSpace("wiki:space.nested");
        assertNotMatchesDocument("wiki:space.nested.page");

        assertMatchesDocument("wiki:space.page");
        assertMatchesSpace("wiki:space");
        assertMatchesWiki("wiki");
        assertMatchesSpace("otherwiki:space");
        assertMatchesSpace("wiki:otherspace");
    }

    @Test
    public void excludePartial() throws Exception
    {
        excludesSpace("space");

        assertNotMatches(new EntityReference("space", EntityType.SPACE, new EntityReference("wiki", EntityType.WIKI)));
        assertNotMatches(new EntityReference("space", EntityType.SPACE));

        assertMatches(new EntityReference("notspace", EntityType.SPACE, new EntityReference("wiki", EntityType.WIKI)));
        assertMatches(new EntityReference("notspace", EntityType.SPACE));
    }

    @Test
    public void includeLocale()
    {
        includes(new DocumentReference("wiki", "space", "document", Locale.ENGLISH));

        assertMatches(new DocumentReference("wiki", "space", "document"));
        assertMatches(new DocumentReference("wiki", "space", "document", Locale.ENGLISH));

        assertNotMatches(new DocumentReference("wiki", "space", "document", Locale.FRENCH));
        assertNotMatches(new DocumentReference("wiki", "space", "document", Locale.ROOT));

        includes(new DocumentReference("wiki", "space", "document", Locale.FRENCH));

        assertMatches(new DocumentReference("wiki", "space", "document"));
        assertMatches(new DocumentReference("wiki", "space", "document", Locale.ENGLISH));
        assertMatches(new DocumentReference("wiki", "space", "document", Locale.FRENCH));

        assertNotMatches(new DocumentReference("wiki", "space", "document", Locale.ROOT));
    }

    @Test
    public void excludeLocale()
    {
        excludes(new DocumentReference("wiki", "space", "document", Locale.ENGLISH));

        assertMatches(new DocumentReference("wiki", "space", "document"));

        assertNotMatches(new DocumentReference("wiki", "space", "document", Locale.ENGLISH));

        assertMatches(new DocumentReference("wiki", "space", "document", Locale.FRENCH));
        assertMatches(new DocumentReference("wiki", "space", "document", Locale.ROOT));

        excludes(new DocumentReference("wiki", "space", "document", Locale.FRENCH));

        assertMatches(new DocumentReference("wiki", "space", "document"));

        assertNotMatches(new DocumentReference("wiki", "space", "document", Locale.ENGLISH));
        assertNotMatches(new DocumentReference("wiki", "space", "document", Locale.FRENCH));

        assertMatches(new DocumentReference("wiki", "space", "document", Locale.ROOT));
    }
}
