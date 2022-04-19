import javax.swing.*;

public class Main extends JFrame {
    Main(){
        setTitle("Symplex method – выполнил Панкрухин Максим – ИКБО-04-20");
        setSize(1040, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JScrollPane scroll = new JScrollPane(new GlobalPanel());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setSize(1040, 520);
        add(scroll);

        setVisible(true);
    }

    /*
                                            Небольшое описание программы.
        Программа состоит из графического окна, включающее в себя 4 главные панели, на которых происходит все
   взаимодействие с пользователем и, соответственно, вся функциональная часть симплексного метода спрятана в код.
   ______________________________________________________________________________________________
   |  ________________     __________________________________________________________________   |
   |  | Настройки    |     |  Целевая функция:  k(1)*x(1)+k(2)*x(2)+...+k(N)*x(N)           |   |
   |  | Переменные=V |     |  Канонический вид:                                             |   |
   |  | Ограничения=B|     |  k(1)*x(1)+k(2)*x(2)+...+k(N)*x(N)+0*x(N+1)+...+0*x(N+B)       |   |
   |  |______________|     |________________________________________________________________|   |
   |  _______________________________________________________________________________________   |
   |  |           Система ограничений:            СО в каноническом виде:                   |   |
   |  | Огр.1: k(1)*x(1)+...+k(N)*x(N)<=...  k(1)*x(1)+...+k(N)*x(N)+x(N+1)+...+x(N+B)=...  |   |
   |  | Огр.2: k(1)*x(1)+...+k(N)*x(N)<=...  k(1)*x(1)+...+k(N)*x(N)+x(N+1)+...+x(N+B)=...  |   |
   |  |        .                             .                                              |   |
   |  |        .                             .                                              |   |
   |  |        .                             .                                              |   |
   |  | Огр.B: k(1)*x(1)+...+k(N)*x(N)<=...  k(1)*x(1)+...+k(N)*x(N)+x(N+1)+...+x(N+B)=...  |   |
   |  |_____________________________________________________________________________________|   |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |                                                                                            |
   |____________________________________________________________________________________________|
    */
    public static void main(String[] args) {
        new Main();
    }
}
