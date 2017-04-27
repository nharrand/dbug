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
package com.xpn.xwiki.doc;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.model.reference.ObjectReference;
import org.xwiki.model.reference.SpaceReference;
import org.xwiki.model.reference.WikiReference;
import org.xwiki.query.Query;
import org.xwiki.query.QueryFilter;
import org.xwiki.rendering.internal.syntax.DefaultSyntaxFactory;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.security.authorization.AccessDeniedException;
import org.xwiki.security.authorization.Right;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentManagerRule;

import com.xpn.xwiki.XWikiConstant;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.StringProperty;
import com.xpn.xwiki.objects.classes.BaseClass;
import com.xpn.xwiki.objects.classes.TextAreaClass;
import com.xpn.xwiki.objects.meta.MetaClass;
import com.xpn.xwiki.objects.meta.StaticListMetaClass;
import com.xpn.xwiki.test.MockitoOldcoreRule;
import com.xpn.xwiki.test.reference.ReferenceComponentList;
import com.xpn.xwiki.validation.XWikiValidationInterface;
import com.xpn.xwiki.web.EditForm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link XWikiDocument}.
 * 
 * @version $Id: 8a0ec4833d1ea1ef9d165cbdc6b3705a82847ba8 $
 */
@ReferenceComponentList
@ComponentList({DefaultSyntaxFactory.class, StaticListMetaClass.class})
public class XWikiDocumentMockitoTest
{
    private static final String DOCWIKI = "wiki";

    private static final String DOCSPACE = "space";

    private static final String DOCNAME = "page";

    private static final DocumentReference DOCUMENT_REFERENCE = new DocumentReference(DOCWIKI, DOCSPACE, DOCNAME);

    private static final DocumentReference CLASS_REFERENCE = DOCUMENT_REFERENCE;

    @Rule
    public MockitoOldcoreRule oldcore = new MockitoOldcoreRule();

    /**
     * The object being tested.
     */
    private XWikiDocument document;

    private BaseClass baseClass;

    private BaseObject baseObject;

    private BaseObject baseObject2;

    private EntityReferenceSerializer<String> defaultEntityReferenceSerializer;

    @Before
    public void setUp() throws Exception
    {
        this.oldcore.registerMockEnvironment();

        // Activate programming rights in order to be able to call com.xpn.xwiki.api.Document#getDocument().
        when(this.oldcore.getMockRightService().hasProgrammingRights(this.oldcore.getXWikiContext())).thenReturn(true);

        this.document = new XWikiDocument(DOCUMENT_REFERENCE);
        this.document.setSyntax(Syntax.PLAIN_1_0);

        this.baseClass = this.document.getXClass();
        this.baseClass.addTextField("string", "String", 30);
        this.baseClass.addTextAreaField("area", "Area", 10, 10);
        this.baseClass.addTextAreaField("puretextarea", "Pure text area", 10, 10);
        // set the text areas an non interpreted content
        ((TextAreaClass) this.baseClass.getField("puretextarea")).setContentType("puretext");
        this.baseClass.addPasswordField("passwd", "Password", 30);
        this.baseClass.addBooleanField("boolean", "Boolean", "yesno");
        this.baseClass.addNumberField("int", "Int", 10, "integer");
        this.baseClass.addStaticListField("stringlist", "StringList", "value1, value2");

        this.baseObject = this.document.newXObject(CLASS_REFERENCE, this.oldcore.getXWikiContext());
        this.baseObject.setStringValue("string", "string");
        this.baseObject.setLargeStringValue("area", "area");
        this.baseObject.setStringValue("passwd", "passwd");
        this.baseObject.setIntValue("boolean", 1);
        this.baseObject.setIntValue("int", 42);
        this.baseObject.setStringListValue("stringlist", Arrays.asList("VALUE1", "VALUE2"));

        this.baseObject2 = this.baseObject.clone();
        this.document.addXObject(this.baseObject2);

        this.oldcore.getSpyXWiki().saveDocument(this.document, "", true, this.oldcore.getXWikiContext());

        this.defaultEntityReferenceSerializer =
            this.oldcore.getMocker().getInstance(EntityReferenceSerializer.TYPE_STRING);

        this.oldcore.getXWikiContext().setWikiId(DOCWIKI);

        // Reset the cached (static) MetaClass instance because it may have been initialized during the execution of the
        // previous test classes, so before the StaticListMetaClass component needed by this test class was loaded.
        MetaClass.setMetaClass(null);
    }

    @Test
    public void getChildrenReferences() throws Exception
    {
        Query query = mock(Query.class);
        when(this.oldcore.getQueryManager().createQuery(anyString(), eq(Query.XWQL))).thenReturn(query);

        QueryFilter hiddenFilter = this.oldcore.getMocker().registerMockComponent(QueryFilter.class, "hidden");

        when(query.setLimit(7)).thenReturn(query);

        List<String> result = Arrays.asList("X.y", "A.b");
        when(query.<String>execute()).thenReturn(result);

        List<DocumentReference> childrenReferences =
            document.getChildrenReferences(7, 3, this.oldcore.getXWikiContext());

        verify(query).addFilter(hiddenFilter);
        verify(query).setLimit(7);
        verify(query).setOffset(3);

        Assert.assertEquals(2, childrenReferences.size());
        Assert.assertEquals(new DocumentReference("wiki", "X", "y"), childrenReferences.get(0));
        Assert.assertEquals(new DocumentReference("wiki", "A", "b"), childrenReferences.get(1));
    }

    /**
     * Generate a fake map for the request used in the tests of {@link #readObjectsFromForm()} and
     * {@link #readObjectsFromFormUpdateOrCreate()}.
     * 
     * @return Map of fake parameters which should test every cases
     */
    private Map<String, String[]> generateFakeRequestMap()
    {
        Map<String, String[]> parameters = new HashMap<>();
        // Testing update of values in existing object with existing properties
        String[] string1 = { "bloublou" };
        parameters.put("space.page_0_string", string1);
        String[] int1 = { "7" };
        parameters.put("space.page_1_int", int1);
        // Testing creation and update of an object's properties when object
        // doesn't exist
        String[] string2 = { "blabla" };
        String[] int2 = { "13" };
        parameters.put("space.page_3_string", string2);
        parameters.put("space.page_3_int", int2);
        // Testing that objects with non-following number is not created
        parameters.put("space.page_42_string", string1);
        parameters.put("space.page_42_int", int1);
        // Testing that invalid parameter are ignored
        parameters.put("invalid", new String[] { "whatever" });
        // Testing that invalid xclass page are ignored
        parameters.put("InvalidSpace.InvalidPage_0_string", new String[] { "whatever" });
        // Testing that an invalid number is ignored (first should be ignored by
        // regexp parser, second by an exception)
        parameters.put("space.page_notANumber_string", new String[] { "whatever" });
        parameters.put("space.page_9999999999_string", new String[] { "whatever" });
        return parameters;
    }

    /**
     * Generate the fake class that is used for the test of {@link #readObjectsFromForm()} and
     * {@link #readObjectsFromFormUpdateOrCreate()}.
     * 
     * @return The fake BaseClass
     */
    private BaseClass generateFakeClass()
    {
        BaseClass baseClass = this.document.getXClass();
        baseClass.addTextField("string", "String", 30);
        baseClass.addTextAreaField("area", "Area", 10, 10);
        baseClass.addTextAreaField("puretextarea", "Pure text area", 10, 10);
        // set the text areas an non interpreted content
        ((TextAreaClass) baseClass.getField("puretextarea")).setContentType("puretext");
        baseClass.addPasswordField("passwd", "Password", 30);
        baseClass.addBooleanField("boolean", "Boolean", "yesno");
        baseClass.addNumberField("int", "Int", 10, "integer");
        baseClass.addStaticListField("stringlist", "StringList", "value1, value2");

        return baseClass;
    }

    /**
     * Generate 2 clones of a fake object in the document
     * 
     * @return Return the reference of the first clone
     */
    private void generateFakeObjects()
    {
        BaseObject baseObject = null, baseObject2 = null, baseObject3 = null;
        try {
            baseObject = this.document.newXObject(this.document.getDocumentReference(), this.oldcore.getXWikiContext());
            baseObject2 =
                this.document.newXObject(this.document.getDocumentReference(), this.oldcore.getXWikiContext());
            baseObject3 =
                this.document.newXObject(this.document.getDocumentReference(), this.oldcore.getXWikiContext());
        } catch (XWikiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        baseObject.setStringValue("string", "string");
        baseObject.setIntValue("int", 42);
        baseObject2.setStringValue("string", "string2");
        baseObject2.setIntValue("int", 42);
        baseObject3.setStringValue("string", "string3");
        baseObject3.setIntValue("int", 42);
    }

    /**
     * Unit test for {@link XWikiDocument#readObjectsFromForm(EditForm, XWikiContext)}.
     */
    @Test
    public void readObjectsFromForm() throws Exception
    {
        this.document = new XWikiDocument(new DocumentReference(DOCWIKI, DOCSPACE, DOCNAME));
        this.oldcore.getSpyXWiki().saveDocument(this.document, "", true, this.oldcore.getXWikiContext());

        HttpServletRequest request = mock(HttpServletRequest.class);
        MockitoComponentManagerRule mocker = this.oldcore.getMocker();
        XWikiContext context = this.oldcore.getXWikiContext();
        DocumentReferenceResolver<String> documentReferenceResolverString =
            mocker.registerMockComponent(DocumentReferenceResolver.TYPE_STRING, "current");
        // Entity Reference resolver is used in <BaseObject>.getXClass()
        DocumentReferenceResolver<EntityReference> documentReferenceResolverEntity =
            mocker.registerMockComponent(DocumentReferenceResolver.TYPE_REFERENCE, "current");
        EntityReferenceSerializer<String> entityReferenceResolver =
            mocker.registerMockComponent(EntityReferenceSerializer.TYPE_STRING, "local");

        Map<String, String[]> parameters = generateFakeRequestMap();
        BaseClass baseClass = generateFakeClass();
        generateFakeObjects();

        when(request.getParameterMap()).thenReturn(parameters);

        DocumentReference documentReference = new DocumentReference("wiki", "space", "page");
        // This entity resolver with this 'resolve' method is used in
        // <BaseCollection>.getXClassReference()
        when(documentReferenceResolverEntity.resolve(any(EntityReference.class), any(DocumentReference.class)))
            .thenReturn(this.document.getDocumentReference());
        when(documentReferenceResolverString.resolve("space.page")).thenReturn(documentReference);
        when(entityReferenceResolver.serialize(any(EntityReference.class))).thenReturn("space.page");

        EditForm eform = new EditForm();
        eform.setRequest(request);
        document.readObjectsFromForm(eform, context);

        assertEquals(3, this.document.getXObjectSize(baseClass.getDocumentReference()));
        assertEquals("string", this.document.getXObject(baseClass.getDocumentReference(), 0).getStringValue("string"));
        assertEquals(42, this.document.getXObject(baseClass.getDocumentReference(), 0).getIntValue("int"));
        assertEquals("string2", this.document.getXObject(baseClass.getDocumentReference(), 1).getStringValue("string"));
        assertEquals(42, this.document.getXObject(baseClass.getDocumentReference(), 1).getIntValue("int"));
        assertEquals("string3", this.document.getXObject(baseClass.getDocumentReference(), 2).getStringValue("string"));
        assertEquals(42, this.document.getXObject(baseClass.getDocumentReference(), 2).getIntValue("int"));
        assertNull(this.document.getXObject(baseClass.getDocumentReference(), 3));
        assertNull(this.document.getXObject(baseClass.getDocumentReference(), 42));
    }

    /**
     * Unit test for {@link XWikiDocument#readObjectsFromFormUpdateOrCreate(EditForm, XWikiContext)} .
     */
    @Test
    public void readObjectsFromFormUpdateOrCreate() throws Exception
    {
        this.document = new XWikiDocument(new DocumentReference(DOCWIKI, DOCSPACE, DOCNAME));
        this.oldcore.getSpyXWiki().saveDocument(this.document, "", true, this.oldcore.getXWikiContext());

        HttpServletRequest request = mock(HttpServletRequest.class);
        MockitoComponentManagerRule mocker = this.oldcore.getMocker();
        XWikiContext context = this.oldcore.getXWikiContext();
        DocumentReferenceResolver<String> documentReferenceResolverString =
            mocker.registerMockComponent(DocumentReferenceResolver.TYPE_STRING, "current");
        // Entity Reference resolver is used in <BaseObject>.getXClass()
        DocumentReferenceResolver<EntityReference> documentReferenceResolverEntity =
            mocker.registerMockComponent(DocumentReferenceResolver.TYPE_REFERENCE, "current");

        Map<String, String[]> parameters = generateFakeRequestMap();
        BaseClass baseClass = generateFakeClass();
        generateFakeObjects();
        EditForm eform = new EditForm();

        when(request.getParameterMap()).thenReturn(parameters);
        when(documentReferenceResolverString.resolve("space.page")).thenReturn(this.document.getDocumentReference());
        when(documentReferenceResolverString.resolve("InvalidSpace.InvalidPage"))
            .thenReturn(new DocumentReference("wiki", "InvalidSpace", "InvalidPage"));
        // This entity resolver with this 'resolve' method is used in
        // <BaseCollection>.getXClassReference()
        when(documentReferenceResolverEntity.resolve(any(EntityReference.class), any(DocumentReference.class)))
            .thenReturn(this.document.getDocumentReference());
        doReturn(this.document).when(this.oldcore.getSpyXWiki()).getDocument(this.document.getDocumentReference(),
            context);

        eform.setRequest(request);
        this.document.readObjectsFromFormUpdateOrCreate(eform, context);

        assertEquals(43, this.document.getXObjectSize(baseClass.getDocumentReference()));
        assertEquals("bloublou",
            this.document.getXObject(baseClass.getDocumentReference(), 0).getStringValue("string"));
        assertEquals(42, this.document.getXObject(baseClass.getDocumentReference(), 0).getIntValue("int"));
        assertEquals("string2", this.document.getXObject(baseClass.getDocumentReference(), 1).getStringValue("string"));
        assertEquals(7, this.document.getXObject(baseClass.getDocumentReference(), 1).getIntValue("int"));
        assertEquals("string3", this.document.getXObject(baseClass.getDocumentReference(), 2).getStringValue("string"));
        assertEquals(42, this.document.getXObject(baseClass.getDocumentReference(), 2).getIntValue("int"));
        assertNotNull(this.document.getXObject(baseClass.getDocumentReference(), 3));
        assertEquals("blabla", this.document.getXObject(baseClass.getDocumentReference(), 3).getStringValue("string"));
        assertEquals(13, this.document.getXObject(baseClass.getDocumentReference(), 3).getIntValue("int"));
        assertNotNull(this.document.getXObject(baseClass.getDocumentReference(), 42));
        assertEquals("bloublou",
            this.document.getXObject(baseClass.getDocumentReference(), 42).getStringValue("string"));
        assertEquals(7, this.document.getXObject(baseClass.getDocumentReference(), 42).getIntValue("int"));
    }

    @Test
    public void testDeprecatedConstructors()
    {
        DocumentReference defaultReference = new DocumentReference("xwiki", "Main", "WebHome");

        XWikiDocument doc = new XWikiDocument(null);
        assertEquals(defaultReference, doc.getDocumentReference());

        doc = new XWikiDocument();
        assertEquals(defaultReference, doc.getDocumentReference());

        doc = new XWikiDocument("notused", "space.page");
        assertEquals("space", doc.getSpaceName());
        assertEquals("page", doc.getPageName());
        assertEquals(this.oldcore.getXWikiContext().getWikiId(), doc.getWikiName());

        doc = new XWikiDocument("space", "page");
        assertEquals("space", doc.getSpaceName());
        assertEquals("page", doc.getPageName());
        assertEquals(this.oldcore.getXWikiContext().getWikiId(), doc.getWikiName());

        doc = new XWikiDocument("wiki2", "space", "page");
        assertEquals("space", doc.getSpaceName());
        assertEquals("page", doc.getPageName());
        assertEquals("wiki2", doc.getWikiName());

        doc = new XWikiDocument("wiki2", "notused", "notused:space.page");
        assertEquals("space", doc.getSpaceName());
        assertEquals("page", doc.getPageName());
        assertEquals("wiki2", doc.getWikiName());
    }

    @Test
    public void testMinorMajorVersions()
    {
        this.document = new XWikiDocument(new DocumentReference(DOCWIKI, DOCSPACE, DOCNAME));

        // there is no version in doc yet, so 1.1
        assertEquals("1.1", this.document.getVersion());

        this.document.setMinorEdit(false);
        this.document.incrementVersion();
        // no version => incrementVersion sets 1.1
        assertEquals("1.1", this.document.getVersion());

        this.document.setMinorEdit(false);
        this.document.incrementVersion();
        // increment major version
        assertEquals("2.1", this.document.getVersion());

        this.document.setMinorEdit(true);
        this.document.incrementVersion();
        // increment minor version
        assertEquals("2.2", this.document.getVersion());
    }

    @Test
    public void testGetPreviousVersion() throws XWikiException
    {
        this.document = new XWikiDocument(new DocumentReference(DOCWIKI, DOCSPACE, DOCNAME));

        Date now = new Date();
        XWikiDocumentArchive archiveDoc = new XWikiDocumentArchive(this.document.getId());
        this.document.setDocumentArchive(archiveDoc);

        assertEquals("1.1", this.document.getVersion());
        assertNull(this.document.getPreviousVersion());

        this.document.incrementVersion();
        archiveDoc.updateArchive(this.document, "Admin", now, "", this.document.getRCSVersion(),
            this.oldcore.getXWikiContext());
        assertEquals("1.1", this.document.getVersion());
        assertNull(this.document.getPreviousVersion());

        this.document.setMinorEdit(true);
        this.document.incrementVersion();
        archiveDoc.updateArchive(this.document, "Admin", now, "", this.document.getRCSVersion(),
            this.oldcore.getXWikiContext());
        assertEquals("1.2", this.document.getVersion());
        assertEquals("1.1", this.document.getPreviousVersion());

        this.document.setMinorEdit(false);
        this.document.incrementVersion();
        archiveDoc.updateArchive(this.document, "Admin", now, "", this.document.getRCSVersion(),
            this.oldcore.getXWikiContext());
        assertEquals("2.1", this.document.getVersion());
        assertEquals("1.2", this.document.getPreviousVersion());

        this.document.setMinorEdit(true);
        this.document.incrementVersion();
        archiveDoc.updateArchive(this.document, "Admin", now, "", this.document.getRCSVersion(),
            this.oldcore.getXWikiContext());
        assertEquals("2.2", this.document.getVersion());
        assertEquals("2.1", this.document.getPreviousVersion());

        archiveDoc.resetArchive();

        assertEquals("2.2", this.document.getVersion());
        assertNull(this.document.getPreviousVersion());
    }

    @Test
    public void testCloneNullObjects()
    {
        XWikiDocument document = new XWikiDocument(new DocumentReference("wiki", DOCSPACE, DOCNAME));

        EntityReference relativeClassReference =
            new EntityReference(DOCNAME, EntityType.DOCUMENT, new EntityReference(DOCSPACE, EntityType.SPACE));
        DocumentReference classReference = new DocumentReference("wiki", DOCSPACE, DOCNAME);
        DocumentReference duplicatedClassReference = new DocumentReference("otherwiki", DOCSPACE, DOCNAME);

        // no object
        XWikiDocument clonedDocument = document.clone();
        assertTrue(clonedDocument.getXObjects().isEmpty());

        XWikiDocument duplicatedDocument = document.duplicate(new DocumentReference("otherwiki", DOCSPACE, DOCNAME));
        assertTrue(duplicatedDocument.getXObjects().isEmpty());

        // 1 null object

        document.addXObject(classReference, null);

        clonedDocument = document.clone();
        assertEquals(1, clonedDocument.getXObjects(classReference).size());
        assertEquals(document.getXObjects(classReference), clonedDocument.getXObjects(classReference));

        duplicatedDocument = document.duplicate(new DocumentReference("otherwiki", DOCSPACE, DOCNAME));
        assertTrue(duplicatedDocument.getXObjects().isEmpty());

        // 1 null object and 1 object

        BaseObject object = new BaseObject();
        object.setXClassReference(relativeClassReference);
        document.addXObject(object);

        clonedDocument = document.clone();
        assertEquals(2, clonedDocument.getXObjects(classReference).size());
        assertEquals(document.getXObjects(classReference), clonedDocument.getXObjects(classReference));

        duplicatedDocument = document.duplicate(new DocumentReference("otherwiki", DOCSPACE, DOCNAME));
        assertEquals(2, duplicatedDocument.getXObjects(duplicatedClassReference).size());
    }

    @Test
    public void testToStringReturnsFullName()
    {
        assertEquals("space.page", this.document.toString());
        assertEquals("Main.WebHome", new XWikiDocument().toString());
    }

    @Test
    public void testCloneSaveVersions()
    {
        XWikiDocument doc1 = new XWikiDocument(new DocumentReference("qwe", "qwe", "qwe"));
        XWikiDocument doc2 = doc1.clone();
        doc1.incrementVersion();
        doc2.incrementVersion();
        assertEquals(doc1.getVersion(), doc2.getVersion());
    }

    @Test
    public void testAddObject() throws XWikiException
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("test", "test", "document"));
        BaseObject object = BaseClass.newCustomClassInstance("XWiki.XWikiUsers", this.oldcore.getXWikiContext());
        doc.addObject("XWiki.XWikiUsers", object);
        assertEquals("XWikiDocument.addObject does not set the object's name", doc.getFullName(), object.getName());
    }

    @Test
    public void testObjectNumbersAfterXMLRoundrip() throws XWikiException
    {
        String wiki = oldcore.getXWikiContext().getWikiId();

        XWikiDocument tagDocument = new XWikiDocument(new DocumentReference(wiki, "XWiki", "TagClass"));
        BaseClass tagClass = tagDocument.getXClass();
        tagClass.addStaticListField(XWikiConstant.TAG_CLASS_PROP_TAGS, "Tags", 30, true, "", "checkbox");
        this.oldcore.getSpyXWiki().saveDocument(tagDocument, this.oldcore.getXWikiContext());

        XWikiDocument doc = new XWikiDocument(new DocumentReference(wiki, "test", "document"));
        doReturn("iso-8859-1").when(this.oldcore.getSpyXWiki()).getEncoding();

        BaseObject object1 = doc.newXObject(tagDocument.getDocumentReference(), this.oldcore.getXWikiContext());
        BaseObject object2 = doc.newXObject(tagDocument.getDocumentReference(), this.oldcore.getXWikiContext());
        BaseObject object3 = doc.newXObject(tagDocument.getDocumentReference(), this.oldcore.getXWikiContext());

        // Remove first object
        doc.removeXObject(object1);

        String docXML = doc.toXML(this.oldcore.getXWikiContext());
        XWikiDocument docFromXML = new XWikiDocument(doc.getDocumentReference());
        docFromXML.fromXML(docXML);

        List<BaseObject> objects = doc.getXObjects(tagDocument.getDocumentReference());
        List<BaseObject> objectsFromXML = docFromXML.getXObjects(tagDocument.getDocumentReference());

        assertNotNull(objects);
        assertNotNull(objectsFromXML);

        assertTrue(objects.size() == objectsFromXML.size());

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) == null) {
                assertNull(objectsFromXML.get(i));
            } else {
                assertTrue(objects.get(i).getNumber() == objectsFromXML.get(i).getNumber());
            }
        }
    }

    @Test
    public void testGetXObjectWithObjectReference()
    {
        Assert.assertSame(this.baseObject, this.document.getXObject(this.baseObject.getReference()));

        Assert.assertSame(this.baseObject,
            this.document.getXObject(new ObjectReference(
                this.defaultEntityReferenceSerializer.serialize(this.baseObject.getXClassReference()),
                this.document.getDocumentReference())));
    }

    @Test
    public void testGetXObjectWithNumber()
    {
        Assert.assertSame(this.baseObject, this.document.getXObject(CLASS_REFERENCE, this.baseObject.getNumber()));
        Assert.assertSame(this.baseObject2, this.document.getXObject(CLASS_REFERENCE, this.baseObject2.getNumber()));
        Assert.assertSame(this.baseObject,
            this.document.getXObject((EntityReference) CLASS_REFERENCE, this.baseObject.getNumber()));
        Assert.assertSame(this.baseObject2,
            this.document.getXObject((EntityReference) CLASS_REFERENCE, this.baseObject2.getNumber()));
    }

    @Test
    public void testSetXObjectswithPreviousObject()
    {
        BaseObject object = new BaseObject();
        object.setXClassReference(this.baseObject.getXClassReference());
        this.document.addXObject(object);

        this.document.setXObjects(this.baseObject.getXClassReference(), Arrays.asList(object));

        Assert.assertEquals(Arrays.asList(object), this.document.getXObjects(this.baseObject.getXClassReference()));
    }

    @Test
    public void testSetXObjectWhithNoPreviousObject()
    {
        XWikiDocument document = new XWikiDocument(this.document.getDocumentReference());

        document.setXObject(this.baseObject.getXClassReference(), 0, this.baseObject);

        Assert.assertEquals(Arrays.asList(this.baseObject), document.getXObjects(this.baseObject.getXClassReference()));
    }

    /**
     * Test that the parent remain the same relative value whatever the context.
     */
    @Test
    public void testGetParent()
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("docwiki", "docspace", "docpage"));

        assertEquals("", doc.getParent());
        doc.setParent(null);
        assertEquals("", doc.getParent());

        doc.setParent("page");
        assertEquals("page", doc.getParent());

        this.oldcore.getXWikiContext().setWikiId("otherwiki");
        assertEquals("page", doc.getParent());

        doc.setDocumentReference(new DocumentReference("otherwiki", "otherspace", "otherpage"));
        assertEquals("page", doc.getParent());
    }

    @Test
    public void testGetParentReference()
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("docwiki", "docspace", "docpage"));

        assertNull(doc.getParentReference());

        doc.setParent("parentpage");

        // ////////////////////////////////////////////////////////////////
        // The following tests are checking that document reference cache is properly cleaned something could make the
        // parent change

        assertEquals(new DocumentReference("docwiki", "docspace", "parentpage"), doc.getParentReference());

        doc.setName("docpage2");
        assertEquals(new DocumentReference("docwiki", "docspace", "parentpage"), doc.getParentReference());

        doc.setSpace("docspace2");
        assertEquals(new DocumentReference("docwiki", "docspace2", "parentpage"), doc.getParentReference());

        doc.setDatabase("docwiki2");
        assertEquals(new DocumentReference("docwiki2", "docspace2", "parentpage"), doc.getParentReference());

        doc.setDocumentReference(new DocumentReference("docwiki", "docspace", "docpage"));
        assertEquals(new DocumentReference("docwiki", "docspace", "parentpage"), doc.getParentReference());

        doc.setFullName("docwiki2:docspace2.docpage2", this.oldcore.getXWikiContext());
        assertEquals(new DocumentReference("docwiki2", "docspace2", "parentpage"), doc.getParentReference());

        doc.setParent("parentpage2");
        assertEquals(new DocumentReference("docwiki2", "docspace2", "parentpage2"), doc.getParentReference());
    }

    @Test
    public void testSetRelativeParentReference()
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("docwiki", "docspace", "docpage"));

        doc.setParentReference(new EntityReference("docpage2", EntityType.DOCUMENT));
        assertEquals(new DocumentReference("docwiki", "docspace", "docpage2"), doc.getParentReference());
        assertEquals("docpage2", doc.getParent());
    }

    /**
     * Verify that setting a new creator will create a new revision (we verify that that metadata dirty flag is set to
     * true).
     * 
     * @see <a href="http://jira.xwiki.org/jira/browse/XWIKI-7445">XWIKI-7445</a>
     */
    @Test
    public void testSetCreatorReferenceSetsMetadataDirtyFlag()
    {
        // Make sure we set the flag to false to verify it's changed
        this.document.setMetaDataDirty(false);

        DocumentReference creator = new DocumentReference("Wiki", "XWiki", "Creator");
        this.document.setCreatorReference(creator);

        assertEquals(true, this.document.isMetaDataDirty());
    }

    /**
     * Verify that setting a new creator that is the same as the currenet creator doesn't create a new revision (we
     * verify that the metadata dirty flag is not set).
     * 
     * @see <a href="http://jira.xwiki.org/jira/browse/XWIKI-7445">XWIKI-7445</a>
     */
    @Test
    public void testSetCreatorReferenceWithSameCreatorDoesntSetMetadataDirtyFlag()
    {
        // Make sure we set the metadata dirty flag to false to verify it's not changed thereafter
        DocumentReference creator = new DocumentReference("Wiki", "XWiki", "Creator");
        this.document.setCreatorReference(creator);
        this.document.setMetaDataDirty(false);

        // Set the creator with the same reference to verify it doesn't change the flag
        this.document.setCreatorReference(new DocumentReference("Wiki", "XWiki", "Creator"));

        assertEquals(false, this.document.isMetaDataDirty());
    }

    /**
     * Verify that setting a new author will create a new revision (we verify that that metadata dirty flag is set to
     * true).
     * 
     * @see <a href="http://jira.xwiki.org/jira/browse/XWIKI-7445">XWIKI-7445</a>
     */
    @Test
    public void testSetAuthorReferenceSetsMetadataDirtyFlag()
    {
        // Make sure we set the flag to false to verify it's changed
        this.document.setMetaDataDirty(false);

        DocumentReference author = new DocumentReference("Wiki", "XWiki", "Author");
        this.document.setAuthorReference(author);

        assertEquals(true, this.document.isMetaDataDirty());
    }

    /**
     * Verify that setting a new author that is the same as the currenet creator doesn't create a new revision (we
     * verify that the metadata dirty flag is not set).
     * 
     * @see <a href="http://jira.xwiki.org/jira/browse/XWIKI-7445">XWIKI-7445</a>
     */
    @Test
    public void testSetAuthorReferenceWithSameAuthorDoesntSetMetadataDirtyFlag()
    {
        // Make sure we set the metadata dirty flag to false to verify it's not changed thereafter
        DocumentReference author = new DocumentReference("Wiki", "XWiki", "Author");
        this.document.setAuthorReference(author);
        this.document.setMetaDataDirty(false);

        // Set the author with the same reference to verify it doesn't change the flag
        this.document.setAuthorReference(new DocumentReference("Wiki", "XWiki", "Author"));

        assertEquals(false, this.document.isMetaDataDirty());
    }

    /**
     * Verify that setting a new content author will create a new revision (we verify that that metadata dirty flag is
     * set to true).
     * 
     * @see <a href="http://jira.xwiki.org/jira/browse/XWIKI-7445">XWIKI-7445</a>
     */
    @Test
    public void testSetContentAuthorReferenceSetsMetadataDirtyFlag()
    {
        // Make sure we set the flag to false to verify it's changed
        this.document.setMetaDataDirty(false);

        DocumentReference contentAuthor = new DocumentReference("Wiki", "XWiki", "ContentAuthor");
        this.document.setContentAuthorReference(contentAuthor);

        assertEquals(true, this.document.isMetaDataDirty());
    }

    /**
     * Verify that setting a new content author that is the same as the currenet creator doesn't create a new revision
     * (we verify that the metadata dirty flag is not set).
     * 
     * @see <a href="http://jira.xwiki.org/jira/browse/XWIKI-7445">XWIKI-7445</a>
     */
    @Test
    public void testSetContentAuthorReferenceWithSameContentAuthorDoesntSetMetadataDirtyFlag()
    {
        // Make sure we set the metadata dirty flag to false to verify it's not changed thereafter
        DocumentReference contentAuthor = new DocumentReference("Wiki", "XWiki", "ContentAuthor");
        this.document.setContentAuthorReference(contentAuthor);
        this.document.setMetaDataDirty(false);

        // Set the content author with the same reference to verify it doesn't change the flag
        this.document.setContentAuthorReference(new DocumentReference("Wiki", "XWiki", "ContentAuthor"));

        assertEquals(false, this.document.isMetaDataDirty());
    }

    @Test
    public void testSetContentSetsContentDirtyFlag()
    {
        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        this.document.setContent("something");

        assertTrue(this.document.isContentDirty());
        assertFalse(this.document.isMetaDataDirty());
    }

    @Test
    public void testSetSameContentDoesNotSetContentDirtyFlag()
    {
        this.document.setContent("something");
        // Make sure we set the flag to false to verify it's changed
        this.document.setContentDirty(false);

        // Set the same content again.
        this.document.setContent("something");

        assertFalse(this.document.isContentDirty());
    }

    @Test
    public void testModifyObjectsSetsOnlyMetadataDirtyFlag() throws Exception
    {
        DocumentReference classReference = this.document.getDocumentReference();

        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // New objects.
        BaseObject object = this.document.newXObject(classReference, this.oldcore.getXWikiContext());

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());

        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // Set/add objects.
        this.document.setXObject(0, object);

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());

        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // Remove objects
        this.document.removeXObject(object);

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());
    }

    @Test
    public void testModifyAttachmentsSetsOnlyMetadataDirtyFlag() throws Exception
    {
        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // Add attachments.
        XWikiAttachment attachment =
            document.addAttachment("file", new ByteArrayInputStream(new byte[] {}), this.oldcore.getXWikiContext());

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());

        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // Add attachments (2).
        document.addAttachment(attachment);

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());

        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // Modify attachment.
        attachment.setContent(new ByteArrayInputStream(new byte[] { 1, 2, 3 }));

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());

        // Make sure we set the flags to false to verify it's changed
        this.document.setContentDirty(false);
        this.document.setMetaDataDirty(false);

        // Remove objects
        this.document.removeAttachment(attachment);

        assertTrue(this.document.isMetaDataDirty());
        assertFalse(this.document.isContentDirty());
    }

    @Test
    public void testEqualsDatas()
    {
        XWikiDocument document = new XWikiDocument(new DocumentReference("wiki", "space", "page"));
        XWikiDocument otherDocument = document.clone();

        Assert.assertTrue(document.equals(otherDocument));
        Assert.assertTrue(document.equalsData(otherDocument));

        otherDocument.setAuthorReference(new DocumentReference("wiki", "space", "otherauthor"));
        otherDocument.setContentAuthorReference(otherDocument.getAuthorReference());
        otherDocument.setCreatorReference(otherDocument.getAuthorReference());
        otherDocument.setVersion("42.0");
        otherDocument.setComment("other comment");
        otherDocument.setMinorEdit(true);

        document.setMinorEdit(false);

        Assert.assertFalse(document.equals(otherDocument));
        Assert.assertTrue(document.equalsData(otherDocument));
    }

    @Test
    public void testEqualsAttachments() throws XWikiException
    {
        XWikiDocument document = new XWikiDocument(new DocumentReference("wiki", "space", "page"));
        XWikiDocument otherDocument = document.clone();

        XWikiAttachment attachment =
            document.addAttachment("file", new byte[] { 1, 2 }, this.oldcore.getXWikiContext());
        XWikiAttachment otherAttachment =
            otherDocument.addAttachment("file", new byte[] { 1, 2 }, this.oldcore.getXWikiContext());

        Assert.assertTrue(document.equals(otherDocument));
        Assert.assertTrue(document.equalsData(otherDocument));

        otherAttachment.setContent(new byte[] { 1, 2, 3 });

        Assert.assertFalse(document.equals(otherDocument));
        Assert.assertFalse(document.equalsData(otherDocument));
    }

    @Test
    public void testSetMetadataDirtyWhenAttachmenListChanges() throws XWikiException
    {
        XWikiDocument document = new XWikiDocument();

        XWikiAttachment attachment =
            document.addAttachment("file", new byte[] { 1, 2 }, this.oldcore.getXWikiContext());

        // Force the metadata not dirty.
        document.setMetaDataDirty(false);

        List<XWikiAttachment> attachments = document.getAttachmentList();
        // Modify (clear) the attachments list)
        attachments.clear();

        // Check that the the metadata is now dirty as a result.
        Assert.assertTrue(document.isMetaDataDirty());

        // Check adding to list
        document.setMetaDataDirty(false);
        attachments.add(new XWikiAttachment());
        Assert.assertTrue(document.isMetaDataDirty());

        // Check removing from the list
        document.setMetaDataDirty(false);
        attachments.remove(0);
        Assert.assertTrue(document.isMetaDataDirty());
    }

    /**
     * XWIKI-8463: Backwards compatibility issue with setting the same attachment list to a document
     */
    @Test
    public void testSetGetAttachmentList() throws Exception
    {
        String attachmentName1 = "someFile.txt";
        String attachmentName2 = "someOtherFile.txt";
        this.document.addAttachment(attachmentName1, new byte[0], this.oldcore.getXWikiContext());
        this.document.addAttachment(attachmentName2, new byte[0], this.oldcore.getXWikiContext());

        List<String> attachmentNames = new ArrayList<String>();

        Assert.assertEquals(2, this.document.getAttachmentList().size());
        for (XWikiAttachment attachment : this.document.getAttachmentList()) {
            attachmentNames.add(attachment.getFilename());
        }
        Assert.assertTrue(attachmentNames.contains(attachmentName1));
        Assert.assertTrue(attachmentNames.contains(attachmentName2));

        // Set back the same list returned by the getter.
        this.document.setAttachmentList(this.document.getAttachmentList());

        // The result needs to stay the same.
        Assert.assertEquals(2, this.document.getAttachmentList().size());
        attachmentNames.clear();
        for (XWikiAttachment attachment : this.document.getAttachmentList()) {
            attachmentNames.add(attachment.getFilename());
        }
        Assert.assertTrue(attachmentNames.contains(attachmentName1));
        Assert.assertTrue(attachmentNames.contains(attachmentName2));
    }

    /**
     * Unit test for {@link XWikiDocument#readFromTemplate(DocumentReference, XWikiContext)}.
     */
    @Test
    public void testReadFromTemplate() throws Exception
    {
        SpaceReference spaceReference = new SpaceReference("Space", new WikiReference("wiki"));
        XWikiDocument template = new XWikiDocument(new DocumentReference("Template", spaceReference));
        template.setParentReference(new EntityReference("Parent", EntityType.DOCUMENT, spaceReference));
        template.setTitle("Enter title here");
        template.setSyntax(Syntax.XWIKI_2_0);
        template.setContent("Enter content here");

        XWikiAttachment templateAttachment = new XWikiAttachment(template, "test.txt");
        String testContent = "test content";
        templateAttachment.setContent(IOUtils.toInputStream(testContent));
        template.addAttachment(templateAttachment);

        this.oldcore.getSpyXWiki().saveDocument(template, this.oldcore.getXWikiContext());

        XWikiDocument target = new XWikiDocument(new DocumentReference("Page", spaceReference));
        target.readFromTemplate(template.getDocumentReference(), this.oldcore.getXWikiContext());

        Assert.assertEquals(template.getDocumentReference(), target.getTemplateDocumentReference());
        Assert.assertEquals(template.getParentReference(), target.getParentReference());
        Assert.assertEquals(template.getTitle(), target.getTitle());
        Assert.assertEquals(template.getSyntax(), target.getSyntax());
        Assert.assertEquals(template.getContent(), target.getContent());
        assertThat(target.getAttachmentList(), Matchers.containsInAnyOrder(target.getAttachmentList().toArray()));
    }

    @Test
    public void testResolveClassReference() throws Exception
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("docwiki", "docspace", "docpage"));

        DocumentReference expected1 = new DocumentReference("docwiki", "XWiki", "docpage");
        assertEquals(expected1, doc.resolveClassReference(""));

        DocumentReference expected2 = new DocumentReference("docwiki", "XWiki", "page");
        assertEquals(expected2, doc.resolveClassReference("page"));

        DocumentReference expected3 = new DocumentReference("docwiki", "space", "page");
        assertEquals(expected3, doc.resolveClassReference("space.page"));

        DocumentReference expected4 = new DocumentReference("wiki", "space", "page");
        assertEquals(expected4, doc.resolveClassReference("wiki:space.page"));
    }

    /**
     * Verify that cloning objects modify their references to point to the document in which they are cloned into.
     */
    @Test
    public void testCloneObjectsHaveCorrectReference()
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("somewiki", "somespace", "somepage"));
        doc.cloneXObjects(this.document);
        assertTrue(doc.getXObjects().size() > 0);

        // Verify that the object references point to the doc in which it's cloned.
        for (Map.Entry<DocumentReference, List<BaseObject>> entry : doc.getXObjects().entrySet()) {
            for (BaseObject baseObject : entry.getValue()) {
                assertEquals(doc.getDocumentReference(), baseObject.getDocumentReference());
            }
        }
    }

    /**
     * Verify that merging objects modify their references to point to the document in which they are cloned into and
     * that GUID for merged objects are different from the original GUIDs.
     */
    @Test
    public void testMergeObjectsHaveCorrectReferenceAndDifferentGuids()
    {
        List<String> originalGuids = new ArrayList<String>();
        for (Map.Entry<DocumentReference, List<BaseObject>> entry : this.document.getXObjects().entrySet()) {
            for (BaseObject baseObject : entry.getValue()) {
                originalGuids.add(baseObject.getGuid());
            }
        }

        // Use a document from a different wiki to see if the class reference of the merged objects is adjusted:
        // documents can't have objects of types defined in a different wiki.
        XWikiDocument doc = new XWikiDocument(new DocumentReference("somewiki", "somespace", "somepage"));
        doc.mergeXObjects(this.document);

        assertTrue(doc.getXObjects().size() > 0);

        for (Map.Entry<DocumentReference, List<BaseObject>> entry : doc.getXObjects().entrySet()) {
            // Verify that the class reference and the target document reference have the same wiki component.
            assertEquals(doc.getDocumentReference().getWikiReference(), entry.getKey().getWikiReference());
            for (BaseObject baseObject : entry.getValue()) {
                // Verify that the object references point to the doc in which it's cloned.
                assertEquals(doc.getDocumentReference(), baseObject.getDocumentReference());
                // Verify that GUIDs are not the same as the original ones
                assertFalse("Non unique object GUID found!", originalGuids.contains(baseObject.getGuid()));
            }
        }
    }

    /**
     * Tests that objects are not copied again when {@link XWikiDocument#mergeXObjects(XWikiDocument)} is called twice.
     */
    @Test
    public void testMergeObjectsTwice()
    {
        // Make sure the target document and the template document are from different wikis.
        XWikiDocument targetDoc = new XWikiDocument(new DocumentReference("someWiki", "someSpace", "somePage"));

        // Merge the objects.
        targetDoc.mergeXObjects(this.document);

        assertEquals(1, targetDoc.getXObjects().size());
        assertEquals(0, targetDoc.getXObjectSize(CLASS_REFERENCE));
        DocumentReference classReference = CLASS_REFERENCE.replaceParent(CLASS_REFERENCE.getWikiReference(),
            targetDoc.getDocumentReference().getWikiReference());
        assertEquals(2, targetDoc.getXObjectSize(classReference));

        // Try to merge the objects again.
        targetDoc.mergeXObjects(this.document);

        // Check that the object from the template document was not copied again.
        assertEquals(2, targetDoc.getXObjectSize(classReference));
    }

    /** Check that a new empty document has empty content (used to have a new line before 2.5). */
    @Test
    public void testInitialContent()
    {
        XWikiDocument doc = new XWikiDocument(new DocumentReference("somewiki", "somespace", "somepage"));
        assertEquals("", doc.getContent());
    }

    @Test
    public void testAuthorAfterDocumentCopy() throws XWikiException
    {
        DocumentReference author = new DocumentReference("Wiki", "XWiki", "Albatross");
        this.document.setAuthorReference(author);
        XWikiDocument copy =
            this.document.copyDocument(this.document.getName() + " Copy", this.oldcore.getXWikiContext());

        assertEquals(author, copy.getAuthorReference());
    }

    @Test
    public void testCreatorAfterDocumentCopy() throws XWikiException
    {
        DocumentReference creator = new DocumentReference("Wiki", "XWiki", "Condor");
        this.document.setCreatorReference(creator);
        XWikiDocument copy =
            this.document.copyDocument(this.document.getName() + " Copy", this.oldcore.getXWikiContext());

        assertEquals(creator, copy.getCreatorReference());
    }

    @Test
    public void testCreationDateAfterDocumentCopy() throws Exception
    {
        Date sourceCreationDate = this.document.getCreationDate();
        Thread.sleep(1000);
        XWikiDocument copy =
            this.document.copyDocument(this.document.getName() + " Copy", this.oldcore.getXWikiContext());

        assertEquals(sourceCreationDate, copy.getCreationDate());
    }

    @Test
    public void testObjectGuidsAfterDocumentCopy() throws Exception
    {
        assertTrue(this.document.getXObjects().size() > 0);

        List<String> originalGuids = new ArrayList<String>();
        for (Map.Entry<DocumentReference, List<BaseObject>> entry : this.document.getXObjects().entrySet()) {
            for (BaseObject baseObject : entry.getValue()) {
                originalGuids.add(baseObject.getGuid());
            }
        }

        XWikiDocument copy =
            this.document.copyDocument(this.document.getName() + " Copy", this.oldcore.getXWikiContext());

        // Verify that the cloned objects have different GUIDs
        for (Map.Entry<DocumentReference, List<BaseObject>> entry : copy.getXObjects().entrySet()) {
            for (BaseObject baseObject : entry.getValue()) {
                assertFalse("Non unique object GUID found!", originalGuids.contains(baseObject.getGuid()));
            }
        }
    }

    @Test
    public void testRelativeObjectReferencesAfterDocumentCopy() throws Exception
    {
        XWikiDocument copy = this.document.copyDocument(new DocumentReference("copywiki", "copyspace", "copypage"),
            this.oldcore.getXWikiContext());

        // Verify that the XObject's XClass reference points to the target wiki and not the old wiki.
        // This tests the XObject cache.
        DocumentReference targetXClassReference = new DocumentReference("copywiki", DOCSPACE, DOCNAME);
        assertNotNull(copy.getXObject(targetXClassReference));

        // Also verify that actual XObject's reference (not from the cache).
        assertEquals(1, copy.getXObjects().size());
        BaseObject bobject = copy.getXObjects().get(copy.getXObjects().keySet().iterator().next()).get(0);
        assertEquals(new DocumentReference("copywiki", DOCSPACE, DOCNAME), bobject.getXClassReference());
    }

    @Test
    public void testCustomMappingAfterDocumentCopy() throws Exception
    {
        this.document.getXClass().setCustomMapping("internal");

        XWikiDocument copy = this.document.copyDocument(new DocumentReference("copywiki", "copyspace", "copypage"),
            this.oldcore.getXWikiContext());

        assertEquals("", copy.getXClass().getCustomMapping());
    }

    /**
     * Normally the xobject vector has the Nth object on the Nth position, but in case an object gets misplaced, trying
     * to remove it should indeed remove that object, and no other.
     */
    @Test
    public void testRemovingObjectWithWrongObjectVector()
    {
        // Setup: Create a document and two xobjects
        BaseObject o1 = new BaseObject();
        BaseObject o2 = new BaseObject();
        o1.setXClassReference(CLASS_REFERENCE);
        o2.setXClassReference(CLASS_REFERENCE);

        // Test: put the second xobject on the third position
        // addObject creates the object vector and configures the objects
        // o1 is added at position 0
        // o2 is added at position 1
        XWikiDocument doc = new XWikiDocument(DOCUMENT_REFERENCE);
        doc.addXObject(o1);
        doc.addXObject(o2);

        // Modify the o2 object's position to ensure it can still be found and removed by the removeObject method.
        assertEquals(1, o2.getNumber());
        o2.setNumber(0);
        // Set a field on o1 so that when comparing it with o2 they are different. This is needed so that the remove
        // will pick the right object to remove (since we've voluntarily set a wrong number of o2 it would pick o1
        // if they were equals).
        o1.addField("somefield", new StringProperty());

        // Call the tested method, removing o2 from position 2 which is set to null
        boolean result = doc.removeXObject(o2);

        // Check the correct behavior:
        assertTrue(result);
        List<BaseObject> objects = doc.getXObjects(CLASS_REFERENCE);
        assertTrue(objects.contains(o1));
        assertFalse(objects.contains(o2));
        assertNull(objects.get(1));

        // Second test: swap the two objects, so that the first object is in the position the second should have
        // Start over, re-adding the two objects
        doc = new XWikiDocument(DOCUMENT_REFERENCE);
        doc.addXObject(o1);
        doc.addXObject(o2);
    }

    @Test
    public void testCopyDocument() throws XWikiException
    {
        XWikiDocument doc = new XWikiDocument();
        doc.setTitle("Some title");
        BaseObject o = new BaseObject();
        o.setXClassReference(CLASS_REFERENCE);
        doc.addXObject(o);

        XWikiDocument newDoc = doc.copyDocument("newdoc", this.oldcore.getXWikiContext());
        BaseObject newO = newDoc.getXObject(CLASS_REFERENCE);

        assertNotSame(o, newDoc.getXObject(CLASS_REFERENCE));
        assertFalse(newO.getGuid().equals(o.getGuid()));
        // Verify that the title is copied
        assertEquals("Some title", newDoc.getTitle());
    }

    /**
     * @see <a href="http://jira.xwiki.org/browse/XWIKI-6743">XWIKI-6743</a>
     * @see <a href="http://jira.xwiki.org/browse/XWIKI-12349">XWIKI-12349</a>
     */
    @Test
    public void testCopyDocumentSetsTitleToNewDocNameIfPreviouslySetToDocName() throws XWikiException
    {
        copyDocumentAndAssertTitle(new DocumentReference("wiki1", "space1", "page1"), "page1",
            new DocumentReference("wiki2", "space2", "page2"), "page2");

        copyDocumentAndAssertTitle(new DocumentReference("wiki1", "space1", "WebHome"), "space1",
            new DocumentReference("wiki2", "space2", "page2"), "page2");

        copyDocumentAndAssertTitle(new DocumentReference("wiki1", "space1", "WebHome"), "space1",
            new DocumentReference("wiki2", "space2", "WebHome"), "space2");
    }

    private void copyDocumentAndAssertTitle(DocumentReference oldReference, String oldTitle,
        DocumentReference newReference, String expectedNewTitle) throws XWikiException
    {
        XWikiDocument doc = new XWikiDocument(oldReference);
        doc.setTitle(oldTitle);

        XWikiDocument newDoc = doc.copyDocument(newReference, this.oldcore.getXWikiContext());

        // Verify that we get the expected title.
        assertEquals(expectedNewTitle, newDoc.getTitle());
    }

    @Test
    public void testValidate() throws XWikiException, AccessDeniedException
    {
        this.document.setValidationScript("validationScript");
        this.baseClass.setValidationScript("validationScript");

        doReturn(new XWikiValidationInterface()
        {
            @Override
            public boolean validateObject(BaseObject object, XWikiContext context)
            {
                return true;
            }

            @Override
            public boolean validateDocument(XWikiDocument doc, XWikiContext context)
            {
                return true;
            }
        }).when(this.oldcore.getSpyXWiki()).parseGroovyFromPage("validationScript", this.oldcore.getXWikiContext());

        // With PR

        assertTrue(this.document.validate(this.oldcore.getXWikiContext()));
        assertTrue(this.baseClass.validateObject(this.baseObject, this.oldcore.getXWikiContext()));

        // Without PR

        doThrow(AccessDeniedException.class).when(this.oldcore.getMockContextualAuthorizationManager())
            .checkAccess(Right.PROGRAM, new DocumentReference("wiki", "space", "validationScript"));

        assertFalse(this.document.validate(this.oldcore.getXWikiContext()));
        assertFalse(this.baseClass.validateObject(this.baseObject, this.oldcore.getXWikiContext()));
    }
}