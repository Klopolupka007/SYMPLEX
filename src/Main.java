import javax.swing.*;

public class Main extends JFrame {
    Main(){
        setSize(1024, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        add(new GlobalPanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
