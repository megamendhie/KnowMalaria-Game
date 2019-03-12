package database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.swiftqube.knowmalaria.R;

import java.util.ArrayList;
import java.util.Random;

public class Backend {

    public static ArrayList<Integer> list1 = new ArrayList<Integer>();
    public static ArrayList<Integer> list2 = new ArrayList<Integer>();
    public static ArrayList<Integer> list3 = new ArrayList<Integer>();

    public static int rand;

    public static  int level = 1;
    public static  int coins = 0;
    Context context;
    Random random = new Random();
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    //ArrayList for each category to store
    public static ArrayList<Integer> holder1 = new ArrayList<Integer>();
    public static ArrayList<Integer> holder2 = new ArrayList<Integer>();
    public static ArrayList<Integer> holder3 = new ArrayList<Integer>();

    public Backend(Context context){
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        //game coins
        coins = prefs.getInt("COINS_", 0);
    }

    public void setCoins(int coins){
        this.coins = coins;
        editor.putInt("COINS_", coins);
        editor.apply();
    }

    public int getCoins(){
        coins = prefs.getInt("COINS_", 0);
        return coins;}

    public int Randomize(int level){
        ArrayList<Integer> holder = new ArrayList<Integer>();
        ArrayList<Integer> list = new ArrayList<Integer>();

        switch (level){
            case 1:
                list = list1;
                holder = holder1; break;
            case 2:
                list = list2;
                holder = holder2; break;
            case 3:
                list = list3;
                holder = holder3; break;
        }
        Log.i("Holder", "holder is "+ holder.toString());
        if(list.isEmpty()){
            //creates list of the questions. Equal to the Total number of questions in that category
            for(int i = 1; i <= 30; i++)
                list.add(i); //adds number to list

            //holder is a cache for already seen questions.
            if(holder.size() > 0){
                for(int compare: holder)
                {
                    if(list.contains(compare))
                        list.remove(list.indexOf(compare));
                }
            }
        }
        //this returns a random number from the list of pictures
        rand = random.nextInt(list.size());
        rand = list.remove(rand);
        holder.add(rand); //adds the number to seen questions

        if(holder.size()>18)
            holder.remove(0); //ensures holder doesn't exceed 4 seen questions

        switch (level){
            case 1:
                list1 = list;
                holder1 = holder; break;
            case 2:
                list2 = list;
                holder2 = holder; break;
            case 3:
                list3 = list;
                holder3 = holder; break;
        }

        return rand;
    }


    public void setBackgroundImage(ImageView imgBg){
        /*
        method determines image to display in activity header by selecting randomly from 6 images
         */
        int Bg = 1 + random.nextInt(6);
        switch (Bg){
            case 1:
                imgBg.setImageResource(R.drawable.img_bg_one);
                break;
            case 2:
                imgBg.setImageResource(R.drawable.img_bg_two);
                break;
            case 3:
                imgBg.setImageResource(R.drawable.img_bg_three);
                break;
            case 4:
                imgBg.setImageResource(R.drawable.img_bg_four);
                break;
            case 5:
                imgBg.setImageResource(R.drawable.img_bg_five);
                break;
            case 6:
                imgBg.setImageResource(R.drawable.img_bg_six);
                break;
        }

    }
}
