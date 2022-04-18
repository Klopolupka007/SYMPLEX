import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class Calculatings {

    //Класс реализует вычисления по итерациям матриц, пока не будет получено оптимальное решение
    //Все элементы новых матриц записываются в абсолютно чистый текстовый файл, где также будут указываться координаты
    //разрешающих элементов матриц и оптимальное решение. Также, для наглядности реализованы вычисления в письменной форме
    //и проверка оптимальности, которые будут записаны в конец файла.
    void Processing(ArrayList<Double> Function, Double[][] System, int count, int constr){
        try {
            Path file = Path.of("src\\log.txt");
            if (Files.exists(file))
                Files.delete(file);
            Files.createFile(Path.of("src\\log.txt"));
            StringBuilder temp = new StringBuilder();
            for (int i =0; i<count; i++){
                temp.append(Function.get(i)).append(" ");
            }
            Files.write(file, Collections.singleton("_ C(j) " + temp));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
