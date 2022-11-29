package ru.vk;

import generatedEntities.tables.records.CompanyRecord;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static generatedEntities.tables.Company.COMPANY;

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
