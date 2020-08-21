package com.company.clinic.web.screens.usedconsumables;

import com.company.clinic.entity.Consumable;
import com.company.clinic.service.ConsumablesService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.reports.app.service.ReportService;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.gui.ReportGuiManager;
import com.haulmont.yarg.reporting.ReportingAPI;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@UiController("clinic_UsedConsumables")
@UiDescriptor("used-consumables.xml")
@DialogMode(width = "800px", height = "600px", forceDialog = true)
public class UsedConsumables extends Screen {

    @Inject
    private ConsumablesService consumablesService;

    @Inject
    private CollectionLoader<Consumable> consumablesDl;

    @Inject
    private CollectionContainer<Consumable> consumablesDc;

    @Inject
    private DataContext dataContext;

    @Inject
    private Logger log;
    @Inject
    private ReportGuiManager reportGuiManager;
    @Inject
    private DataManager dataManager;

    @Install(to = "consumablesDl", target = Target.DATA_LOADER)
    private List<Consumable> consumablesDlLoadDelegate(LoadContext<Consumable> loadContext) {
        return consumablesService.getUsedConsumables();
    }

    @Subscribe("refreshAction")
    public void onRefreshAction(Action.ActionPerformedEvent event) {
        consumablesDl.load();
    }

    @Subscribe("addFakeConsumable")
    public void onAddFakeConsumable(Action.ActionPerformedEvent event) {

        Consumable c = dataContext.create(Consumable.class);

        UUID id = UUID.randomUUID();
        c.setDescription("fake consumable "+id);
        c.setPrice(BigDecimal.ZERO);
        c.setTitle("Fake Consumable");

        consumablesDc.replaceItem(c);
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Entity> commitDelegate(CommitContext commitContext) {

        Set<Entity> savedConsumables = new HashSet<>();

        commitContext.getCommitInstances().forEach(e -> {
            log.info(e.toString());
            Consumable co = (Consumable)e;
            co.setTitle("Saved Fake Consumable");
            savedConsumables.add(co);
        });

        return savedConsumables;
    }

    @Subscribe("saveData")
    public void onSaveData(Action.ActionPerformedEvent event) {
        dataContext.commit();
    }

    @Subscribe("runPriceListReport")
    public void onRunPriceListReport(Action.ActionPerformedEvent event) {
        Report report = dataManager.load(Report.class).list()
                .stream().filter(r -> "prices".equals(r.getCode()))
                .findFirst().orElseThrow(RuntimeException::new);
        reportGuiManager.runReport(report, this);
    }



}