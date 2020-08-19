package com.company.clinic.web.screens.owner;

import com.haulmont.cuba.gui.screen.*;
import com.company.clinic.entity.Owner;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.ValidationException;

@UiController("clinic_Owner.edit")
@UiDescriptor("owner-edit.xml")
@EditedEntityContainer("ownerDc")
@LoadDataBeforeShow
public class OwnerEdit extends StandardEditor<Owner> {

    @Install(to = "nameField", subject = "validator")
    private void nameFieldValidator(String name) throws ValidationException {
        if (!StringUtils.isAlphaSpace(name)) {
            throw new ValidationException("Name must contain letters only");
        }
    }
}