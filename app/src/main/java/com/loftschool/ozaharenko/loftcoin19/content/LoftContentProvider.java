package com.loftschool.ozaharenko.loftcoin19.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//CRUD
public class LoftContentProvider extends ContentProvider {
    //allows to share data between applications;
    //can be used to integrate with Android Assistant search
    //can be used if you need to create Android device account (like a Google account)
    //can be used if you don't want to create an application, but a library.  1:57:00 lecture 9 time.

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    //uri format examples:
    // content://[table name]/[id]
    // content://contacts
    // content://contacts/1
    // content://contacts/2

    //projection parameter = fields name. If null:
    //SELECT *
    //If not null:
    //SELECT a, b, c

    //selection parameter = WHERE conditions, and others:
    //SELECT projection WHERE selection(selectionArgs) ORDER BY sortOrder
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    //ContentValues parameter - just map what describes to what value we will insert what column
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
