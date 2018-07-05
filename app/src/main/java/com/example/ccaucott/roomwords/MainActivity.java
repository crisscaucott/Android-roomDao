package com.example.ccaucott.roomwords;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WordListAdapter.OnItemInteractionListener{
    private WordViewModel mWordViewModel;
    private WordListAdapter mAdapter;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new WordListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                mAdapter.setWords(words);
            }

        });

        mWordViewModel.getLastWordInserted().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long id) {
                mAdapter.setWordIdToAnimate(id);
                //Log.d(MainActivity.class.getSimpleName(), "CAYO UNA NUEVA WORD");
            }
        });

        // Funcionalidad de swipe en los items del RecyclerView.
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word theWord = mAdapter.getWordAtPosition(position);

                Toast.makeText(MainActivity.this, getString(R.string.deleting_toast, theWord.getWord()), Toast.LENGTH_LONG).show();

                // Borrar la palabra de la BD.
                mWordViewModel.deleteWord(theWord);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            Toast.makeText(this, R.string.clear_data_msg, Toast.LENGTH_SHORT).show();

            mWordViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case NEW_WORD_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
                    mWordViewModel.insert(word);
                }else{
                    //Toast.makeText(getApplicationContext(),R.string.empty_not_saved,Toast.LENGTH_LONG).show();
                }
                break;

            case EDIT_WORD_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    Word word = new Word(
                            data.getIntExtra(NewWordActivity.WORDID_EDIT_REPLY, 0),
                            data.getStringExtra(NewWordActivity.WORD_EDIT_REPLY)
                    );
                    mWordViewModel.updateWord(word);
                    mAdapter.setWordIdToAnimate(word.getId());
                }else{
                    //Toast.makeText(getApplicationContext(),R.string.empty_not_saved,Toast.LENGTH_LONG).show();
                }
                break;
        }

    }


    // Callback cuando se hace click en un item del RecyclerView.
    @Override
    public void onClickItem(Word word) {
        Intent intent = new Intent(MainActivity.this, NewWordActivity.class);

        intent.putExtra("word_str", word.getWord());
        intent.putExtra("word_id", word.getId());
        startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
    }
}
