package com.dpagliotto.paykids.db.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;



public class DBManager {
    private static final String TAG = DBManager.class.getName();

    private SQLiteDatabase mDb;
    private static DBManager sInstance;

    private DbHelper mOpenHelper;

    private DBManager(Context context) {
        mOpenHelper = new DbHelper(context.getApplicationContext());
        while (true) {
            try {
                setDb(mOpenHelper.getWritableDatabase());
                getDb().enableWriteAheadLogging();
                break;
            } catch (Exception e) {

            }
        }
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

    public void setDb(SQLiteDatabase db) {
        mDb = db;
    }

    public static DBManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBManager(context);
            Log.d(TAG, "Database instance created");
        }
        return sInstance;
    }

    public static class DbHelper extends OrmLiteSqliteOpenHelper {
        private static String TAG = DbHelper.class.getName();
        private Context mContext = null;

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "projeto.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
            executeSQLScript(db, "create.sql");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        }

        public void executeSQLScript(SQLiteDatabase database, String scriptName) {
            AssetManager assetManager = mContext.getAssets();
            StringBuilder sqlBuilder = new StringBuilder();
            try {
                InputStream inputStream = assetManager.open("db/" + scriptName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                int c;

                while ((c = reader.read()) != -1) {
                    sqlBuilder.append((char) c);
                    if (c == ';') {
                        if (database.inTransaction()) database.endTransaction();

                        database.beginTransaction();
                        String sqlStatement = sqlBuilder.toString().trim();
                        Log.d(TAG, "SQL: " + sqlStatement);
                        try {
                            // Executes the SQL
                            database.execSQL(sqlStatement);
                            database.setTransactionSuccessful();
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao executar a instrução: " + sqlStatement);
                        }
                        finally {
                            database.endTransaction();
                        }

                        // Allocates a new StringBuilder
                        sqlBuilder = new StringBuilder();
                    }
                }

                reader.close();
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public <T> Dao<T, Integer> getDao(Class<T> clazz) {
        Dao<T, Integer> dao = null;
        try {
            dao = mOpenHelper.getDao(clazz);
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }

    public DbHelper getOpenHelper() {
        return mOpenHelper;
    }
}