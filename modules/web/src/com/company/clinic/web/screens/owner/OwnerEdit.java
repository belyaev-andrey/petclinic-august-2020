package com.company.clinic.web.screens.owner;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.screen.*;
import com.company.clinic.entity.Owner;
import com.haulmont.reports.gui.actions.EditorPrintFormAction;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.xml.bind.ValidationException;

@UiController("clinic_Owner.edit")
@UiDescriptor("owner-edit.xml")
@EditedEntityContainer("ownerDc")
@LoadDataBeforeShow
public class OwnerEdit extends StandardEditor<Owner> {

    @Inject
    private Button ownerReportBtn;

    @Subscribe
    public void onInit(InitEvent event) {
        ownerReportBtn.setAction(new EditorPrintFormAction(this, null));
    }

    @Install(to = "nameField", subject = "validator")
    private void nameFieldValidator(String name) throws ValidationException {
        if (!StringUtils.isAlphaSpace(name)) {
            throw new ValidationException("Name must contain letters only");
        }
    }
}