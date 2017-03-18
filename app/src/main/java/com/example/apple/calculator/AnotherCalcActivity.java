package com.example.apple.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AnotherCalcActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener{

    private EditText numberInput1;
    private EditText numberInput2;
    private Spinner operatorSelector;
    private TextView calcResult;

    private static final int REQUEST_CODE_ANOTHER_CALC_1 = 1;
    private static final int REQUEST_CODE_ANOTHER_CALC_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_calc);

        //onClickが呼ばれるボタンの設定
        //findViewById(R.id.calcButton1).setOnClickListener(this);
        //findViewById(R.id.calcButton2).setOnClickListener(this);
        //findViewById(R.id.nextButton).setOnClickListener(this);
        findViewById(R.id.backButton).setOnClickListener(this);

        numberInput1 = (EditText)findViewById(R.id.numberInput1);
        numberInput1.addTextChangedListener(this);

        numberInput2 = (EditText)findViewById(R.id.numberInput2);
        numberInput2.addTextChangedListener(this);

        operatorSelector = (Spinner)findViewById(R.id.operatorSelector);
        calcResult = (TextView)findViewById(R.id.calcResult);
    }

    //入力チェック
    private boolean checkEditTextInput(){
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();

        return !TextUtils.isEmpty(input1) && !TextUtils.isEmpty(input2);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after){

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count){

    }

    @Override
    public void afterTextChanged(Editable s){
        refreshResult();
    }

    //計算結果の表示を更新する
    private void refreshResult(){
        if(checkEditTextInput()){
            int result = calc();

            String resultText = getString(R.string.calc_result_text, result);
            calcResult.setText(resultText);
        }else{
            calcResult.setText(R.string.calc_result_default);
        }
    }

    //計算を行う
    private int calc(){
        String input1 = numberInput1.getText().toString();
        String input2 = numberInput2.getText().toString();

        int number1 = Integer.parseInt(input1);
        int number2 = Integer.parseInt(input2);

        int operator = operatorSelector.getSelectedItemPosition();

        switch(operator){
            case 0:
                return number1 + number2;
            case 1:
                return number1 - number2;
            case 2:
                return number1 * number2;
            case 3:
                return number1 / number2;
            default:
                throw new RuntimeException();
        }
    }

    //view.OnclickListenerのメソッド
    @Override
    public void onClick(View v){
        if(!checkEditTextInput()){
            setResult(RESULT_CANCELED);
        }else{
            int result = calc();

            //計算結果をIntentに詰める
            Intent data = new Intent();
            data.putExtra("result", result);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    //繊維先から帰ってきた時に呼ばれるclass
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;

        //結果データセットを取り出す(Intentに詰められたものを取り出すためにBundleに詰め替えるイメージ？)
        Bundle resultBundle = data.getExtras();

        //計算結果の取り出し
        if(resultBundle.containsKey("result")) return;
        int result = resultBundle.getInt("result");

        if(requestCode == REQUEST_CODE_ANOTHER_CALC_1){
            numberInput1.setText(String.valueOf(result));

        }else if(requestCode == REQUEST_CODE_ANOTHER_CALC_2){
            numberInput2.setText(String.valueOf(result));

        }
        refreshResult();
    }
}
