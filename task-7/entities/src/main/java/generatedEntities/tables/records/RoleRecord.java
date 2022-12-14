/*
 * This file is generated by jOOQ.
 */
package generatedEntities.tables.records;


import generatedEntities.tables.Role;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RoleRecord extends UpdatableRecordImpl<RoleRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.role.id</code>.
     */
    public RoleRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.role.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.role.name</code>.
     */
    public RoleRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.role.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Role.ROLE.ID;
    }

    @Override
    public Field<String> field2() {
        return Role.ROLE.NAME;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public RoleRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public RoleRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public RoleRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RoleRecord
     */
    public RoleRecord() {
        super(Role.ROLE);
    }

    /**
     * Create a detached, initialised RoleRecord
     */
    public RoleRecord(Integer id, String name) {
        super(Role.ROLE);

        setId(id);
        setName(name);
    }

    /**
     * Create a detached, initialised RoleRecord
     */
    public RoleRecord(generatedEntities.tables.pojos.Role value) {
        super(Role.ROLE);

        if (value != null) {
            setId(value.getId());
            setName(value.getName());
        }
    }
}
