package com.dkoptin.loftmoney;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class AddItemActivity extends AppCompatActivity{

    private TextInputEditText editName;
    private TextInputEditText editPrice;
    private Button addButton;

    private String value;
    private String name;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editName = findViewById(R.id.edittext_hint_name);
        editPrice = findViewById(R.id.edittext_hint_price);
        addButton = findViewById(R.id.add_button);

        editName.addTextChangedListener(watcher);
        editPrice.addTextChangedListener(watcher);

        configureAddingExpenses();

        changeColorText();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddItemActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //auto generated
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String changeColor = getIntent().getExtras().getString("tag");

            if(editName.getText().length() > 0 && editPrice.getText().length() > 0) {

                value = editPrice.getText().toString();
                name = editName.getText().toString();

                if (!addButton.isEnabled()) {
                    if (changeColor.equals("expense")) {
                        addButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_addbutton_enabled_expense);
                        addButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    } else {
                        addButton.setTextColor(getResources().getColor(R.color.colorTemp));
                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_addbutton_enabled_income);
                        addButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }
                }
                addButton.setEnabled(true);
            } else {
                if(addButton.isEnabled()) {
                    addButton.setTextColor(getResources().getColor(R.color.colorAccent));
                    Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.ic_addbutton_disabled);
                    addButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }
                addButton.setEnabled(false);
            }
        }
    };

    private void changeColorText() {

        String changeColor = getIntent().getExtras().getString("tag");

        if (changeColor.equals("expense"))  {
            editName.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
            editPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        } else {
            editName.setTextColor(getApplicationContext().getResources().getColor(R.color.colorTemp));
            editPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.colorTemp));
        }
    }
    private void configureAddingExpenses() {
        addButton.setOnClickListener(view -> {
            final String token = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(Prefs.TOKEN,"");
            compositeDisposable.add(((LoftApp) getApplication()).getMoneyApi().addMoney(token, name, value,  getIntent().getExtras().getString("tag"))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            Log.e("TAG", "Completed");
                            finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("TAG", "Error" + throwable.getLocalizedMessage());

                        }
                    }));
        });
    }



}
