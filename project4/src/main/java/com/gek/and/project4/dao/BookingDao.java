package com.gek.and.project4.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.gek.and.project4.entity.Booking;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BOOKING.
*/
public class BookingDao extends AbstractDao<Booking, Long> {

    public static final String TABLENAME = "BOOKING";

    /**
     * Properties of entity Booking.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ProjectId = new Property(1, Long.class, "projectId", false, "PROJECT_ID");
        public final static Property From = new Property(2, java.util.Date.class, "from", false, "FROM");
        public final static Property To = new Property(3, java.util.Date.class, "to", false, "TO");
        public final static Property Minutes = new Property(4, Integer.class, "minutes", false, "MINUTES");
        public final static Property Note = new Property(5, String.class, "note", false, "NOTE");
        public final static Property BreakHours = new Property(6, Integer.class, "breakHours", false, "BREAK_HOURS");
        public final static Property BreakMinutes = new Property(7, Integer.class, "breakMinutes", false, "BREAK_MINUTES");
        public final static Property Billable = new Property(8, Boolean.class, "billable", false, "BILLABLE");
    };

    private DaoSession daoSession;

    private Query<Booking> project_BookingListQuery;

    public BookingDao(DaoConfig config) {
        super(config);
    }
    
    public BookingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BOOKING' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PROJECT_ID' INTEGER," + // 1: projectId
                "'FROM' INTEGER NOT NULL ," + // 2: from
                "'TO' INTEGER," + // 3: to
                "'MINUTES' INTEGER," + // 4: minutes
                "'NOTE' TEXT," + // 5: note
                "'BREAK_HOURS' INTEGER," + // 6: breakHours
                "'BREAK_MINUTES' INTEGER," + // 7: breakMinutes
                "'BILLABLE' INTEGER);"); // 8: billable
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BOOKING'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Booking entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long projectId = entity.getProjectId();
        if (projectId != null) {
            stmt.bindLong(2, projectId);
        }
        stmt.bindLong(3, entity.getFrom().getTime());
 
        java.util.Date to = entity.getTo();
        if (to != null) {
            stmt.bindLong(4, to.getTime());
        }
 
        Integer minutes = entity.getMinutes();
        if (minutes != null) {
            stmt.bindLong(5, minutes);
        }
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(6, note);
        }
 
        Integer breakHours = entity.getBreakHours();
        if (breakHours != null) {
            stmt.bindLong(7, breakHours);
        }
 
        Integer breakMinutes = entity.getBreakMinutes();
        if (breakMinutes != null) {
            stmt.bindLong(8, breakMinutes);
        }
 
        Boolean billable = entity.getBillable();
        if (billable != null) {
            stmt.bindLong(9, billable ? 1l: 0l);
        }
    }

    @Override
    protected void attachEntity(Booking entity) {
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
    public Booking readEntity(Cursor cursor, int offset) {
        Booking entity = new Booking( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // projectId
            new java.util.Date(cursor.getLong(offset + 2)), // from
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // to
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // minutes
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // note
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // breakHours
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // breakMinutes
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0 // billable
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Booking entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setProjectId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setFrom(new java.util.Date(cursor.getLong(offset + 2)));
        entity.setTo(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setMinutes(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setNote(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBreakHours(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setBreakMinutes(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setBillable(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Booking entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Booking entity) {
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
    
    /** Internal query to resolve the "bookingList" to-many relationship of Project. */
    public List<Booking> _queryProject_BookingList(Long projectId) {
        synchronized (this) {
            if (project_BookingListQuery == null) {
                QueryBuilder<Booking> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ProjectId.eq(null));
                project_BookingListQuery = queryBuilder.build();
            }
        }
        Query<Booking> query = project_BookingListQuery.forCurrentThread();
        query.setParameter(0, projectId);
        return query.list();
    }

}
