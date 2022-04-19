import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class FunctionPanel extends JPanel {

    //Хранение данных
    ArrayList<Double> list =new ArrayList<>(2);

    public ArrayList<Double> getList() {
        return list;
    }

    //Формирование панели
    public void Processing (){
        setSize(695, 100);
        setBackground(ColorFont.colorPanel);
        setLocation(290,20);
        setLayout(null);

        list.add(0, 1.0); list.add(1, 1.0);

        //Создание надписей заголовков
        LabelCreate();
    }

    //Создание заголовков
    void LabelCreate(){
        String [] bd = {"Целевая функция:", "Канонический вид:"};
        JLabel[] labels = new JLabel[2];
        for (int i=0; i<2; i++){
            labels[i] = new JLabel(bd[i]);
            labels[i].setFont(ColorFont.titles);
            labels[i].setBounds(15, 20+(i*33),200, 20);
            add(labels[i]);
        }
    }

    //Создание полей ввода и labels для формирования целевой функции
    void FieldsCreate(int count, int constr){
        //text - поля ввода
        //labelX - надписи переменных x, которые итерируются с 0 до количества переменных x
        JTextField[] text = new JTextField[count];
        JLabel[] labelX = new JLabel[count +1];

        //Надпись целевой функции канонического вида, формируется на основе динамического массива list и целевой функции,
        //по умолчанию, коэффициенты переменных имеют значение = 1.0
        labelX[count] = new JLabel(CreateString1(count, constr));
        labelX[count].setFont(ColorFont.simple);
        labelX[count].setBounds(215, 53, 400,20);
        add(labelX[count]);

        //Формирование целевой функции
        for (int i = 0; i<count; i++ ){
            text[i] = new JTextField();
            text[i].setHorizontalAlignment(JTextField.CENTER);
            text[i].setFont(ColorFont.simple);
            text[i].setBounds(215+(i*80), 20, 40,20);

            if (i== count-1) labelX[i] = new JLabel(CreateString(i, true));
            else labelX[i] = new JLabel(CreateString(i, false));
            labelX[i].setFont(ColorFont.simple);
            labelX[i].setBounds(257+(i*80), 20, 45,20);

            add(labelX[i]); add(text[i]);

            //Добавляем listener для обработки ввода в поля ввода text[i]
            int finalI = i;
            text[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Если было изменено количество переменных x - меняется размер динамического массива по мере
                    //ввода текста в определенные текстовые поля.
                    if (finalI >=list.size()){
                        if(!text[finalI].getText().equals("")) list.add(finalI, Double.valueOf(text[finalI].getText()));
                        else list.add(finalI, 1.0);
                    }
                    else {
                        if (!text[finalI].getText().equals("")) list.set(finalI, Double.valueOf(text[finalI].getText()));
                        else list.set(finalI, 1.0);
                    }

                    //Затем меняется надпись целевой функции с каноническим видом посредством вызова функции создания соответствующей строки String
                    labelX[count].setText(CreateString1(count, constr));

                    //Для удобства включил функцию перехода из одного поля ввода в другой нажатием на кнопку ENTER.
                    //Это происходит последовательно и при нахождении последнего текстового поля ввода - переход осуществляется
                    //на самое первое поле ввода.
                    //Важно! Если поле пустое, процесс перехода не будет осуществлен до тех пор, пока туда не будет текст и нажата клавиша ENTER.
                    if (e.getSource()==text[text.length-1]){
                        //Переводим фокус на нулевой индекс
                        e.setSource(text[0]); ((JTextField) e.getSource()).requestFocusInWindow();
                    } else {
                        //Переводим фокус левее
                        e.setSource(text[Arrays.asList(text).indexOf((JTextField)e.getSource()) + 1]);
                        ((JTextField) e.getSource()).requestFocusInWindow();
                    }
                }
            });

        }
    }

    //Функция создания строки String для целевой функции. Конкретно - для каждой переменной отдельно.
    String CreateString(int i, boolean j){
        i+=1;
        if (j){
            return "*x"+i+";";
        }
        else return "*x"+i+" + ";
    }

    //Функция создания строки String для всей целевой функции в канон виде.
    String CreateString1(int count, int constr){
        StringBuilder temp = new StringBuilder();

        for (int i=0; i<count; i++){
            if (i >=list.size()){
                list.add(i, 1.0);
            }
            temp.append(list.get(i)).append("*x").append(i + 1).append(" + ");
        }
        for (int i=count; i<constr+count; i++){
            if (i!=constr-1+count) temp.append("0*x").append(i + 1).append(" + ");
            else temp.append("0*x").append(i + 1).append(".");
        }
        return String.valueOf(temp);
    }
}
