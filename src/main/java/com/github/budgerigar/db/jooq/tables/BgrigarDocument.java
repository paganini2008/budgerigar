/*
 * This file is generated by jOOQ.
 */
package com.github.budgerigar.db.jooq.tables;


import com.github.budgerigar.db.jooq.Keys;
import com.github.budgerigar.db.jooq.Public;
import com.github.budgerigar.db.jooq.tables.records.BgrigarDocumentRecord;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function7;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class BgrigarDocument extends TableImpl<BgrigarDocumentRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>PUBLIC.BGRIGAR_DOCUMENT</code>
     */
    public static final BgrigarDocument BGRIGAR_DOCUMENT = new BgrigarDocument();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<BgrigarDocumentRecord> getRecordType() {
        return BgrigarDocumentRecord.class;
    }

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.ID</code>.
     */
    public final TableField<BgrigarDocumentRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.NAME</code>.
     */
    public final TableField<BgrigarDocumentRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.TITLE</code>.
     */
    public final TableField<BgrigarDocumentRecord, String> TITLE = createField(DSL.name("TITLE"), SQLDataType.VARCHAR(1024), this, "");

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.EXTENTION</code>.
     */
    public final TableField<BgrigarDocumentRecord, String> EXTENTION = createField(DSL.name("EXTENTION"), SQLDataType.VARCHAR(32), this, "");

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.PATH</code>.
     */
    public final TableField<BgrigarDocumentRecord, String> PATH = createField(DSL.name("PATH"), SQLDataType.VARCHAR(2000).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.LAST_MODIFIED</code>.
     */
    public final TableField<BgrigarDocumentRecord, LocalDateTime> LAST_MODIFIED = createField(DSL.name("LAST_MODIFIED"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>PUBLIC.BGRIGAR_DOCUMENT.CONTENT</code>.
     */
    public final TableField<BgrigarDocumentRecord, String> CONTENT = createField(DSL.name("CONTENT"), SQLDataType.CLOB, this, "");

    private BgrigarDocument(Name alias, Table<BgrigarDocumentRecord> aliased) {
        this(alias, aliased, null);
    }

    private BgrigarDocument(Name alias, Table<BgrigarDocumentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>PUBLIC.BGRIGAR_DOCUMENT</code> table reference
     */
    public BgrigarDocument(String alias) {
        this(DSL.name(alias), BGRIGAR_DOCUMENT);
    }

    /**
     * Create an aliased <code>PUBLIC.BGRIGAR_DOCUMENT</code> table reference
     */
    public BgrigarDocument(Name alias) {
        this(alias, BGRIGAR_DOCUMENT);
    }

    /**
     * Create a <code>PUBLIC.BGRIGAR_DOCUMENT</code> table reference
     */
    public BgrigarDocument() {
        this(DSL.name("BGRIGAR_DOCUMENT"), null);
    }

    public <O extends Record> BgrigarDocument(Table<O> child, ForeignKey<O, BgrigarDocumentRecord> key) {
        super(child, key, BGRIGAR_DOCUMENT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<BgrigarDocumentRecord, Integer> getIdentity() {
        return (Identity<BgrigarDocumentRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<BgrigarDocumentRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_7;
    }

    @Override
    public BgrigarDocument as(String alias) {
        return new BgrigarDocument(DSL.name(alias), this);
    }

    @Override
    public BgrigarDocument as(Name alias) {
        return new BgrigarDocument(alias, this);
    }

    @Override
    public BgrigarDocument as(Table<?> alias) {
        return new BgrigarDocument(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public BgrigarDocument rename(String name) {
        return new BgrigarDocument(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public BgrigarDocument rename(Name name) {
        return new BgrigarDocument(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public BgrigarDocument rename(Table<?> name) {
        return new BgrigarDocument(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Integer, String, String, String, String, LocalDateTime, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function7<? super Integer, ? super String, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function7<? super Integer, ? super String, ? super String, ? super String, ? super String, ? super LocalDateTime, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
