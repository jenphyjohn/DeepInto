package com.mincat.sample.utils;

import android.text.InputFilter;
import android.widget.EditText;

/**
 * @author Ming
 * @描述 限制EditText输入的长度
 */
public class FilterEtLength {

    public static void filter(EditText editText, int length) {

        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }
}
