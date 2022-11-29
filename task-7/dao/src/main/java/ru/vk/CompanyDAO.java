package ru.vk;

import generatedEntities.tables.Company;
import generatedEntities.tables.records.CompanyRecord;
import org.jetbrains.annotations.NotNull;
import static generatedEntities.tables.Company.COMPANY;

import java.sql.Connection;

public class CompanyDAO extends AbstractDAO<CompanyRecord, Integer> {
    public CompanyDAO(@NotNull Connection connection) {
        super(connection, COMPANY, COMPANY.ID);
    }

    public CompanyRecord getByName(String name) {
        CompanyRecord companyRecord = getContext().selectFrom(getTableName()).where(COMPANY.NAME.eq(name)).fetchOne();
        if (companyRecord == null) {
            throw new IllegalStateException(getTableName().getName() + "with name " + getTableName().getPrimaryKey().getName() + " equal to " + name + " not found");
        }
        return companyRecord;
    }
}
