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
package org.xwiki.rest.internal.representations.tags;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.xwiki.component.annotation.Component;
import org.xwiki.rest.internal.representations.TextPlainReader;
import org.xwiki.rest.model.jaxb.ObjectFactory;
import org.xwiki.rest.model.jaxb.Tag;
import org.xwiki.rest.model.jaxb.Tags;

/**
 * @version $Id: 2c182e38836832992a30565c1f5addaec4057ffa $
 */
@Component
@Named("org.xwiki.rest.internal.representations.tags.TextPlainTagsReader")
@Provider
@Consumes(MediaType.TEXT_PLAIN)
@Singleton
public class TextPlainTagsReader extends TextPlainReader<Tags>
{
    @Override
    public boolean isReadable(Class< ? > type, Type genericType, Annotation[] annotations, MediaType mediaType)
    {
        return Tags.class.isAssignableFrom(type);
    }

    @Override
    public Tags readFrom(Class<Tags> type, Type genericType, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException,
        WebApplicationException
    {
        ObjectFactory objectFactory = new ObjectFactory();
        Tags tags = objectFactory.createTags();

        String text = getEntityAsString(entityStream);
        String[] tagNames = text.split(" |,|\\||\\r?\\n");

        for (String tagName : tagNames) {
            Tag tag = objectFactory.createTag();
            tag.setName(tagName);
            tags.getTags().add(tag);
        }

        return tags;
    }

}
