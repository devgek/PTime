package com.gek.and.project4.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.gek.and.project4.entity.Project;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PROJECT.
*/
public class ProjectDao extends AbstractDao<Project, Long> {

    public static final String TABLENAME = "PROJECT";

    /**
     * Properties of entity Project.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property SubTitle = new Property(2, String.class, "subTitle", false, "SUB_TITLE");
        public final static Property Company = new Property(3, String.class, "company", false, "COMPANY");
        public final static Property Color = new Property(4, String.class, "color", false, "COLOR");
        public final static Property Priority = new Property(5, Integer.class, "priority", false, "PRIORITY");
        public final static Property Active = new Property(6, Boolean.class, "active", false, "ACTIVE");
    };

    private DaoSession daoSession;


    public ProjectDao(DaoConfig config) {
        super(config);
    }
    
    public ProjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PROJECT' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'TITLE' TEXT NOT NULL ," + // 1: title
                "'SUB_TITLE' TEXT," + // 2: subTitle
                "'COMPANY' TEXT NOT NULL ," + // 3: company
                "'COLOR' TEXT NOT NULL ," + // 4: color
                "'PRIORITY' INTEGER," + // 5: priority
                "'ACTIVE' INTEGER);"); // 6: active
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PROJECT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Project entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
 
        String subTitle = entity.getSubTitle();
        if (subTitle != null) {
            stmt.bindString(3, subTitle);
        }
        stmt.bindString(4, entity.getCompany());
        stmt.bindString(5, entity.getColor());
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(6, priority);
        }
 
        Boolean active = entity.getActive();
        if (active != null) {
            stmt.bindLong(7, active ? 1l: 0l);
        }
    }

    @Override
    protected void attachEntity(Project entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Project readEntity(Cursor cursor, int offset) {
        Project entity = new Project( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // subTitle
            cursor.getString(offset + 3), // company
            cursor.getString(offset + 4), // color
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // priority
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0 // active
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Project entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setSubTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCompany(cursor.getString(offset + 3));
        entity.setColor(cursor.getString(offset + 4));
        entity.setPriority(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setActive(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Project entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Project entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
