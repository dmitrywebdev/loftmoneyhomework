package com.dkoptin.loftmoney.cells.money;

import com.dkoptin.loftmoney.R;
import com.dkoptin.loftmoney.remote.MoneyItem;

public class MoneyCellModel {
    private int id;
    private String name;
    private String value;
    private Integer color;
    private String date;

    public MoneyCellModel(String name, String value, Integer color, String date) {
        this.name = name;
        this.value = value;
        this.color = color;
        this.date = date;
    }


    public static MoneyCellModel getInstance(MoneyItem moneyItem) {
        return new MoneyCellModel(moneyItem.getName(),
                moneyItem.getPrice() + " ₽",
                moneyItem.getType().equals("expense") ? R.color.expenseColor : R.color.colorTemp,
                moneyItem.getDate());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Integer getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }
}
