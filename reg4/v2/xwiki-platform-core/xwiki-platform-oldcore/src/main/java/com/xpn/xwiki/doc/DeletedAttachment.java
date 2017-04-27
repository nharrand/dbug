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

import java.util.Date;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.util.AbstractSimpleClass;

/**
 * Archive of deleted attachment, stored in {@link com.xpn.xwiki.store.AttachmentRecycleBinStore}. Immutable, because
 * deleted attachments should not be modified.
 *
 * @version $Id: 615631705fae21d75c76ce9fcf86442f6aa538fd $
 * @since 1.4M1
 */
public class DeletedAttachment extends AbstractSimpleClass
{
    /** Synthetic id, generated by Hibernate. This is used to address entries in the recycle bin. */
    private long id;

    /** The ID of the document this attachment belonged to. */
    private long docId;

    /** The name of the document this attachment belonged to. */
    private String docName;

    /** The name of the attachment. */
    private String filename;

    /** Date of delete action. */
    private Date date;

    /** The user who deleted the attachment, in the <tt>XWiki.UserName</tt> format. */
    private String deleter;

    /**
     * XML export of the full attachment, with content and history.
     *
     * @see XWikiAttachment#toXML(boolean, boolean, XWikiContext)
     */
    private String xml;

    /** Default constructor. Used only by hibernate when restoring objects from the database. */
    protected DeletedAttachment()
    {
    }

    /**
     * A constructor with all the information about the deleted attachment.
     *
     * @param attachment Deleted attachment.
     * @param deleter User which deleted the attachment.
     * @param deleteDate Date of delete action.
     * @param context The current context. Used for determining the encoding.
     * @throws XWikiException If the attachment cannot be exported to XML.
     */
    public DeletedAttachment(XWikiAttachment attachment, String deleter, Date deleteDate, XWikiContext context)
        throws XWikiException
    {
        this.docId = attachment.getDocId();
        this.docName = attachment.getDoc().getFullName();
        this.filename = attachment.getFilename();
        this.deleter = deleter;
        this.date = deleteDate;
        setAttachment(attachment, context);
    }

    /**
     * Getter for {@link #id}.
     *
     * @return The synthetic id of this deleted attachment. Uniquely identifies an entry in the recycle bin.
     */
    public long getId()
    {
        return this.id;
    }

    /**
     * Setter for {@link #id}.
     *
     * @param id The synthetic id to set. Used only by hibernate.
     */
    protected void setId(long id)
    {
        this.id = id;
    }

    /**
     * Getter for {@link #docId}.
     *
     * @return The id of the document this attachment belonged to.
     */
    public long getDocId()
    {
        return this.docId;
    }

    /**
     * Setter for {@link #docId}.
     *
     * @param docId The id of the document to set. Used only by hibernate.
     */
    protected void setDocId(long docId)
    {
        this.docId = docId;
    }

    /**
     * Getter for {@link #docName}.
     *
     * @return The name of the document this attachment belonged to.
     */
    public String getDocName()
    {
        return this.docName;
    }

    /**
     * Setter for {@link #docName}.
     *
     * @param docName The document name to set. Used only by hibernate.
     */
    protected void setDocName(String docName)
    {
        this.docName = docName;
    }

    /**
     * Getter for {@link #filename}.
     *
     * @return The name of the attachment.
     */
    public String getFilename()
    {
        return this.filename;
    }

    /**
     * Setter for {@link #filename}.
     *
     * @param filename The attachment filename to set. Used only by hibernate.
     */
    protected void setFilename(String filename)
    {
        this.filename = filename;
    }

    /**
     * Getter for {@link #date}.
     *
     * @return The date of the delete action.
     */
    public Date getDate()
    {
        return this.date;
    }

    /**
     * Setter for {@link #date}.
     *
     * @param date The date of the delete action to set. Used only by Hibernate.
     */
    protected void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Getter for {@link #deleter}.
     *
     * @return the user who deleted the attachment, as its document name (e.g. {@code XWiki.Admin})
     */
    public String getDeleter()
    {
        return this.deleter;
    }

    /**
     * Setter for {@link #deleter}.
     *
     * @param deleter The user which has removed the document to set. Used only by Hibernate.
     */
    protected void setDeleter(String deleter)
    {
        this.deleter = deleter;
    }

    /**
     * Getter for {@link #xml}.
     *
     * @return XML serialization of {@link XWikiAttachment}
     */
    public String getXml()
    {
        return this.xml;
    }

    /**
     * Setter for {@link #xml}.
     *
     * @param xml XML serialization of {@link XWikiAttachment}. Used only by Hibernate.
     */
    protected void setXml(String xml)
    {
        this.xml = xml;
    }

    /**
     * Export {@link XWikiAttachment} to {@link DeletedAttachment}.
     *
     * @param attachment the deleted attachment
     * @param context the current context, used in the XML export
     * @throws XWikiException if an exception occurs during the XML export
     */
    protected void setAttachment(XWikiAttachment attachment, XWikiContext context) throws XWikiException
    {
        setXml(attachment.toStringXML(true, true, context));
    }

    /**
     * Restore a {@link XWikiAttachment} from a {@link DeletedAttachment}. Note that this method does not actually
     * restore the attachment to its owner document, it simply recomposes an {@link XWikiAttachment} object from the
     * saved data.
     *
     * @return restored attachment
     * @param attachment optional object where to put the attachment data, if not <code>null</code>
     * @param context the current {@link XWikiContext context}
     * @throws XWikiException If an exception occurs while the Attachment is restored from the XML. See
     *             {@link XWikiAttachment#fromXML(String)}.
     */
    public XWikiAttachment restoreAttachment(XWikiAttachment attachment, XWikiContext context) throws XWikiException
    {
        XWikiAttachment result = attachment;
        if (result == null) {
            result = new XWikiAttachment();
        }
        result.fromXML(getXml());
        if (result.getDoc() == null || !(this.getDocName().equals(result.getDoc().getFullName()))) {
            result.setDoc(context.getWiki().getDocument(this.getDocName(), context));
        }
        return result;
    }
}
