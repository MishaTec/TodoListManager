package il.ac.huji.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Helper class for managing the database.
 * Created by Michael on 22/04/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo_db";
    private static final String TODO_LIST_TABLE_NAME = "todolist";
    private static final String KEY_ROWID = "_id";
    private static final String TODO_LIST_TABLE_CREATE =
            "create table " + TODO_LIST_TABLE_NAME + " ( " +
                    KEY_ROWID + " integer primary key autoincrement, " +
                    " title text, " +
                    " due long );";
    public static final int TITLE_COLUMN_INDEX = 1;
    public static final int DUE_COLUMN_INDEX = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TODO_LIST_TABLE_CREATE);
    }

    public void onUpgrade(
            SQLiteDatabase db, int oldVer, int newVer) {
    }

    /**
     * Helper method. Returns an updated cursor to the db.
     *
     * @return the new cursor
     */
    public Cursor getCursor() {
        return getWritableDatabase().query(TODO_LIST_TABLE_NAME, null,
                null, null, null, null, null);
    }

    /**
     * Inserts a new to-do item to the db
     *
     * @param title - item's content
     * @param dueDate - item's due date
     * @return the new cursor
     */
    public Cursor insert(String title, Date dueDate) {
        ContentValues newTodoItem = new ContentValues();
        newTodoItem.put("title", title);
        if (dueDate != null) {
            newTodoItem.put("due", dueDate.getTime());
        } else {
            newTodoItem.put("due", "No due date"); // since due is of type long, it puts '-1' instead
        }
        getWritableDatabase().insert(TODO_LIST_TABLE_NAME, null, newTodoItem);
        return getCursor();
    }

    /**
     * Deletes a to-do with the given id
     *
     * @param rowId - the index of the row
     * @return the new cursor
     */
    public Cursor delete(long rowId) {
        getWritableDatabase().delete(TODO_LIST_TABLE_NAME, KEY_ROWID + "=" + rowId, null);
        return getCursor();
    }
}
