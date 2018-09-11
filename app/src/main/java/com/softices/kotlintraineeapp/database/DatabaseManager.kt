package com.softices.kotlintraineeapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.util.Log
import com.softices.kotlintraineeapp.extra.Utility
import com.softices.kotlintraineeapp.models.UserModel
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper

@SuppressLint("SdCardPath")
class DatabaseManager(context: Context) {
    private val mHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {
        private val TAG = "DatabaseManager"
        private lateinit var mDatabase: SQLiteDatabase
    }

    init {
        mDatabase = mHelper.writableDatabase
    }

    fun checkUserLogin(email: String, password: String): UserModel {
        val data = UserModel()
        try {
            val query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE + " WHERE " +
                    DatabaseHelper.TABLE_USER_COLUMN_EMAIL + " = '" + email + "' AND " +
                    DatabaseHelper.TABLE_USER_COLUMN_PASSWORD + " = '" + password + "'"
            Log.e("mye", "query " + query)
            val cursor = mDatabase.rawQuery(query, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                data.firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_FIRSTNAME))
                data.lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_LASTNAME))
                data.email = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_EMAIL))
                data.password = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_PASSWORD))
                data.mobile = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_MOBILE))
                data.photo = Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_USER_PHOTO)))
                return data
            }
        } catch (e: Exception) {
            Log.e(TAG, "checkUserLogin " + e)
        }
        return data
    }

    fun changePassword(email: String, password: String, newPassword: String): Boolean {
        val isUpdate = false
        try {
            val query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE + " WHERE " +
                    DatabaseHelper.TABLE_USER_COLUMN_EMAIL + " = '" + email + "' AND " +
                    DatabaseHelper.TABLE_USER_COLUMN_PASSWORD + " = '" + password + "'"
            val cursor = mDatabase.rawQuery(query, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                val put = ContentValues()
                put.put(DatabaseHelper.TABLE_USER_COLUMN_PASSWORD, newPassword)
                mDatabase.update(DatabaseHelper.TABLE_USER_TABLE, put, DatabaseHelper.TABLE_USER_COLUMN_EMAIL + " = '$email'", null)
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "changePassword " + e)
        }
        return isUpdate
    }

    fun getUserDataByEmail(email: String): UserModel {
        val data = UserModel()
        try {
            val query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE + " WHERE " +
                    DatabaseHelper.TABLE_USER_COLUMN_EMAIL + " = '" + email + "'"
            Log.e("mye", "query " + query)
            val cursor = mDatabase.rawQuery(query, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                data.firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_FIRSTNAME))
                data.lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_LASTNAME))
                data.email = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_EMAIL))
                data.password = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_PASSWORD))
                data.mobile = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_MOBILE))
                data.photo = Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper
                        .TABLE_USER_COLUMN_USER_PHOTO)))
                return data
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserDataByEmail " + e)
        }
        return data
    }

    fun getAllUserData(context: Context): ArrayList<UserModel> {
        val arrayList: MutableList<UserModel> = ArrayList()
        try {
            val query = "SELECT * FROM " + DatabaseHelper.TABLE_USER_TABLE
            Log.e("mye", "query " + query)
            val cursor = mDatabase.rawQuery(query, null)
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val data = UserModel()
                    data.firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                            .TABLE_USER_COLUMN_FIRSTNAME))
                    data.lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                            .TABLE_USER_COLUMN_LASTNAME))
                    data.email = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                            .TABLE_USER_COLUMN_EMAIL))
                    data.password = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                            .TABLE_USER_COLUMN_PASSWORD))
                    data.mobile = cursor.getString(cursor.getColumnIndex(DatabaseHelper
                            .TABLE_USER_COLUMN_MOBILE))
                    data.photo = Utility.getPhoto(cursor.getBlob(cursor.getColumnIndex(DatabaseHelper
                            .TABLE_USER_COLUMN_USER_PHOTO)))
                    Log.e("mye", "ph" + data.email)
                    if (!data.email.equals(PreferenceHelper.getUserEmail(context))) {
                        arrayList.add(data)
                    }
                    Log.e("mye", "--")
                } while (cursor.moveToNext())
                return arrayList as ArrayList<UserModel>
            }
        } catch (e: Exception) {
            Log.e(TAG, "getAllUserData " + e)
        }
        return arrayList as ArrayList<UserModel>
    }


    fun deleteTable(tablename: String) {
        mDatabase.delete(tablename, null, null)
    }

    fun insertIntoTableUser(data: UserModel): Boolean {
        try {
            val put = ContentValues()
            val Bte = Utility.getBytes(data.photo!!)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_FIRSTNAME, data.firstName)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_LASTNAME, data.lastName)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_EMAIL, data.email)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_PASSWORD, data.password)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_MOBILE, data.mobile)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_USER_PHOTO, Bte)
            Log.e("mye", "put $put")
            mDatabase.insert(DatabaseHelper.TABLE_USER_TABLE, null, put)
            return true
        } catch (e: Exception) {
            Log.e(TAG, "insertIntoTableUser" + e)
        }
        return false
    }

    fun updateIntoTableUser(email: String, firstName: String,
                            lastName: String, mobile: String, userPhoto: Bitmap): Int {
        try {
            val put = ContentValues()
            put.put(DatabaseHelper.TABLE_USER_COLUMN_FIRSTNAME, firstName)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_LASTNAME, lastName)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_MOBILE, mobile)
            put.put(DatabaseHelper.TABLE_USER_COLUMN_USER_PHOTO, Utility.getBytes(userPhoto))
            val i = mDatabase.update(DatabaseHelper.TABLE_USER_TABLE, put, "user_email ='$email'", null)
            return i
        } catch (e: Exception) {
            Log.e(TAG, "updateIntoTableUser" + e)
        }
        return 0
    }

    fun deleteUser(id: String) {
        mDatabase.execSQL("delete from " + DatabaseHelper.TABLE_USER_TABLE + " where user_email='$id'")
    }
}