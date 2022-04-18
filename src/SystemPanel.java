import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SystemPanel extends JPanel {

    //Хранение данных
    Double[][] list;


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
            labels[i].setBounds(150+(i*400), 10,400, 20);
            add(labels[i]);
        }
    }

    //Функция для оформления системы ограничений в обычном и каноническом виде.
    void FieldsCreate(int count, int constr){
        //text - поля ввода
        //labelX - надписи переменных x, которые итерируются с 0 до количества переменных x
        list = new Double[constr+1][count+1];

        JTextField[][] text = new JTextField[constr+1][count+1];
        JLabel[][] labelX = new JLabel[constr][count];

        /*
        labelX[count] = new JLabel(CreateString1(count, constr));
        labelX[count].setFont(ColorFont.simple);
        labelX[count].setBounds(215, 53, 400,20);
        add(labelX[count]);
        */


        //Создаем и добавляем компоненты текстовых полей в систему:
        for (int i =0; i<constr; i++) {
            text[i][constr] = new JTextField();
            text[i][constr].setHorizontalAlignment(JTextField.CENTER);
            text[i][constr].setFont(ColorFont.simple);
            text[i][constr].setBounds(30 + (count * 80), 40 + (i * 40), 40, 20);
            add(text[i][constr]);
        }
        for (int i=0;i<constr; i++){
            for (int j = 0; j<count; j++ ){
                text[i][j] = new JTextField();
                text[i][j].setHorizontalAlignment(JTextField.CENTER);
                text[i][j].setFont(ColorFont.simple);
                text[i][j].setBounds(20+(j*80), 40+(i*40), 40,20);

                if (j == count-1) labelX[i][j] = new JLabel(CreateString(j, true));
                else labelX[i][j] = new JLabel(CreateString(j, false));
                labelX[i][j].setFont(ColorFont.simple);
                labelX[i][j].setBounds(62+(j*80), 40+(i*40), 100,20);

                add(labelX[i][j]); add(text[i][j]);

                //Listener для обработки ввода в ячейки поля ввода и еще некоторых других функциях (описаны ниже)
                int finalI = i;
                int finalJ = j;
                text[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        list[finalI][finalJ] = Double.valueOf(String.valueOf(text[finalI][finalJ]));
                        labelX[finalI][finalJ].setText(CreateString1(count, constr));

                        /*
                        if (e.getSource()==text[text.length-1]){
                            //Переводим фокус на нулевой индекс
                            e.setSource(text[0]); ((JTextField) e.getSource()).requestFocusInWindow();
                        } else {
                            //Переводим фокус левее
                            e.setSource(text[Arrays.asList(text).indexOf((JTextField)e.getSource()) + 1]);
                            ((JTextField) e.getSource()).requestFocusInWindow();
                        }
                        */
                    }
                });

            }
        }

    }

    //Функция оформления строки ограничения в системе ограничений
    String CreateString(int i, boolean j){
        i+=1;
        if (j){
            return "*x"+i+" <= ";
        }
        else return "*x"+i+" + ";
    }

    //Функция оформления строки ограничений в системе ограничений в канон виде
    String CreateString1(int count, int constr){
        StringBuilder temp = new StringBuilder();

        for (int i=0; i<constr; i++){
            for (int j=0; j<count; j++) {
                list[i][j] = 1.0;
                temp.append(list[i][j]).append("*x").append(i + 1).append(" + ");
            }
        }
        for (int i=count; i<constr+count; i++){
            if (i!=constr-1+count) temp.append("x").append(i + 1).append(" + ");
            else temp.append("x").append(i + 1).append(" =");
        }
        return String.valueOf(temp);
    }

}

