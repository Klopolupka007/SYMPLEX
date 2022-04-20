import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GlobalPanel extends JPanel {
    SystemPanel systemB = new SystemPanel();
    FunctionPanel functionPanel = new FunctionPanel();
    //Количество переменных и ограничений - по 2 соответственно
    int countVar = 2;
    int countConstr = 2;
    GlobalPanel(){
        setPreferredSize(new Dimension(1024, 520));
        setLayout(null);
        setBackground(new Color(67,64,62));
        //Настраиваем и отображаем окно настроек
        Processing();
    }
    public void Processing(){
        //Создаем labels "Настройки", "Количество переменных" и "Количество ограничений":
        LabelCreate();
        //Создаем поля ввода для переменных и ограничений
        TextFieldCreate();
        //Первичное создание объектов матрицы, системы и целевой функции
        functionPanel.Processing(); systemB.Processing();
        functionPanel.FieldsCreate(countVar, countConstr);
        add(functionPanel);
        systemB.Processing(); systemB.FieldsCreate(countVar, countConstr);
        add(systemB);
        ButtonStart();
    }
    //Надписи для настройки функции и системы
    void LabelCreate(){
        String [] bd = {"Настройки", "Количество переменных:", "Количество ограничений:"};
        JLabel[] labels = new JLabel[3];
        for (int i=0; i<3; i++){
            labels[i] = new JLabel(bd[i]);
            labels[i].setFont(ColorFont.simple);
            labels[i].setBounds(35, 35+(i*23),200, 20);
            add(labels[i]);
        } labels[0].setFont(ColorFont.titles);
    }
    //Функция для обеспечения настройки количества переменных и ограничений
    JTextField[] textFields = new JTextField[2];
    void TextFieldCreate() {
        for (int i = 0; i < 2; i++) {
            //По умолчанию значения в каждом поле ввода = 2
            textFields[i] = new JTextField("2");
            textFields[i].setFont(ColorFont.simple);
            textFields[i].setSize(20, 20);
            textFields[i].setLocation(220, 35 + ((i + 1) * 23));
            add(textFields[i]);
            //Добавляем Listener для:
            //   1. Перевод текста из полей ввода в соответствующие им переменные
            //   2. Декоративное оформление перехода из одного поля в другое простым нажатием на ENTER
            //   3. Обновление панелей матрицы, системы и целевой функции после изменения одного из параметров настроек (полей)
            textFields[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource()==textFields[0]){
                        //Первый пункт
                        if (!textFields[0].getText().equals("")) countVar = Integer.parseInt(textFields[0].getText());

                        //Второй пункт
                        //Переводим фокус на нижний textField
                        e.setSource(textFields[Arrays.asList(textFields).indexOf((JTextField)e.getSource()) + 1]);
                        ((JTextField) e.getSource()).requestFocusInWindow();
                    } else {
                        //Первый пункт
                        if (!textFields[1].getText().equals("")) countConstr = Integer.parseInt(textFields[1].getText());

                        //Второй пункт
                        //Переводим фокус на верхний textField
                        e.setSource(textFields[Arrays.asList(textFields).indexOf((JTextField)e.getSource()) - 1]);
                        ((JTextField) e.getSource()).requestFocusInWindow();
                    }
                    //Третий пункт
                    //Делаем обновление всех панелек
                    initObjects();
                }
            });
        }
        //Бесполезно, но зато красивенько стало...
        //Декоративная панель
        JLabel back = new JLabel();
        back.setOpaque(true);
        back.setBackground(ColorFont.colorPanel);
        back.setBounds(20, 20, 250, 100);
        add(back);
    }
    //Та самая функция обновления панелек с остальными элементами метода
    //"Обновление" происходит путем удаления из памяти неактуальных объектов глобальной панели и замены их на новые
    void initObjects(){
        //Обновление целевой функции
        functionPanel.setVisible(false);
        functionPanel = new FunctionPanel();
        functionPanel.setVisible(true);
        functionPanel.Processing();
        functionPanel.FieldsCreate(countVar, countConstr);
        add(functionPanel);

        //Обновление системы ограничений
        systemB.setVisible(false);
        systemB = new SystemPanel();
        systemB.setVisible(true);
        systemB.Processing();
        systemB.FieldsCreate(countVar, countConstr);
        add(systemB);
    }
    MatrixPanel matrixPanel;
    //Кнопка старта вычислений - после нажатия запускается процесс вычисления по симплексному методу и построение матриц визуально
    void ButtonStart(){
        JButton StartButton = new JButton("Start");
        StartButton.setBounds(20, 445, 100, 30);
        StartButton.setOpaque(true);
        StartButton.setBackground(ColorFont.colorGreen);
        StartButton.setFocusPainted(false);
        add(StartButton);
        StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Обновление вычислений
                Calculatings calculations = new Calculatings(functionPanel.list, systemB.list, countVar, countConstr);
                setPreferredSize(new Dimension(1024, (478)*(calculations.iterator_matrix+2)));
                if (matrixPanel!= null) matrixPanel.setVisible(false);
                matrixPanel = new MatrixPanel(calculations.iterator_matrix, countVar, countConstr);
                matrixPanel.setVisible(true);
                add(matrixPanel);
            }
        });
    }
}
