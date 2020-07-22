package com.dkoptin.loftmoney;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dkoptin.loftmoney.cells.money.MoneyCellModel;

public class AddItemActivity extends AppCompatActivity{

    private EditText editName;
    private EditText editPrice;
    private Button addButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editName = findViewById(R.id.edittext_hint_name);
        editPrice = findViewById(R.id.edittext_hint_price);
        addButton = findViewById(R.id.add_button);

        editName.addTextChangedListener(watcher);
        editPrice.addTextChangedListener(watcher);
    }


        private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //auto generated
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //auto generated
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editName.getText().length() > 0 && editPrice.getText().length() > 0){
                addButton.setEnabled(true);
                addButton.setTextColor(getResources().getColor(R.color.colorTemp));
            } else {
                addButton.setEnabled(false);
                addButton.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    };

}
