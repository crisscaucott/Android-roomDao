package com.example.ccaucott.roomwords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class NewWordActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.example.ccaucott.roomword.REPLY";
    public static final String WORD_EDIT_REPLY = "com.example.ccaucott.roomword.WORD_EDIT_REPLY";
    public static final String WORDID_EDIT_REPLY = "com.example.ccaucott.roomword.WORDID_EDIT_REPLY";

    private EditText mEditWordView;
    private Word mWordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        mEditWordView = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finishActivity();
            }
        });

        // cuando la actividad es llamada por el click de un item,
        // llenar el campo de editar palabra.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if (bundle.getString("word_str") != null && bundle.getInt("word_id", 0) != 0){
                mWordEdit = new Word(bundle.getInt("word_id"), bundle.getString("word_str"));
                mEditWordView.setText(mWordEdit.getWord());
            }
        }
        // Foco al edittext y mostrar el teclado en pantalla.
        mEditWordView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        // Listener en el editext para captar el ENTER.
        mEditWordView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    if (keyCode == KeyEvent.KEYCODE_ENTER){
                        finishActivity();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void finishActivity(){
        Intent replyIntent = new Intent();
        String word = mEditWordView.getText().toString();

        if (mWordEdit == null){
            // Es una palabra nueva.
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                replyIntent.putExtra(EXTRA_REPLY, word);
                setResult(RESULT_OK, replyIntent);
            }
        }else{
            // Es una palabra a editar.
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            }else{
                replyIntent.putExtra(WORD_EDIT_REPLY, word);
                replyIntent.putExtra(WORDID_EDIT_REPLY, mWordEdit.getId());
                setResult(RESULT_OK, replyIntent);
            }
        }
        hideKeyboard(NewWordActivity.this);
        finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
