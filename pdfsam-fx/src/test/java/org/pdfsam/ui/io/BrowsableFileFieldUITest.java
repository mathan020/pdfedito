/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 16/lug/2014
 * Copyright 2013-2014 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.ui.io;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.sejda.eventstudio.StaticStudio.eventStudio;

import java.util.Arrays;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import org.pdfsam.support.io.FileType;
import org.pdfsam.ui.support.FXValidationSupport.ValidationState;
import org.pdfsam.ui.support.Style;

/**
 * @author Andrea Vacondio
 *
 */
@Category(TestFX.class)
public class BrowsableFileFieldUITest extends GuiTest {
    @Override
    protected Parent getRootNode() {
        BrowsableFileField victimBlank = new BrowsableFileField(FileType.PDF);
        victimBlank.enforceValidation(false, true);
        victimBlank.getStyleClass().add("victim-blank");

        BrowsableFileField victimNoBlank = new BrowsableFileField(FileType.PDF);
        victimNoBlank.enforceValidation(false, false);
        victimNoBlank.getStyleClass().add("victim-no-blank");
        return new VBox(victimBlank, victimNoBlank);
    }

    @After
    public void tearDown() {
        eventStudio().clear();
    }

    @Test
    public void validBlank() {
        BrowsableFileField victim = find(".victim-blank");
        click(victim);
        type(KeyCode.TAB);
        verifyThat(victim, v -> v.getTextField().getValidationState() == ValidationState.VALID);
    }

    @Test
    public void invalidBlank() {
        BrowsableFileField victim = find(".victim-no-blank");
        click(victim);
        type(KeyCode.TAB);
        verifyThat(victim, v -> v.getTextField().getValidationState() == ValidationState.INVALID);
        Arrays.stream(Style.INVALID.css()).forEach(c -> verifyThat(victim, v -> exists("." + c)));
    }
}
