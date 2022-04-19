import javax.naming.CompositeName;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Calculatings {

    //Данные массивы реализуют изменения в шапках матрицы - в C(j) и C(в)
    Double[] ChangesHorizontal;
    Double[] ChangesVertical;


    //Класс реализует вычисления по итерациям матриц, пока не будет получено оптимальное решение
    //Все элементы новых матриц записываются в абсолютно чистый текстовый файл, где также будут указываться координаты
    //разрешающих элементов матриц и оптимальное решение. Также, для наглядности реализованы вычисления в письменной форме
    //и проверка оптимальности, которые будут записаны в конец файла.
    void Processing(ArrayList<Double> Function, Double[][] System, int count, int constr){
        try {
            ChangesHorizontal = new Double[count]; ChangesVertical = new Double[constr];
            //Заполняем вертикаль нулями
            Arrays.fill(ChangesVertical, 0.0);
            for (int i =0; i<count; i++){
                ChangesHorizontal[i] = Function.get(i);
            }


            Path file = Path.of("src\\log.txt");
            if (Files.exists(file))
                Files.delete(file);
            Files.createFile(Path.of("src\\log.txt"));

            //1 строка матрицы
            StringBuilder temp = new StringBuilder();
            for (int i =0; i<count; i++){
                temp.append(ChangesHorizontal[i]).append(" ");
            }
            Files.write(file, Collections.singleton("X C(j) " + temp), StandardOpenOption.APPEND);

            //2 строка матрицы
            temp = new StringBuilder("");
            for (int i =0; i<count; i++){
                temp.append("x").append(i+1).append(" ");
            } temp.append("A(0)");
            Files.write(file, Collections.singleton("C(в) X " + temp), StandardOpenOption.APPEND);


            //3+ строки матрицы
            for (int i =0; i<constr; i++){
                temp = new StringBuilder("");
                temp.append(ChangesVertical[i]).append(" x").append(count + 1 + i).append(" ");
                for (int j =0; j<count; j++){
                    temp.append(System[i][j]).append(" ");
                }
                Files.write(file, Collections.singleton(temp), StandardOpenOption.APPEND);
            }

            //f строка
            temp = new StringBuilder("X F ");
            double tempF;
            for (int i=0; i<=count; i++){
                tempF = 0.0;
                for (int j =0; j <constr; j++){
                    tempF += System[j][i]*ChangesVertical[j];
                }
                if (i!=count) tempF -= ChangesHorizontal[i];
                temp.append(tempF).append(" ");

            }
            Files.write(file, Collections.singleton(temp), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
