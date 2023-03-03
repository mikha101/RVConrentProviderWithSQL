package ru.synergy.rvconrentproviderwithsql.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.synergy.rvconrentproviderwithsql.tablemoc.CustomSqliteOpenHelper;
import ru.synergy.rvconrentproviderwithsql.tablemoc.TableItems;

public class RaquestProvider extends ContentProvider {
    public static final String TAG = "RaquestProvider";
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private static final UriMatcher sUriMatcher;

    public static final String AUTURITY = BuildConfig.APPLICATION_ID + ".db";

    public static final int TABLE_ITEMS = 0;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTURITY, TableItems.NAME + "/offset/" + "#", TABLE_ITEMS);
    }

    public static Uri urlForItems(int limit){
        return Uri.parse("content://" + AUTURITY + "/" + TableItems.NAME + "/offset/" + limit);
    }

    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = new CustomSqliteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mSQLiteOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        Cursor c = null;
        String offset = null;

        switch (sUriMatcher.match(uri)){
            case TABLE_ITEMS:
                sqb.setTables(TableItems.NAME);
                offset = uri.getLastPathSegment();
                break;
            default:
                break;
        }

        int intOffset = Integer.parseInt(offset);
        String limitArg = intOffset + "," + 30;
        Log.d(TAG, "query: " + limitArg);
        c = sqb.query(db, projection, selection, null, null, sortOrder, limitArg);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return BuildConfig.APPLICATION_ID + "item";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = "";
        switch (sUriMatcher.match(uri)){
            case TABLE_ITEMS:{
                table = TableItems.NAME;
                break;
            }
        }
        Long result = mSQLiteOpenHelper.getReadableDatabase().insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if(result == -1){
            throw  new SQLException("insert with conflict");
        }

        Uri retUri = ContentUris.withAppendedId(uri, result);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return -1;
    }
}
