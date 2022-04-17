import javax.swing.*;
import java.awt.*;

public class GlobalPanel extends JPanel {
    SettingPanel setUp = new SettingPanel();
    SystemPanel systemB = new SystemPanel();
    MatrixPanel matrix = new MatrixPanel();


    GlobalPanel(){
        setPreferredSize(new Dimension(1024, 800));
        setLayout(null);


        //Настраиваем и отображаем окно настроек
        setUp.Processing();

        add(setUp);
        add(systemB);
        add(matrix);
    }
}
