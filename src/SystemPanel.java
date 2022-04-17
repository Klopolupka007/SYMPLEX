import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SystemPanel extends JPanel {

    //Хранение данных
    ArrayList<Double> list =new ArrayList<>(2);

    public ArrayList<Double> getList() {
        return list;
    }


    //Формирование панельки
    void Processing(){
        setSize(965, 300);
        setBackground(ColorFont.colorPanel);
        setLocation(20,140);
        setLayout(null);

        //Вызываем функцию для создания и размещения заголовков
        LabelCreate();
    }

    //Функция для заголовков
    void LabelCreate(){
        String [] bd = {"Система ограничений:", "СО в каноническом виде:"};
        JLabel[] labels = new JLabel[2];
        for (int i=0; i<2; i++){
            labels[i] = new JLabel(bd[i]);
            labels[i].setFont(ColorFont.titles);
            labels[i].setBounds(150+(i*400), 20,400, 20);
            add(labels[i]);
        }
    }

    //Функция для оформления системы ограничений в обычном и каноническом виде.
    void FieldsCreate(int count, int constr){
        //text - поля ввода
        //labelX - надписи переменных x, которые итерируются с 0 до количества переменных x
        JTextField[] text = new JTextField[count];
        JLabel[] labelX = new JLabel[count +1];


        labelX[count] = new JLabel(CreateString1(count, constr));
        labelX[count].setFont(ColorFont.simple);
        labelX[count].setBounds(215, 53, 400,20);
        add(labelX[count]);


        //Создаем и добавляем компоненты текстовых полей в систему:
        for (int i = 0; i<count; i++ ){
            text[i] = new JTextField();
            text[i].setHorizontalAlignment(JTextField.CENTER);
            text[i].setFont(ColorFont.simple);
            text[i].setBounds(20+(i*80), 40, 40,20);

            if (i== count-1) labelX[i] = new JLabel(CreateString(i, true));
            else labelX[i] = new JLabel(CreateString(i, false));
            labelX[i].setFont(ColorFont.simple);
            labelX[i].setBounds(62+(i*80), 40, 45,20);

            add(labelX[i]); add(text[i]);

            //Listener для обработки ввода в ячейки поля ввода и еще некоторых других функциях (описаны ниже)
            int finalI = i;
            text[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (finalI >=list.size()){
                        list.add(finalI, Double.valueOf(text[finalI].getText()));
                    }
                    else {
                        list.set(finalI, Double.valueOf(text[finalI].getText()));
                    }
                    labelX[count].setText(CreateString1(count, constr));

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

    //Функция оформления строки ограничения в системе ограничений
    String CreateString(int i, boolean j){
        i+=1;
        if (j){
            return "*x"+i+" <=;";
        }
        else return "*x"+i+" + ";
    }

    //Функция оформления строки ограничений в системе ограничений в канон виде
    String CreateString1(int count, int constr){
        StringBuilder temp = new StringBuilder();

        for (int i=0; i<count; i++){
            if (i >=list.size()){
                list.add(i, 1.0);
            }
            temp.append(list.get(i)).append("*x").append(i + 1).append(" + ");
        }
        for (int i=count; i<constr+count; i++){
            if (i!=constr-1+count) temp.append("x").append(i + 1).append(" + ");
            else temp.append("x").append(i + 1).append(" =");
        }
        return String.valueOf(temp);
    }

}
