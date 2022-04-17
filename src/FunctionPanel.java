import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FunctionPanel extends JPanel {

    ArrayList<Double> list =new ArrayList<>(2);

    public ArrayList<Double> getList() {
        return list;
    }

    public void Processing (){
        setSize(695, 100);
        setBackground(ColorFont.colorPanel);
        setLocation(290,20);
        setLayout(null);

        list.add(0, 1.0); list.add(1, 1.0);

        LabelCreate();

        //FieldsCreate(2,2);

    }

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



    void FieldsCreate(int count, int constr){
        JTextField[] text = new JTextField[count];
        JLabel[] labelX = new JLabel[count +1];

        labelX[count] = new JLabel(CreateString1(count, constr));
        labelX[count].setFont(ColorFont.simple);
        labelX[count].setBounds(215, 53, 400,20);
        add(labelX[count]);

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
                }
            });

        }
    }

    String CreateString(int i, boolean j){
        i+=1;
        if (j){
            return "*x"+i+";";
        }
        else return "*x"+i+" + ";
    }

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
