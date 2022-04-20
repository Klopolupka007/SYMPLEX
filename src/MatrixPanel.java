import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MatrixPanel extends JPanel {
    BufferedReader reader;
    {
        try {
            reader = new BufferedReader(new FileReader("src/log.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    MatrixPanel(int iter_matrix, int count, int constr){
        setSize(965, 315*(iter_matrix+2));
        setBackground(ColorFont.colorPanel);
        setLocation(20,480);
        setLayout(null);

        for (int i=0; i<=iter_matrix+1; i++){
            Matrix matrix = new Matrix(reader, count, i, constr, iter_matrix+1);
            add(matrix);
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Matrix extends JPanel{
    Matrix(BufferedReader reader, int count, int iter_m, int constr, int iter_matrix){
        setSize(945, 295);
        setLocation(10,10+iter_m*310);
        setLayout(new GridLayout(0, count+3, 5, 5));
        setBackground(ColorFont.colorPanel);

        String symbol = ""; char buff;
        StringBuilder finalStr;
        JLabel[][] cells  = new JLabel[(3+constr)][(3+count)];
        for (int i =0; i<3+constr; i++){
            for (int j =0; j<count+3; j++){
                finalStr = new StringBuilder("");
                cells[i][j] = new JLabel();
                cells[i][j].setSize(40,30);
                cells[i][j].setFont(ColorFont.simple);
                cells[i][j].setOpaque(true);
                cells[i][j].setBackground(ColorFont.colorCells);
                try {
                     do {
                        symbol = String.valueOf(reader.read());
                        buff = (char) Integer.parseInt(symbol);
                        if(buff == '\n') { symbol = "32"; }
                        else if (buff == '\r') buff = ' ';
                        if (!symbol.equals("32"))finalStr.append(buff);
                    } while (!symbol.equals("32"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (String.valueOf(finalStr).equals("X") || String.valueOf(finalStr).equals("X ")) {
                    finalStr = new StringBuilder("");
                }
                cells[i][j].setText(String.valueOf(finalStr));
                cells[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                add(cells[i][j]);
            }
        }

        for (int i = 2; i<constr+3; i++) for (int j =2; j<count+3; j++) cells[i][j].setBackground(ColorFont.colorCells1);
        for(int i =0; i<constr+3; i++){ for (int j = 0; j<count+3; j++) { cells[0][j].setBackground(ColorFont.colorCells2); cells[i][0].setBackground(ColorFont.colorCells2);} }


        int raw =0, col = 0;
        if (iter_m!=iter_matrix){
                try {
                    for (int i=0; i<3; i++) {
                        symbol = String.valueOf(reader.read());
                        if (!symbol.equals("32")) {
                            buff = (char) Integer.parseInt(symbol);
                            if (i == 0) raw = Integer.parseInt(String.valueOf(buff));
                            else col = Integer.parseInt(String.valueOf(buff));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i=2; i<(3+constr); i++){
                    for (int j=2; j<(3+count); j++){
                        cells[i][col+2].setBackground(ColorFont.colorLightGreen);
                        cells[raw+2][j].setBackground(ColorFont.colorLightGreen);
                    }
                }
                cells[raw+2][col+2].setBackground(ColorFont.colorGreen);
            try {
                symbol = String.valueOf(reader.read());
                symbol = String.valueOf(reader.read());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cells[2+constr][2+count].setBackground(ColorFont.colorGreen);
        }
    }
}