import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.management.ClassLoadingMXBean;
import java.util.Arrays;

/*public class SettingPanel extends JPanel {
    //Количество переменных и ограничений - по 2 соответственно
    int countVar = 2;
    int countConstr = 2;
    FunctionPanel functionPanel = new FunctionPanel();


    public int getCountConstr() {
        return countConstr;
    }
    public int getCountVar() {
        return countVar;
    }
    public void setCountConstr(int countConstr) {
        this.countConstr = countConstr;
    }
    public void setCountVar(int countVar) {
        this.countVar = countVar;
    }

    public void Processing(){
        setSize(965, 100);
        //setBackground(ColorFont.colorPanel);
        setLocation(20,20);
        setLayout(null);
        //Создаем labels "Настройки", "Количество переменных" и "Количество ограничений":
        LabelCreate();
        //Создаем поля ввода для переменных и ограничений
        TextFieldCreate();

        functionPanel.Processing();
        functionPanel.FieldsCreate(countVar, countConstr);
        add(functionPanel);
    }

    void LabelCreate(){
        String [] bd = {"Настройки", "Количество переменных:", "Количество ограничений:"};
        JLabel[] labels = new JLabel[3];
        for (int i=0; i<3; i++){
            labels[i] = new JLabel(bd[i]);
            labels[i].setFont(ColorFont.simple);
            labels[i].setBounds(15, 15+(i*23),200, 20);
            add(labels[i]);
        } labels[0].setFont(ColorFont.titles);
    }

    JTextField[] textFields = new JTextField[2];
    void TextFieldCreate() {
        for (int i = 0; i < 2; i++) {
            textFields[i] = new JTextField("2");
            textFields[i].setFont(ColorFont.simple);
            textFields[i].setSize(20, 20);
            textFields[i].setLocation(200, 15 + ((i + 1) * 23));
            add(textFields[i]);
            textFields[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource()==textFields[0]){
                        if (!textFields[0].getText().equals("")) countVar = Integer.parseInt(textFields[0].getText());
                        //Переводим фокус на нижний textField
                        e.setSource(textFields[Arrays.asList(textFields).indexOf((JTextField)e.getSource()) + 1]);
                        ((JTextField) e.getSource()).requestFocusInWindow();
                    } else {
                        if (!textFields[1].getText().equals("")) countConstr = Integer.parseInt(textFields[1].getText());
                        //Переводим фокус на верхний textField
                        e.setSource(textFields[Arrays.asList(textFields).indexOf((JTextField)e.getSource()) - 1]);
                        ((JTextField) e.getSource()).requestFocusInWindow();
                    }
                    functionPanel.setVisible(false);
                    functionPanel = new FunctionPanel();
                    functionPanel.setVisible(true);
                    functionPanel.Processing();
                    functionPanel.FieldsCreate(countVar, countConstr);
                    add(functionPanel);
                }
            });
        }
    }
}
*/