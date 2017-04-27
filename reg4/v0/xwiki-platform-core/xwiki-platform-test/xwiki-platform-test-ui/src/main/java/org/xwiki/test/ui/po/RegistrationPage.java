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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the actions possible on the Registration Page
 * 
 * @version $Id: b0ba2038d9414c641b464e429ede64bb093c31b6 $
 * @since 3.2M3
 */
public class RegistrationPage extends AbstractRegistrationPage
{
    @FindBy(xpath = "//form[@id='register']/div/span/input[@type='submit']")
    private WebElement submitButton;

    /**
     * To put the registration page someplace else, subclass this class and change
     * this method.
     */
    public static RegistrationPage gotoPage()
    {
        getUtil().gotoPage("XWiki", "Register", "register");
        return new RegistrationPage();
    }

    @Override
    public void clickRegister()
    {
        this.submitButton.click();
    }
}
