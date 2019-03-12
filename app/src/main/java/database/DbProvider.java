package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class DbProvider {
    private static final String _ID = "SN";
    private static final String NAME = "name";
    private static int length;

    static Context context;
    SQLiteDatabase database;
    DbHelper helper;

    public DbProvider(){}

    public DbProvider(Context ctx){
        context = ctx;
        helper = new DbHelper(context);
        database = helper.openDataBase();
    }


    public int getLength(){
        String table = "Easy";
        Cursor catCursor = database.query(table, new String[] {_ID}, null, null, null, null, null);
        if(catCursor!=null){
            catCursor.moveToFirst();
            length = catCursor.getCount();
            catCursor.close();
        }

        return length;
    }

    public String[] getQuestions(String table, int row){
        Cursor cursor = database.query(table, new String[] {"Question", "A", "B", "C", "D", "Correct"}, _ID +"="+row, null, null, null, null);
        String[] dataSet = new String[6];
        cursor.moveToFirst();
        for(int i = 0; i<=5; i++){
            dataSet[i] = cursor.getString(i);
        }
        cursor.close();
        return dataSet;
    }

    public HashMap getLevelData(int level){
        String gamelevel = "\"lvl1\"";
        switch (level){
            case 1:
                gamelevel = "\"lvl1\""; break;
            case 2:
                gamelevel = "\"lvl2\""; break;
            case 3:
                gamelevel = "\"lvl3\""; break;
        }
        Cursor cursor = database.query("Details", new String[] {"Allowed", "SavedGame", "Completed", "SavedLife", "SavedScore", "SavedQuestion"}, "GameLevel="+gamelevel, null, null, null, null);
        HashMap<String, Object> dataSet = new HashMap<>();
        cursor.moveToFirst();
        dataSet.put("allowed", cursor.getInt(0));
        dataSet.put("savedGame", cursor.getInt(1));
        dataSet.put("completed", cursor.getInt(2));
        dataSet.put("savedLife", cursor.getInt(3));
        dataSet.put("savedScore", cursor.getInt(4));
        dataSet.put("savedQuestion", cursor.getInt(5));

        cursor.close();
        return dataSet;

    }

    public void saveGame(int level, int allowed, int saved, int completed,int savedLife,int savedScore,int savedQuestion){
        String gamelevel = "\"lvl1\"";
        switch (level){
            case 1:
                gamelevel = "\"lvl1\""; break;
            case 2:
                gamelevel = "\"lvl2\""; break;
            case 3:
                gamelevel = "\"lvl3\""; break;
        }
        ContentValues values = new ContentValues();

        values.put("Allowed", allowed);
        values.put("SavedGame", saved);
        values.put("Completed", completed);
        values.put("SavedLife", savedLife);
        values.put("SavedScore", savedScore);
        values.put("SavedQuestion", savedQuestion);
        database.update("Details", values, "GameLevel =" + gamelevel, null);
    }
}
