package com.company.clinic.web.screens.owner;

import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.screen.*;
import com.company.clinic.entity.Owner;

import javax.inject.Inject;

@UiController("clinic_Owner.browse")
@UiDescriptor("owner-browse.xml")
@LookupComponent("ownersTable")
@LoadDataBeforeShow
public class OwnerBrowse extends StandardLookup<Owner> {

    @Inject
    private GroupTable<Owner> ownersTable;

    @Inject
    private Notifications notifications;

    @Subscribe("ownersTable.greet")
    public void onOwnersTableGreet(Action.ActionPerformedEvent event) {

        Owner selectedOwner = ownersTable.getSingleSelected();

        if (selectedOwner != null) {
            notifications.create().withCaption("Hello "+selectedOwner.getName()).show();
        }

    }
}