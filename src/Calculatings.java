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
            Double[] F_String = new Double[count+1];


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
                for (int j =0; j<=count; j++){
                    temp.append(System[i][j]).append(" ");
                }
                Files.write(file, Collections.singleton(temp), StandardOpenOption.APPEND);
            }

            //f строка
            temp = new StringBuilder("X F ");
            for (int i=0; i<=count; i++){
                F_String[i] = 0.0;
                for (int j =0; j <constr; j++){
                    F_String[i] += System[j][i]*ChangesVertical[j];
                }
                if (i!=count) F_String[i] -= ChangesHorizontal[i];
                temp.append(F_String[i]).append(" ");
            }
            Files.write(file, Collections.singleton(temp), StandardOpenOption.APPEND); //После этой строки исходная матрица готова

            //Составляем остальные матрицы, пока не дойдем до оптимального решения
            //На выход получаем массив строк, который записываем в файл
            ArrayList <String> ALL_MATRIX = MatrixCreate (F_String, ChangesHorizontal, ChangesVertical, System, count, constr);
            for(int i =0; i<ALL_MATRIX.size(); i++){
                Files.write(file, Collections.singleton(ALL_MATRIX.get(i)), StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Функция вычисления остальных матриц и оптимального решения
    ArrayList <String> MatrixCreate (Double[] F_string, Double[] ChangesHorizontal, Double[] ChangesVertical, Double[][] System, int count, int constr){
        boolean result = false;
        ArrayList <String> ALL_MATRIX = new ArrayList<>();
        int iterator_matrix = 0, it_str =0;

        int XCord = 0, YCord = 0;
        double RE;

        //Пока не будет получен результат или, пока не будут перебраны все возможные итерации
        while (!result && iterator_matrix<count){
            boolean result_temp = true;
            Double tempArr;
            RE = 1000000000;
            //Находим разрешающий элемент и записываем в буфер для файла:
            for (int i =0; i<constr; i++){
                tempArr = System[i][count]/System[i][iterator_matrix];
                if (tempArr<RE){
                    RE = tempArr;
                    //Определяем координаты разрешающего элемента матрицы
                    XCord = i;
                    YCord =iterator_matrix;
                }
            }
            RE = System[XCord][YCord];
            ALL_MATRIX.add(it_str, "RE_cords = " + XCord + " " + YCord +" RE = "+ RE); it_str++;
            ALL_MATRIX.add(it_str, "Итерация " + iterator_matrix); it_str ++;

            Double temp = ChangesHorizontal[YCord];
            ChangesHorizontal[YCord] = ChangesVertical[XCord];
            ChangesVertical[XCord] = temp;

            Double RE_new = 1/RE;
            //Делаем копию таблицы предыдущей итерации или исходной матрицы, если итерация = 0
            //Основная таблица будет изменяться только после составления новой матрицы
            Double[][] Clone = new Double[constr][count+1];
            for(int i=0;i<constr; i++){
                for(int j=0; j<=count; j++) Clone [i][j] = System[i][j];
            }

            //Вычисляем разрешающую строку
            for (int i =0; i<=count; i++){
                if (i!= YCord)
                Clone[XCord][i] = System[XCord][i]/RE;
            }
            //Вычисляем разрешающий столбец
            for (int i=0;i<constr; i++){
                if (i!= XCord)
                Clone[i][YCord] = -System[i][YCord]/RE;
            }
            Clone[XCord][YCord] = RE_new;

            //Далее высчитываем все остальные элементы матрицы
            for (int i =0; i<constr; i++){
                for (int j=0;j<=count;j++ ){
                    if (i!=XCord && j!=YCord){
                        Clone[i][j] = ((System[i][j]*RE)-(System[i][YCord]*System[XCord][j]))/RE;
                    }
                }
            }

            //Повторяем для F строки:
            Double [] F_string_clone = new Double[count+1];
            for (int i =0; i<=count; i++) F_string_clone[i] = F_string[i];

            for (int i =0; i<count; i++)
            {
                if (i!=YCord){
                    F_string_clone[i] = ((F_string[i]*RE)-(System[XCord][i]*(-F_string[YCord]/RE)))/RE;
                }else F_string_clone[i] /= -RE;
                if (F_string[i]<0) result_temp = false;
            }
            F_string_clone[count] = 0.0;

            //Вычисляем сам результат:
            for (int i =0; i<constr; i++){
                F_string_clone[count] += ChangesVertical[i]*System[i][count];
            }
            //Записываем строки в буфер:
            //1 строка матрицы
            StringBuilder buff = new StringBuilder();
            for (int i =0; i<count; i++){
                buff.append(ChangesHorizontal[i]).append(" ");
            }
            ALL_MATRIX.add(it_str, "X C(j) " + buff); it_str++;

            //2 строка матрицы
            buff = new StringBuilder("");
            for (int i =0; i<count; i++){
                buff.append("x").append(i+1).append(" ");
            } buff.append("A(0)");
            ALL_MATRIX.add(it_str, "C(в) X " + buff); it_str++;

            //3+ строки матрицы
            for (int i =0; i<constr; i++){
                buff = new StringBuilder("");
                buff.append(ChangesVertical[i]).append(" x").append(count + 1 + i).append(" ");
                for (int j =0; j<=count; j++){
                    buff.append(Clone[i][j]).append(" ");
                }
                ALL_MATRIX.add(it_str, String.valueOf(buff)); it_str++;
            }

            //f строка
            buff = new StringBuilder("X F ");
            for (int i=0; i<=count; i++){
                F_string_clone[i] = 0.0;
                for (int j =0; j <constr; j++){
                    F_string_clone[i] += Clone[j][i]*ChangesVertical[j];
                }
                if (i!=count) F_string_clone[i] -= ChangesHorizontal[i];
                buff.append(F_string_clone[i]).append(" ");
            }
            ALL_MATRIX.add(it_str, String.valueOf(buff)); it_str++;
            for (int i =0; i<=count; i++) F_string[i] = F_string_clone[i];

            //Переходим к другой матрице
            if (!result_temp){
                iterator_matrix++;
                for(int i=0;i<constr; i++){
                    for(int j=0; j<=count; j++) System [i][j] = Clone[i][j];
                }
            } else {
                result = true;
                ALL_MATRIX.add(it_str, "Result: "+F_string[count]); it_str++;
            }
        }
        return ALL_MATRIX;
    }
}
