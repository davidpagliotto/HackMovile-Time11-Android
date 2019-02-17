package com.dpagliotto.gimb.db.dao;

import android.content.Context;
import android.util.Log;

import com.dpagliotto.gimb.db.helper.DBManager;
import com.dpagliotto.gimb.model.BaseModel;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by davidpagliotto on 29/07/17.
 */

public class BaseDAO {

    protected Context mContext;
    protected Dao dao;
    protected Class clazz;

    public BaseDAO(Context context, Class clazz) {
        this.mContext = context;
        this.dao = DBManager.getInstance(context).getDao(clazz);
        this.clazz = clazz;
    }

    public <T extends BaseModel> T buscaPorID(Object id) {
        try {
            return (T) dao.queryForId(id);
        } catch (SQLException e) {
            Log.e("", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public <T extends BaseModel> List<T> buscaPorAtributoEValor(String fieldValue, Object valor) {
        try {
            return dao.queryForEq(fieldValue, valor);
        } catch (SQLException e) {
            Log.e("", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public <T extends BaseModel> List<T> listarTodos() {
        try {
            return  dao.queryForAll();
        } catch (SQLException e) {
            Log.e("", e.getMessage());
        }
        return null;
    }

    public void deleteAll(ForeignCollection<? extends BaseModel> list) {
        if (list != null && list.size() > 0) {
            for (BaseModel obj : list) {
                delete(obj);
            }
        }
    }

    public void deleteAll(List<? extends BaseModel> list) {
        if (list != null && list.size() > 0) {
            for (BaseModel obj : list) {
                delete(obj);
            }
        }
    }

    public boolean deleteAll() {
        try {
            return dao.delete(dao.deleteBuilder().prepare()) > 0;
        } catch (SQLException e) {
            Log.e("", e.getMessage());
        }
        return false;
    }

    public boolean delete(Object object) {
        try {
            return dao.delete(object) == 1;
        } catch (SQLException e) {
            Log.e("", e.getMessage());
        }
        return false;
    }

    public boolean create(Object object) {
        try {
            object = setSequenceID((BaseModel) object);
            return dao.create(object) > 0;
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        return false;
    }

    public boolean createOrUpdate(Object object) {
        try {
            object = setSequenceID((BaseModel) object);
            Dao.CreateOrUpdateStatus status = dao.createOrUpdate(object);
            return status.isCreated() || status.isUpdated();
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        return false;
    }

    public boolean createAll(List<? extends BaseModel> list) {
        DBManager.getInstance(mContext).getDb().beginTransaction();

        boolean errorOnCreate = false;
        try {
            for (Object object : list) {
                if (!createOrUpdate(object)) {
                    errorOnCreate = true;
                    break;
                }
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        if (!errorOnCreate)
            DBManager.getInstance(mContext).getDb().setTransactionSuccessful();

        DBManager.getInstance(mContext).getDb().endTransaction();

        return !errorOnCreate;
    }

    public boolean executeSQL(String sql) {
        try {
            return dao.executeRawNoArgs(sql) > 0;
        } catch (SQLException e) {
            Log.e("", e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public void assignEmptyList(Object parent, String fieldName) {
        try {
            dao.assignEmptyForeignCollection(parent, fieldName);
        } catch (Exception e) {
            Log.e("", e.getMessage());
            e.printStackTrace();
        }
    }

    public <T extends BaseModel> T setSequenceID(T object) throws Exception {

        Field fieldId = null;
        String table = null;
        String columnId = null;

        DatabaseTable databaseTable = (DatabaseTable) clazz.getAnnotation(DatabaseTable.class);
        if (databaseTable != null) {
            table = databaseTable.tableName();
        }

        for (Field field: clazz.getDeclaredFields()) {
            DatabaseField databaseField = field.getAnnotation(DatabaseField.class);
            if (databaseField != null) {
                if (databaseField.id()) {
                    fieldId = field;
                    columnId = databaseField.columnName();
                    break;
                }
            }
        }

        if (table == null)
            throw new Exception("DatabaseTable annotation not found on class " + clazz.getName());

        if (columnId == null)
            throw new Exception("ID not found on class " + clazz.getName());

        fieldId.setAccessible(true);
        // if ID has value, return object
        if (fieldId.get(object) != null && !Integer.valueOf(0).equals(fieldId.get(object))) {
            return object;
        } else { // else, query sequence id
            Integer nextID = 0;
            try {
                String[] result = (String[]) dao.queryRaw(" SELECT COALESCE(MAX(" + columnId + "), 0) + 1 FROM " + table).getFirstResult();
                nextID = Integer.parseInt(result[0]);
                fieldId.set(object, nextID);
            } catch (Exception e) {
                Log.e("", e.getMessage());
            }
        }

        return object;
    }

}
