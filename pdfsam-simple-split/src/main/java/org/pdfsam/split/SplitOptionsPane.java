/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 08/apr/2014
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
package org.pdfsam.split;

import java.util.function.Consumer;

import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.pdfsam.context.DefaultI18nContext;
import org.pdfsam.ui.commons.RadioButtonDrivenTextFieldsPane;
import org.pdfsam.ui.commons.ValidableTextField;
import org.pdfsam.ui.support.Style;
import org.sejda.model.parameter.AbstractSplitByPageParameters;
import org.sejda.model.parameter.SimpleSplitParameters;
import org.sejda.model.parameter.SplitByPagesParameters;
import org.sejda.model.pdf.page.PredefinedSetOfPages;

/**
 * Panel for the Split options
 * 
 * @author Andrea Vacondio
 *
 */
class SplitOptionsPane extends VBox {

    private PredefinedSetOfPagesRadioButton burst = new PredefinedSetOfPagesRadioButton(PredefinedSetOfPages.ALL_PAGES,
            DefaultI18nContext.getInstance().i18n("Burst (Split into single pages)"));
    private PredefinedSetOfPagesRadioButton even = new PredefinedSetOfPagesRadioButton(PredefinedSetOfPages.EVEN_PAGES,
            DefaultI18nContext.getInstance().i18n("Split even pages"));
    private PredefinedSetOfPagesRadioButton odd = new PredefinedSetOfPagesRadioButton(PredefinedSetOfPages.ODD_PAGES,
            DefaultI18nContext.getInstance().i18n("Split odd pages"));

    private ToggleGroup group = new ToggleGroup();
    private SplitAtRadioButtonDrivenTextFieldController splitAtController;

    SplitOptionsPane() {
        super(5);
        RadioButtonDrivenTextFieldsPane grid = new RadioButtonDrivenTextFieldsPane(group);
        burst.setToggleGroup(group);
        burst.setTooltip(new Tooltip(DefaultI18nContext.getInstance().i18n("Explode the document into single pages")));
        burst.setSelected(true);
        even.setToggleGroup(group);
        even.setTooltip(new Tooltip(DefaultI18nContext.getInstance().i18n("Split the document at every even page")));
        odd.setToggleGroup(group);
        odd.setTooltip(new Tooltip(DefaultI18nContext.getInstance().i18n("Split the document at every odd page")));
        ValidableTextField splitAt = new ValidableTextField();
        splitAt.setOnEnterValidation(true);
        splitAt.setEnableInvalidStyle(true);
        splitAt.setPromptText(DefaultI18nContext.getInstance().i18n("Page numbers to split at (n1,n2,n3..)"));
        RadioButton splitAtRadio = new RadioButton(DefaultI18nContext.getInstance()
                .i18n("Split at the following pages"));
        splitAtRadio.setTooltip(new Tooltip(DefaultI18nContext.getInstance().i18n(
                "Split the document after the given page numbers")));
        grid.addRow(splitAtRadio, splitAt);
        splitAtController = new SplitAtRadioButtonDrivenTextFieldController(splitAtRadio, splitAt);

        HBox simpleSplit = new HBox(20, burst, even, odd);
        simpleSplit.getStyleClass().addAll(Style.VITEM.css());
        getStyleClass().addAll(Style.CONTAINER.css());
        getChildren().addAll(simpleSplit, grid);
    }

    AbstractSplitByPageParameters createParams(Consumer<String> onError) {
        Toggle toggle = group.getSelectedToggle();
        if (toggle instanceof PredefinedSetOfPagesRadioButton) {
            return new SimpleSplitParameters(((PredefinedSetOfPagesRadioButton) toggle).getPages());
        } else if (splitAtController.isSelected()) {
            SplitByPagesParameters retVal = new SplitByPagesParameters();
            splitAtController.apply(retVal, onError);
            return retVal;
        }
        onError.accept(DefaultI18nContext.getInstance().i18n("Unable to create split parameters"));
        return null;
    }

}
