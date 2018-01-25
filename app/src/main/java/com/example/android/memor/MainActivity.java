package com.example.android.memor;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.example.android.memor.data.Contract;
import com.example.android.memor.data.SQLhelper;
import com.example.android.memor.data.SimpleFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private EditText word, translate;
    private SQLhelper sqLhelper;
    private SQLiteDatabase db;
    private int LOADE_ID = 111;
    private RecyclerView recyclerView, secondRecyclerView;
    private mRecyler mRecyler;
    private secondRecycler secondRecycler;
    private int mPositon = RecyclerView.NO_POSITION;
    private int count = 0;
    private fetchDataCursor ftData;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        ftData = new fetchDataCursor();
//        initialize The DataBase
        sqLhelper = new SQLhelper(this);
//        get access to write/read items to/from Database
        db = sqLhelper.getWritableDatabase();


//        get reference to ToolBar View
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
////        get
//        setSupportActionBar(toolbar);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
//        toggle.syncState();
//        toggle.syncState();
//
//
        word = (EditText) findViewById(R.id.word);
        translate = (EditText) findViewById(R.id.translate);
//
//
//        secondRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
//        secondRecyclerView.setHasFixedSize(true);
//        secondRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        secondRecycler = new secondRecycler(this);
//        secondRecyclerView.setAdapter(secondRecycler);

//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        mRecyler = new mRecyler(this);
//        recyclerView.setAdapter(mRecyler);
//
//
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//                int tag = (int) viewHolder.itemView.getTag();
//                String arra = Integer.toString(tag);
//                String[] id = new String[]{arra};
//                int posioton = db.delete(Contract.WordsContainer.TABLE_NAME, Contract.WordsContainer._ID + "=?", id);
//                if (posioton > 0) {
//                    count = 1;
//                }
//                mRecyler.notifyItemChanged(viewHolder.getAdapterPosition());
//                getSupportLoaderManager().restartLoader(LOADE_ID, null, MainActivity.this);
//            }
//        }).attachToRecyclerView(recyclerView);
////
////
//        getSupportLoaderManager().initLoader(LOADE_ID, null, this);


    }

    public void ButtonForTake(View view) {
        String Word = word.getText().toString();
        String translated = translate.getText().toString();
        if (!TextUtils.isEmpty(Word) && !TextUtils.isEmpty(translated)) {

            long id = insert(Word, translated);
            if (id == -1) {
                Toast.makeText(this, "Occur Some Problem with Inserting", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "For now it's all ok" + id, Toast.LENGTH_SHORT).show();
            }
//

        } else {
            Toast.makeText(this, "all the fields should be used", Toast.LENGTH_SHORT).show();
        }
        word.getText().clear();
        translate.getText().clear();
        getSupportLoaderManager().restartLoader(LOADE_ID, null, this);
    }
//
    private long insert(String word, String translate) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.WordsContainer._WORD, word);
        contentValues.put(Contract.WordsContainer._TRANSLATED, translate);
        return db.insert(Contract.WordsContainer.TABLE_NAME, null, contentValues);
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getSupportLoaderManager().restartLoader(LOADE_ID, null, this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        getSupportLoaderManager().destroyLoader(LOADE_ID);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor cursor;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (cursor != null) {
                    deliverResult(cursor);
                } else {
                    forceLoad();
                }

            }

            @Override
            public Cursor loadInBackground() {
                cursor = db.query(Contract.WordsContainer.TABLE_NAME, null, null, null, null, null, null, null);
                return cursor;
            }

            @Override
            public void deliverResult(Cursor data) {
                cursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mRecyler.swapCursor(data);
        secondRecycler.swapCursro(data);

        if (mPositon == RecyclerView.NO_POSITION) mPositon = 0;
        if (count == 0) {
            recyclerView.smoothScrollToPosition(data.getCount());
        } else {
            count = 0;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
 }
