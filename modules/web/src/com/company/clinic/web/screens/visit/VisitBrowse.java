package com.company.clinic.web.screens.visit;

import com.haulmont.cuba.gui.screen.*;
import com.company.clinic.entity.Visit;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;

@UiController("clinic_Visit.browse")
@UiDescriptor("visit-browse.xml")
@LookupComponent("visitsTable")
@LoadDataBeforeShow
public class VisitBrowse extends StandardLookup<Visit> {

    @Inject
    private UserSession userSession;

    @Install(to = "visitsTable.excel", subject = "enabledRule")
    private boolean visitsTableExcelEnabledRule() {
        return userSession.getUser().getLogin().equals("admin");
    }

}