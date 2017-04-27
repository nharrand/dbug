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
package org.xwiki.extension.script;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.extension.repository.ExtensionRepository;
import org.xwiki.extension.repository.internal.recommended.RecommendedExtensionRepository;

/**
 * Various script APIs related to recommended extensions.
 * 
 * @version $Id: 2a220b81f245c9411b96040a359e0c20acf926cf $
 * @since 8.3RC1
 */
@Component
@Named(ExtensionManagerScriptService.ROLEHINT + '.' + RecommendedExtensionRepository.ID)
@Singleton
public class RecommendedExtensionScriptService extends AbstractExtensionScriptService
{
    /**
     * The repository containing installed extensions.
     */
    @Inject
    @Named(RecommendedExtensionRepository.ID)
    private ExtensionRepository recommendedExtensionRepository;

    /**
     * @return the installed extensions repository
     */
    public ExtensionRepository getRepository()
    {
        return safe(this.recommendedExtensionRepository);
    }
}