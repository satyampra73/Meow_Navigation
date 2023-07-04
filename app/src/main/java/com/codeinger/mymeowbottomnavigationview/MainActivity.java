package com.codeinger.mymeowbottomnavigationview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bnv_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv_Main = findViewById(R.id.bnv_Main);
        bnv_Main.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        bnv_Main.add(new MeowBottomNavigation.Model(2,R.drawable.search));
        bnv_Main.add(new MeowBottomNavigation.Model(3,R.drawable.bookmark));
        bnv_Main.add(new MeowBottomNavigation.Model(4,R.drawable.person));

        bnv_Main.show(1,true);
        replace(new HomeFragment());
        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        replaceWithBackStack(new HomeFragment(), "HomeFragment");
                        break;

                    case 2:
                        replaceWithBackStack(new SearchFragment(), "SearchFragment");
                        break;

                    case 3:
                        replaceWithBackStack(new BookmarkFragment(), "BookmarkFragment");
                        break;

                    case 4:
                        replaceWithBackStack(new ProfileFragment(), "ProfileFragment");
                        break;
                }
                return null;
            }
        });



    }
    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.commit();
    }

    private void replaceWithBackStack(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment, tag);
        transaction.addToBackStack(tag); // Add the fragment to backstack with a tag
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = fragmentManager.getBackStackEntryCount();

        if (backStackCount > 0) {
            try {
                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(backStackCount - 2);
                String fragmentTag = backEntry.getName();

                updateBottomNavigationIcon(fragmentTag);
                fragmentManager.popBackStack();
            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
                Log.d("strData",e.getLocalizedMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure You Want to Exit?")
                        .setTitle("Exit !")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                System.exit(0);
                            }
                        }).show();
            }


            // Update the selected state of the bottom navigation icon
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are You Sure You Want to Exit?")
                    .setTitle("Exit !")
                    .setNegativeButton("NO", null)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }
                    }).show();
        }
    }

    private void updateBottomNavigationIcon(String fragmentTag) {
        Log.d("strData","Fragment Tag: "+fragmentTag);
        int itemId = 0;
        switch (fragmentTag) {
            case "HomeFragment":
                itemId = 1;
                break;
            case "SearchFragment":
                itemId = 2;
                break;
            case "BookmarkFragment":
                itemId = 3;
                break;
            case "ProfileFragment":
                itemId = 4;
                break;

        }
        bnv_Main.show(itemId,true);
    }
}