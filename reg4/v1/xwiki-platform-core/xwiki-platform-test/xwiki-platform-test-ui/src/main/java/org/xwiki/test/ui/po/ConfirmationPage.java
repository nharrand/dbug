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
package org.xwiki.test.ui.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents a confirmation page that allows the user cancel or confirm the current action (e.g. delete a page, delete
 * a space, etc.).
 * 
 * @version $Id: abc19291ccb67b57803ae2cdb6d57fa73ab13764 $
 * @since 4.0M2
 */
public class ConfirmationPage extends ViewPage
{
    @FindBy(xpath = "//*[(contains(@class, 'btn') or contains(@class, 'button')) "
            + "and (contains(@value, 'Yes') or contains(text(), 'Yes'))]")
    private WebElement yesButton;

    @FindBy(xpath = "//*[(contains(@class, 'btn') or contains(@class, 'button')) "
            + "and (contains(@value, 'No') or contains(text(), 'No'))]")
    private WebElement noButton;

    @FindBy(id = "affectChildren")
    private WebElement affectChildren;

    /**
     * Clicks on the Yes button to confirm the current action.
     */
    public void clickYes()
    {
        this.yesButton.click();
    }

    /**
     * Clicks on the No button to cancel the current action.
     */
    public void clickNo()
    {
        this.noButton.click();
    }

    /**
     * @return if the "affect children" option is present or not
     * @since 7.2RC1 
     */
    public boolean hasAffectChildrenOption()
    {
        return getDriver().hasElementWithoutWaiting(By.id("affectChildren"));
    }

    /**
     * Check or un-check the "affect children" option.
     * @param value either or the option should be enabled 
     * @since 7.2RC1
     */
    public void setAffectChildren(boolean value)
    {
        if (affectChildren.isSelected() != value) {
            affectChildren.click();
        }
    }

    /**
     * Confirm the deletion of the page
     * @return an object representing the UI displayed when a page is deleted
     */
    public DeletingPage confirmDeletePage()
    {
        clickYes();
        return new DeletingPage();
    }
}