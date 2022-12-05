package ru.vk;

import com.google.inject.Inject;
import generatedEntities.tables.pojos.Company;
import generatedEntities.tables.records.CompanyRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;



import static generatedEntities.tables.Company.COMPANY;

public class CompanyDAO  {
    private final DSLContext context;

    @Inject
    public CompanyDAO(DSLContext context) {
        this.context = context;
    }

    public Company get(@NotNull Integer pk) {
        CompanyRecord record = context.selectFrom(COMPANY).where(COMPANY.ID.eq(pk)).fetchOne();
        if (record == null) {
            throw new IllegalStateException("Company with primary key equal to " + pk + " not found");
        }
        return record.into(Company.class);
    }


    public void create(Company company) {
        if (context.executeInsert(context.newRecord(COMPANY, company)) == 0) {
            throw new IllegalStateException("Error during insertion");
        }
    }

    public Company getByName(String name) {
        CompanyRecord companyRecord = context.selectFrom(COMPANY).where(COMPANY.NAME.eq(name)).fetchOne();
        if (companyRecord == null) {
            throw new IllegalStateException("Company with name equal to " + name + " not found");
        }
        return companyRecord.into(Company.class);
    }
}
