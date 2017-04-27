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
package org.xwiki.model.internal.reference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.component.util.ReflectionUtils;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.ClassPropertyReference;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.model.reference.EntityReferenceProvider;
import org.xwiki.model.reference.EntityReferenceResolver;
import org.xwiki.model.reference.ObjectPropertyReference;
import org.xwiki.model.reference.SpaceReference;
import org.xwiki.model.reference.WikiReference;
import org.xwiki.model.reference.test.TestConstants;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DefaultStringEntityReferenceResolver}.
 * 
 * @version $Id: f5b0722ce21261c03541b8aeb4d1e0b3ba6c421a $
 * @since 2.2M1
 */
@ComponentList({
    DefaultSymbolScheme.class
})
public class DefaultStringEntityReferenceResolverTest implements TestConstants
{
    @Rule
    public MockitoComponentMockingRule<DefaultStringEntityReferenceResolver> mocker =
        new MockitoComponentMockingRule<>(DefaultStringEntityReferenceResolver.class);

    private EntityReferenceResolver<String> resolver;

    @Before
    public void setUp() throws Exception
    {
        this.resolver = this.mocker.getComponentUnderTest();

        EntityReferenceProvider referenceProvider = mock(EntityReferenceProvider.class);
        ReflectionUtils.setFieldValue(this.resolver, "provider", referenceProvider);

        when(referenceProvider.getDefaultReference(EntityType.WIKI)).thenReturn(DEFAULT_WIKI_REFERENCE);
        when(referenceProvider.getDefaultReference(EntityType.SPACE)).thenReturn(DEFAULT_SPACE_REFERENCE);
        when(referenceProvider.getDefaultReference(EntityType.DOCUMENT)).thenReturn(DEFAULT_PAGE_REFERENCE);
        when(referenceProvider.getDefaultReference(EntityType.ATTACHMENT)).thenReturn(DEFAULT_ATTACHMENT_REFERENCE);
        when(referenceProvider.getDefaultReference(EntityType.OBJECT)).thenReturn(DEFAULT_OBJECT_REFERENCE);
        when(referenceProvider.getDefaultReference(EntityType.OBJECT_PROPERTY)).thenReturn(
            DEFAULT_OBJECT_PROPERTY_REFERENCE);
        when(referenceProvider.getDefaultReference(EntityType.CLASS_PROPERTY)).thenReturn(
            DEFAULT_CLASS_PROPERTY_REFERENCE);
    }

    @Test
    public void testResolveDocumentReference() throws Exception
    {
        EntityReference reference = resolver.resolve("wiki:space.page", EntityType.DOCUMENT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.getName());

        reference = resolver.resolve("wiki:space.", EntityType.DOCUMENT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.getName());

        reference = resolver.resolve("space.", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.getName());

        reference = resolver.resolve("page", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.getName());

        reference = resolver.resolve(".", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.getName());

        reference = resolver.resolve(null, EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.getName());

        reference = resolver.resolve("", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.getName());

        reference = resolver.resolve("wiki1.wiki2:wiki3:some.space.page", EntityType.DOCUMENT);
        assertEquals("wiki1.wiki2:wiki3", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("some", reference.extractReference(EntityType.SPACE).getParent().getName());
        assertEquals("page", reference.getName());

        reference = resolver.resolve("some.space.page", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("some", reference.extractReference(EntityType.SPACE).getParent().getName());
        assertEquals("page", reference.getName());

        reference = resolver.resolve("wiki:page", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals("wiki:page", reference.getName());

        // Test escapes

        reference = resolver.resolve("\\\\\\.:@\\.", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals("\\.:@.", reference.getName());

        reference = resolver.resolve("some\\.space.page", EntityType.DOCUMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals("some.space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.getName());

        // Escaping characters are escaped
        reference = resolver.resolve("\\\\:\\\\.\\\\", EntityType.DOCUMENT);
        assertEquals("\\", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("\\", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("\\", reference.getName());

        reference = resolver.resolve("\\wiki:\\space.\\page", EntityType.DOCUMENT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.getName());
    }

    @Test
    public void testResolveAttachmentReference() throws Exception
    {
        EntityReference reference = resolver.resolve("wiki:space.page@filename.ext", EntityType.ATTACHMENT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("filename.ext", reference.getName());

        reference = resolver.resolve("", EntityType.ATTACHMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_ATTACHMENT, reference.getName());

        reference = resolver.resolve("wiki:space.page@my.png", EntityType.ATTACHMENT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("my.png", reference.getName());

        reference = resolver.resolve("some:file.name", EntityType.ATTACHMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("some:file.name", reference.getName());

        // Test escapes

        reference = resolver.resolve(":.\\@", EntityType.ATTACHMENT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(":.@", reference.getName());
    }

    /**
     * Tests resolving object references.
     */
    @Test
    public void testResolveObjectReference()
    {
        EntityReference reference = resolver.resolve("wiki:space.page^Object", EntityType.OBJECT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.getName());

        // default values
        reference = resolver.resolve("", EntityType.OBJECT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_OBJECT, reference.getName());

        // without some of the parents
        reference = resolver.resolve("space.page^Object", EntityType.OBJECT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.getName());

        reference = resolver.resolve("page^Object", EntityType.OBJECT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.getName());

        reference = resolver.resolve("XWiki.Class", EntityType.OBJECT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("XWiki.Class", reference.getName());

        // property without object
        reference = resolver.resolve("wiki:space.page.property", EntityType.OBJECT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("wiki:space.page.property", reference.getName());

        // object with no name
        reference = resolver.resolve("wiki:space.page^", EntityType.OBJECT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_OBJECT, reference.getName());

        // test separator escape
        reference = resolver.resolve("wiki:space.page^obje\\^ct", EntityType.OBJECT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("obje^ct", reference.getName());

        // and that separators don't need to be escaped other than in the object name
        reference = resolver.resolve("wiki:spa^ce.page^Object", EntityType.OBJECT);
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("spa^ce", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.getName());

        reference = resolver.resolve(":.\\^@", EntityType.OBJECT);
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(":.^@", reference.getName());
    }

    /**
     * Tests resolving object references.
     */
    @Test
    public void testResolveObjectPropertyReference()
    {
        EntityReference reference =
            new ObjectPropertyReference(resolver.resolve("wiki:space.page^object.prop", EntityType.OBJECT_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("object", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("prop", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        // default values
        reference = new ObjectPropertyReference(resolver.resolve("", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_OBJECT, reference.extractReference(EntityType.OBJECT).getName());
        assertEquals(DEFAULT_OBJECT_PROPERTY, reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        // without some of the parents
        reference = new ObjectPropertyReference(resolver.resolve("space.page^Object.prop", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("prop", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        reference = new ObjectPropertyReference(resolver.resolve("page^Object.prop", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("prop", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        reference = new ObjectPropertyReference(resolver.resolve("Object.prop", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("prop", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        reference = new ObjectPropertyReference(resolver.resolve("FooBar", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_OBJECT, reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("FooBar", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        // object without property, parsed as property
        reference = new ObjectPropertyReference(resolver.resolve("page^Object", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_OBJECT, reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("page^Object", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        // empty prop
        reference =
            new ObjectPropertyReference(resolver.resolve("wiki:space.page^Object.", EntityType.OBJECT_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals(DEFAULT_OBJECT_PROPERTY, reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        // test separator escape
        reference =
            new ObjectPropertyReference(resolver.resolve("wiki:space.page^Object.prop\\.erty",
                EntityType.OBJECT_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("Object", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("prop.erty", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        // and that separators don't need to be escaped other than in the property name
        reference =
            new ObjectPropertyReference(resolver.resolve("wiki:space.page^x.wiki.class[0].prop",
                EntityType.OBJECT_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("x.wiki.class[0]", reference.extractReference(EntityType.OBJECT).getName());
        assertEquals("prop", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());

        reference = new ObjectPropertyReference(resolver.resolve(":^\\.@", EntityType.OBJECT_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_OBJECT, reference.extractReference(EntityType.OBJECT).getName());
        assertEquals(":^.@", reference.extractReference(EntityType.OBJECT_PROPERTY).getName());
    }

    /**
     * Tests resolving object references.
     */
    @Test
    public void testResolveClassPropertyReference()
    {
        EntityReference reference =
            new ClassPropertyReference(resolver.resolve("wiki:space.page^ClassProp", EntityType.CLASS_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("ClassProp", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        // default values
        reference = new ClassPropertyReference(resolver.resolve("", EntityType.CLASS_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_CLASS_PROPERTY, reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        // without some of the parents
        reference = new ClassPropertyReference(resolver.resolve("space.page^ClassProp", EntityType.CLASS_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("ClassProp", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        reference = new ClassPropertyReference(resolver.resolve("page^ClassProp", EntityType.CLASS_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("ClassProp", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        reference = new ClassPropertyReference(resolver.resolve("XWiki.Class", EntityType.CLASS_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("XWiki.Class", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        // property without object
        reference = new ClassPropertyReference(resolver.resolve("wiki:space.page.property", EntityType.CLASS_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        Assert
            .assertEquals("wiki:space.page.property", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        // object with no name
        reference = new ClassPropertyReference(resolver.resolve("wiki:space.page^", EntityType.CLASS_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(DEFAULT_CLASS_PROPERTY, reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        // test separator escape
        reference =
            new ClassPropertyReference(resolver.resolve("wiki:space.page^obje\\^ct", EntityType.CLASS_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("obje^ct", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        // and that separators don't need to be escaped other than in the object name
        reference =
            new ClassPropertyReference(resolver.resolve("wiki:spa^ce.page^ClassProp", EntityType.CLASS_PROPERTY));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("spa^ce", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals("ClassProp", reference.extractReference(EntityType.CLASS_PROPERTY).getName());

        reference = new ClassPropertyReference(resolver.resolve(":.\\^@", EntityType.CLASS_PROPERTY));
        assertEquals(DEFAULT_WIKI, reference.extractReference(EntityType.WIKI).getName());
        assertEquals(DEFAULT_SPACE, reference.extractReference(EntityType.SPACE).getName());
        assertEquals(DEFAULT_PAGE, reference.extractReference(EntityType.DOCUMENT).getName());
        assertEquals(":.^@", reference.extractReference(EntityType.CLASS_PROPERTY).getName());
    }

    @Test
    public void testResolveDocumentReferenceWithExplicitReference()
    {
        EntityReference reference =
            resolver.resolve("page", EntityType.DOCUMENT, new EntityReference("space", EntityType.SPACE,
                new EntityReference("wiki", EntityType.WIKI)));
        assertEquals("wiki", reference.extractReference(EntityType.WIKI).getName());
        assertEquals("space", reference.extractReference(EntityType.SPACE).getName());
        assertEquals("page", reference.getName());
    }

    /**
     * Tests resolving space references.
     */
    @Test
    public void testResolveSpaceReference()
    {
        SpaceReference reference = new SpaceReference(this.resolver.resolve("space", EntityType.SPACE));
        assertEquals("space", reference.getName());

        // several spaces
        reference = new SpaceReference(this.resolver.resolve("space1.space2", EntityType.SPACE));
        assertEquals("space2", reference.getName());
        assertEquals("space1", reference.getParent().getName());
    }

    /**
     * Tests resolving wiki references.
     */
    @Test
    public void testResolveWikiReference()
    {
        WikiReference reference = new WikiReference(resolver.resolve("dev", EntityType.WIKI));
        assertEquals("dev", reference.getName());

        // default value
        reference = new WikiReference(resolver.resolve("", EntityType.WIKI));
        assertEquals(DEFAULT_WIKI, reference.getName());

        // with parameter
        DocumentReference baseReference = new DocumentReference("math", "Theorems", "Pitagora");
        reference = new WikiReference(resolver.resolve("", EntityType.WIKI, baseReference));
        assertEquals(baseReference.getWikiReference(), reference);

        // special symbols
        reference = new WikiReference(resolver.resolve("dev:lab.uix", EntityType.WIKI));
        assertEquals("dev:lab.uix", reference.getName());

        // escaping
        reference = new WikiReference(resolver.resolve("dev\\:lab\\.uix", EntityType.WIKI));
        assertEquals("dev:lab.uix", reference.getName());
    }
}
