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
package org.xwiki.linkchecker.test.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.xwiki.index.test.po.AllDocsPage;
import org.xwiki.test.ui.po.LiveTableElement;

/**
 * Represents the actions possible on the AllDocs Page when the LinkChecker feature is enabled (it contributes a new
 * tab to the AllDocs page).
 *
 * @version $Id: 413176ee40e11f03ecf5ea8538ca8c6298b03208 $
 * @since 3.5M1
 */
public class LinkCheckerAllDocsPage extends AllDocsPage
{
    public static LinkCheckerAllDocsPage gotoPage()
    {
        getUtil().gotoPage("Main", "AllDocs");
        return new LinkCheckerAllDocsPage();
    }

    public LiveTableElement clickLinkCheckerTab()
    {
        WebElement linkCheckerTab = getDriver().findElement(By.xpath("//li[@id='xwikiexternalLinks']/a"));
        linkCheckerTab.click();

        LiveTableElement lt = new LiveTableElement("links");
        lt.waitUntilReady();

        return lt;
    }
}
