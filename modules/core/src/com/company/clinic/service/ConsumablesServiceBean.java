package com.company.clinic.service;

import com.company.clinic.entity.Consumable;
import com.haulmont.addon.bproc.service.BprocRuntimeService;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.reports.ReportingApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service(ConsumablesService.NAME)
public class ConsumablesServiceBean implements ConsumablesService {

    @Inject
    private Persistence persistence;

    @Inject
    private DataManager dataManager;
    @Inject
    private ReportingApi reportingApi;
    @Inject
    private EmailerAPI emailerAPI;
    @Inject
    private FileStorageAPI fileStorageAPI;

    @Inject
    private BprocRuntimeService bprocRuntimeService;

    public void sendReport() {
        FileDescriptor andSaveReport = reportingApi.createAndSaveReport(null, null, null);
        try {
            byte[] bytes = fileStorageAPI.loadFile(andSaveReport);
            EmailInfo emailInfo = new EmailInfoBuilder()
                    .setAddresses("a@a.a")
                    .addAttachment(new EmailAttachment(bytes, "emailInfo")).build();
            emailerAPI.sendEmail(emailInfo);
        } catch (EmailException | FileStorageException e) {
            e.printStackTrace();
        }

    }

/*
    @Override
    public List<Consumable> getUsedConsumables() {

        LoadContext<Consumable> loadContext = new LoadContext<>(Consumable.class).setQuery(
                new LoadContext.Query("select distinct c from clinic_Visit v join v.consumables c" +
                        " where @between(c.createTs, now-7, now+1, day)")).setView(View.LOCAL);

        return dataManager.secure().loadList(loadContext);

    }
*/

    public List<Consumable> getUsedConsumablesNonTrans() {
        return getUsedConsumables();
    }

    @Override
    @Transactional
    public List<Consumable> getUsedConsumables() {
        EntityManager em = persistence.getEntityManager();

        return em.createQuery("select distinct c from clinic_Visit v join v.consumables c" +
                " where @between(c.createTs, now-7, now+1, day)", Consumable.class)
                .addViewName(View.LOCAL)
                .getResultList();
    }

    /*
    @Override
    public List<Consumable> getUsedConsumables() {
        return persistence.createTransaction().execute(em -> {
            return em.createQuery("select distinct c from clinic_Visit v join v.consumables c" +
                    " where @between(c.createTs, now-7, now+1, day)", Consumable.class)
                    .addViewName(View.LOCAL).getResultList();
        });
    }
   */


    /*
    @Override
    public List<Consumable> getUsedConsumables() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            TypedQuery<Consumable> query =
                    em.createQuery("select distinct c from clinic_Visit v join v.consumables c" +
                            " where @between(c.createTs, now-7, now+1, day)", Consumable.class)
                            .addViewName(View.LOCAL);

            List<Consumable> consumables = query.getResultList();

            tx.commit();

            return consumables;
        }
    }
   */

}