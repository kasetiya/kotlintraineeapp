package com.softices.kotlintraineeapp.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

@SuppressLint("SdCardPath")
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DATABASE_VERSION) {
    companion object {
        val DB_NAME = "TraineeApp"
        private val DATABASE_VERSION = 1

        //-------------table User
        val TABLE_USER_TABLE = "user_table"
        val TABLE_USER_COLUMN_ID = "user_id"
        val TABLE_USER_COLUMN_FIRSTNAME = "user_first_name"
        val TABLE_USER_COLUMN_LASTNAME = "user_last_name"
        val TABLE_USER_COLUMN_EMAIL = "user_email"
        val TABLE_USER_COLUMN_PASSWORD = "user_password"
        val TABLE_USER_COLUMN_MOBILE = "user_mobile"
        val TABLE_USER_COLUMN_USER_PHOTO = "user_photo"
    }

    private val USER_TABLE = ("CREATE TABLE "
            + TABLE_USER_TABLE + "("
            + TABLE_USER_COLUMN_ID + " INTEGER PRIMARY KEY,"
            + TABLE_USER_COLUMN_EMAIL + " TEXT,"
            + TABLE_USER_COLUMN_PASSWORD + " TEXT,"
            + TABLE_USER_COLUMN_FIRSTNAME + " TEXT,"
            + TABLE_USER_COLUMN_LASTNAME + " TEXT,"
            + TABLE_USER_COLUMN_MOBILE + " TEXT,"
            + TABLE_USER_COLUMN_USER_PHOTO + " blob)")

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(USER_TABLE)
        } catch (e: Exception) {
            Log.e("onCreate", "" + e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("drop table if exists " + TABLE_USER_TABLE)
            onCreate(db)
        } catch (e: Exception) {
            Log.e("onUpgrade", "" + e)
        }
    }
}