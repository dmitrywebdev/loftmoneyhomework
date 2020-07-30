package com.dkoptin.loftmoney.cells.money;

import com.dkoptin.loftmoney.R;
import com.dkoptin.loftmoney.remote.MoneyItem;

public class MoneyCellModel {
    private String name;
    private String value;
    private Integer color;

    public MoneyCellModel(String name, String value, Integer color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public static MoneyCellModel getInstance(MoneyItem moneyItem) {
        return new MoneyCellModel(moneyItem.getName(),
                moneyItem.getPrice() + " ₽",
                moneyItem.getType().equals("expanse") ? R.color.expenseColor : R.color.colorTemp);
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
}
