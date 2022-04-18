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
        JLabel labels = new JLabel();
            labels = new JLabel("Система ограничений в каноническом виде:");
            labels.setFont(ColorFont.titles);
            labels.setBounds(150, 10,500, 20);
            add(labels);
    }

    //Функция для оформления системы ограничений в обычном и каноническом виде.
    void FieldsCreate(int count, int constr){
        //text - поля ввода
        //labelX - надписи переменных x, которые итерируются с 0 до количества переменных x
        list = new Double[constr][count+1];

        JTextField[][] text = new JTextField[constr][count+1];
        JLabel[][] labelX = new JLabel[constr][count];

        FieldsListen listen = new FieldsListen();
        /*
        labelX[count] = new JLabel(FieldsListen.CreateString1(count, constr));
        labelX[count].setFont(ColorFont.simple);
        labelX[count].setBounds(215, 53, 400,20);
        add(labelX[count]);
        */



        //Создаем и добавляем компоненты текстовых полей в систему:
        for (int i =0; i<constr; i++) {
            text[i][count] = new JTextField();
            text[i][count].setHorizontalAlignment(JTextField.CENTER);
            text[i][count].setFont(ColorFont.simple);
            text[i][count].setBounds(55 + (count * 80), 40 + (i * 40), 40, 20);
            add(text[i][count]);

            listen.count = count;
            listen.constr = constr;

            listen.text = text;
            listen.list = list;

            text[i][count].addActionListener(listen);



        }
        for (int i=0;i<constr; i++){
            for (int j = 0; j<count; j++ ){
                text[i][j] = new JTextField();
                text[i][j].setHorizontalAlignment(JTextField.CENTER);
                text[i][j].setFont(ColorFont.simple);
                text[i][j].setBounds(20+(j*80), 40+(i*40), 40,20);

                if (j == count-1) labelX[i][j] = new JLabel(CreateString(j, true, count+1+i));
                else labelX[i][j] = new JLabel(CreateString(j, false, -1));
                labelX[i][j].setFont(ColorFont.simple);
                labelX[i][j].setBounds(62+(j*80), 40+(i*40), 100,20);

                add(labelX[i][j]); add(text[i][j]);

                //Listener для обработки ввода в ячейки поля ввода и еще некоторых других функциях (описаны ниже)
                listen.count = count;
                listen.constr = constr;

                listen.labelX = labelX;
                listen.text = text;
                listen.list = list;
                text[i][j].addActionListener(listen);
            }
        }

    }

    //Функция оформления строки ограничения в системе ограничений
    String CreateString(int i, boolean j, int constr){
        i+=1;
        if (j){
            return "*x" + i +" + x" + constr + " = ";
        }
        else return "*x" + i + " + ";
    }
    //Функция оформления строки ограничений в системе ограничений в канон виде
 /*
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
            else temp.append("x").append(i + 1).append(" = ");
        }
        return String.valueOf(temp);
    }
*/
}
class FieldsListen implements ActionListener{
    int count, constr;
    JTextField[][] text;
    Double[][] list;
    JLabel[][] labelX;
    int finalI, finalJ;

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i =0; i<constr; i++){
            for (int j =0; j<=count; j++) {
                if (e.getSource() == text[i][j]) {
                    finalI = i;
                    finalJ = j;
                    break;
                }
            }
        }
        if (!text[finalI][finalJ].getText().equals(""))
            list[finalI][finalJ] = Double.valueOf(text[finalI][finalJ].getText());
        //labelX[finalI][finalJ].setText(CreateString1(count, constr));

        //Проверяем, не последний ли элемент в строке
        if (finalJ != count) {
                e.setSource(text[finalI][finalJ+1]);
                ((JTextField) e.getSource()).requestFocusInWindow();
        }
        //Если последний:
        else {
            //Проверяем, не последний ли элемент в матрице:
            if(finalI != constr-1) {
                e.setSource(text[finalI+1][0]);
                ((JTextField) e.getSource()).requestFocusInWindow();
            }
            //Последний:
            else {
                e.setSource(text[0][0]); ((JTextField) e.getSource()).requestFocusInWindow();
            }
        }
    }

}

