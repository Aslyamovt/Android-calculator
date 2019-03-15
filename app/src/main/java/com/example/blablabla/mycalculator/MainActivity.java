package com.example.blablabla.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.LinkedList;
import java.math.MathContext;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView resultField;
    String result= "None";
    LinkedList<String> lasttext = new LinkedList<String>();
    Integer otkrcounter=0;
    Integer zakrconter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField =(TextView) findViewById(R.id.resultField);
        lasttext.add(" ");
    }

    public boolean checkString(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean isDelim(char c) {
        return c ==' ';
    }

    boolean isOperator(String c) {
        return c.equals("+") || c.equals("–") || c.equals("*") || c.equals("/") || c.equals("MOD")||c.equals("^")||c.equals("XOR");
    }

    boolean isFunction(String c) {
        return c.equals("SQRT");
    }

    int priority(String op) {
        switch (op) {
            case"XOR":
                return 1;
            case "+":
            case "–":
                return 2;
            case "*":
            case "/":
            case "MOD":
                return 3;
            case"^":
            case"SQRT":
                return 4;
            default:
                return -1;
        }
    }

    void processOperator(LinkedList<Double> st, String op) {
        if(isOperator(op)){
            double r = st.removeLast();
            double l = st.removeLast();
            switch (op) {
                case "+":
                    st.add(l + r);
                    result = "None";
                    break;
                case "–":
                    st.add(l - r);
                    result = "None";
                    break;
                case "*":
                    st.add(l * r);
                    result = "None";
                    break;
                case "/":
                    st.add(l / r);
                    result = "None";
                    break;
                case "MOD":
                    st.add((double)((int)l % (int)r));
                    result = String.valueOf((int)l / (int)r);
                    break;
                case"^":
                    st.add(Math.pow(l,r));
                    result = "None";
                    break;
                case"XOR":
                    st.add((double)((int)l^(int)r));
                    result = "None";
                    break;
            }
        }
        else{
            double r = st.removeLast();
            switch (op) {
                case "SQRT":
                    st.add(Math.sqrt(r));
                    result = "None";
                    break;
            }
        }
    }

    public Double eval(String s) {
        LinkedList<Double> st = new LinkedList<Double>();
        LinkedList<String> op = new LinkedList<String>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (isDelim(c))
                continue;
            if (c == '(')
                op.add("(");
            else if (c == ')') {
                while (!op.getLast().equals("("))
                    processOperator(st,op.removeLast());
                op.removeLast();
            }
             else if(Character.isDigit(s.charAt(i)) || s.charAt(i)=='.'||s.charAt(i)=='-'){
                String operand = "";
                while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i)=='.'||s.charAt(i)=='-')&&!isDelim(s.charAt(i)))
                    operand += s.charAt(i++);
                --i;
                if(operand.equals("-")){
                    operand = "-1";
                    op.add("*");
                }
                st.add(Double.parseDouble(operand));
            }else{
                String oper="";
                while(i< s.length() && !(Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')&& s.charAt(i)!='(' && s.charAt(i)!=')' && !isDelim(s.charAt(i)))
                    oper += s.charAt(i++);
                --i;
                if(isOperator(oper)){
                while (!op.isEmpty() && priority(op.getLast()) >= priority(oper))
                    processOperator(st, op.removeLast());
                }
                op.add(oper);
             }
        }
        while (!op.isEmpty())
            processOperator(st, op.removeLast());
        return st.get(0);
    }

    public void onButtonClick(View view){
        Button button = (Button)view;
        String text = button.getText().toString();
        if(text.equals(")")){
            if(!(isOperator(lasttext.getLast())||isFunction(lasttext.getLast())||lasttext.getLast().equals("(")||lasttext.getLast().equals(" "))&&otkrcounter>zakrconter){
                lasttext.add(text);
                resultField.append(text);
                zakrconter++;
            }
        }
        else if(text.equals("(")){
            if(isOperator(lasttext.getLast())){
                lasttext.add(text);
                text=" "+text;
                resultField.append(text);
                otkrcounter++;
            }else if(isFunction(lasttext.getLast())||lasttext.getLast().equals("(")||lasttext.getLast().equals(" ")||lasttext.getLast().equals("-")){
                lasttext.add(text);
                resultField.append(text);
                otkrcounter++;
            }
            else if(lasttext.getLast().equals("=")){
                lasttext.clear();
                resultField.setText("");
                lasttext.add(" ");
                lasttext.add(text);
                resultField.append(text);
                otkrcounter++;
            }
        }
        else if(text.equals("-")){
            if(checkString(lasttext.getLast())||lasttext.getLast().equals(")")){
                text = "–";
                lasttext.add(text);
                text=" " + text;
                resultField.append(text);
            }
            else if(lasttext.getLast().equals("(")||lasttext.getLast().equals(" ")){
                lasttext.add(text);
                resultField.append(text);
            }
            else if(lasttext.getLast().equals("=")){
                lasttext.clear();
                resultField.setText("");
                lasttext.add(" ");
                lasttext.add(text);
                resultField.append(text);
            }
            else{
                lasttext.add(text);
                text=" " + text;
                resultField.append(text);
            }
        }
        else if(isOperator(text)){
            if(checkString(lasttext.getLast())||lasttext.getLast().equals(")")){
                lasttext.add(text);
                text=" " + text;
                resultField.append(text);
            }
        }
        else if(isFunction(text)){
            if(isOperator(lasttext.getLast())){
                lasttext.add(text);
                text=" " + text + "(";
                lasttext.add("(");
                resultField.append(text);
                otkrcounter++;
            }
            else if(lasttext.getLast().equals(" ")||lasttext.getLast().equals("-")){
                lasttext.add(text);
                text = text + "(";
                lasttext.add("(");
                resultField.append(text);
                otkrcounter++;
            }
            else if(lasttext.getLast().equals("=")){
                lasttext.clear();
                resultField.setText("");
                lasttext.add(" ");
                lasttext.add(text);
                text = text + "(";
                lasttext.add("(");
                resultField.setText(text);
                otkrcounter++;
            }
        }
        else{
            if(lasttext.getLast().equals(" ")){
                lasttext.add(text);
                resultField.setText(text);
            }
            else if(lasttext.getLast().equals("=")){
                lasttext.clear();
                resultField.setText("");
                lasttext.add(" ");
                lasttext.add(text);
                resultField.append(text);
            }
            else if(checkString(lasttext.getLast())||lasttext.getLast().equals(".")||lasttext.getLast().equals("(")||lasttext.getLast().equals("-")){
                lasttext.add(text);
                resultField.append(text);
            }
            else if(isOperator(lasttext.getLast())){
                lasttext.add(text);
                text= " " + text;
                resultField.append(text);
            }
        }
    }

    public void delOne(View view){
        if(resultField.getText().length()>0){
            if(lasttext.getLast().equals("=")){
                lasttext.clear();
                resultField.setText("");
                lasttext.add(" ");
            }
            else if(resultField.getText().charAt(resultField.getText().length()-1)==' '&&resultField.getText().length()>1){
                StringBuffer str = new StringBuffer(resultField.getText().toString());
                str.delete(resultField.getText().length()-1, resultField.getText().length());
                resultField.setText(str.toString());
            }
            else{
                int l = lasttext.getLast().length();
                StringBuffer str = new StringBuffer(resultField.getText().toString());
                str.delete(resultField.getText().length()-l, resultField.getText().length());
                if(str.length()>0)
                    if(str.charAt(str.length()-1)==' ')
                        str.delete(str.length()-1, str.length());
                resultField.setText(str.toString());
                if (lasttext.getLast().equals(")"))
                    zakrconter--;
                if (lasttext.getLast().equals("("))
                    otkrcounter--;
                lasttext.removeLast();
            }
        }
    }

    public void resultOfCalculations (View view){
        if(otkrcounter!=zakrconter){
            Toast toast = Toast.makeText(this, "Ошибка! Колличество скобок не одинаково",Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(isOperator(lasttext.getLast())||resultField.getText().charAt(resultField.getText().length()-1)=='.'||resultField.getText().charAt(resultField.getText().length()-1)=='-'){
            Toast toast = Toast.makeText(this, "Ошибка! Некорректное выражение",Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            String s = resultField.getText().toString();
            Double d;
            if(!s.isEmpty())
            {
                d = eval(s);
                resultField.append(String.format("\n= %f",d));
                if(result!="None")
                    resultField.append(String.format("\nЧастное = %s",result));
                lasttext.add("=");
            }
        }
    }
}

//android:layout_height="wrap_content"


