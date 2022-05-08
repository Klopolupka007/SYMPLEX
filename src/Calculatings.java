import javax.naming.CompositeName;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Calculatings {

    //Данные массивы реализуют изменения в шапках матрицы - в C(j) и C(в)
    Double[] ChangesHorizontal;
    Double[] ChangesVertical;

    int iterator_matrix=0;
    //MathContext context = new MathContext(2, RoundingMode.HALF_UP);
    //Класс реализует вычисления по итерациям матриц, пока не будет получено оптимальное решение
    //Все элементы новых матриц записываются в абсолютно чистый текстовый файл, где также будут указываться координаты
    //разрешающих элементов матриц и оптимальное решение. Также, для наглядности реализованы вычисления в письменной форме
    //и проверка оптимальности, которые будут записаны в конец файла.

    Calculatings(ArrayList<Double> Function, Double[][] System, int count, int constr){
        Path file = Path.of("src\\log.txt");
        try {
            ChangesHorizontal = new Double[count]; ChangesVertical = new Double[constr];
            Double[] F_String = new Double[count+1];
            //Заполняем вертикаль нулями
            Arrays.fill(ChangesVertical, 0.0);
            for (int i =0; i<count; i++){
                ChangesHorizontal[i] = Function.get(i);
            }
            if (Files.exists(file))
                Files.delete(file);
            Files.createFile(Path.of("src\\log.txt"));

            //1 строка матрицы
            StringBuilder temp = new StringBuilder();
            for (int i =0; i<count; i++){
                temp.append(ChangesHorizontal[i]).append(" ");
            }
            Files.write(file, Collections.singleton("X C(j) " + temp + "X"));

            //2 строка матрицы
            temp = new StringBuilder("");
            for (int i =0; i<count; i++){
                temp.append("x").append(i+1).append(" ");
            } temp.append("A(0)");
            Files.write(file, Collections.singleton("C(в) X " + temp), StandardOpenOption.APPEND);


            //3+ строки матрицы
            for (int i =0; i<constr; i++){
                temp = new StringBuilder("");
                temp.append(ChangesVertical[i]).append(" x").append(count + 1 + i);
                for (int j =0; j<=count; j++){
                    temp.append(" ").append(System[i][j]);
                }
                Files.write(file, Collections.singleton(temp), StandardOpenOption.APPEND);
            }

            //f строка
            temp = new StringBuilder("X F");
            for (int i=0; i<=count; i++){
                F_String[i] = 0.0;
                for (int j =0; j <constr; j++){
                    F_String[i] += System[j][i]*ChangesVertical[j];
                }
                if (i!=count) F_String[i] -= ChangesHorizontal[i];
                temp.append(" ").append(F_String[i]);
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

    //Следующие массивы для хранения изменений в порядке переменных вертикальной и горизонтальных шапок Cj и Cв
    //НАСЛЕДУЮТСЯ В КЛАСС ДЛЯ ДВОЙСТВЕННОЙ ЗАДАЧИ
    int [] horizontalVar, verticalVar;

    //Функция вычисления остальных матриц и оптимального решения
    ArrayList <String> MatrixCreate (Double[] F_string, Double[] ChangesHorizontal, Double[] ChangesVertical, Double[][] System, int count, int constr){

        horizontalVar = new int [count];
        verticalVar = new int[constr];

        //Заполняем horizontalVar
        for (int i =0; i<count; i++){
            horizontalVar[i] = i+1;
        }
        //Заполняем verticalVar
        for (int i=0;i<constr;i++){
            verticalVar[i] = count+i+1;
        }

        boolean result = false;
        ArrayList <String> ALL_MATRIX = new ArrayList<>();
        iterator_matrix = 0; int it_str =0;

        int XCord = 0, YCord = 0;
        double RE;

        //Пока не будет получен результат или, пока не будут перебраны все возможные итерации
        while (!result && iterator_matrix<count){
            boolean result_temp = true;
            Double tempArr;
            RE = 1000000000;
            int indxTable = searchIndexTable(count, F_string);
            //Находим разрешающий элемент и записываем в буфер для файла:
            for (int i =0; i<constr; i++){
                tempArr = System[i][count]/System[i][indxTable];
                if (tempArr<RE && tempArr>0){
                    RE = tempArr;
                    //Определяем координаты разрешающего элемента матрицы
                    XCord = i;
                    YCord =indxTable;
                }
            }
            RE = System[XCord][YCord];
            ALL_MATRIX.add(it_str, XCord + " " + YCord); it_str++;

            Double temp = ChangesHorizontal[YCord];
            ChangesHorizontal[YCord] = ChangesVertical[XCord];
            ChangesVertical[XCord] = temp;

            //Меняем порядок переменных Св и Cj:
            int tempVar = horizontalVar[YCord];
            horizontalVar[YCord] = verticalVar[XCord];
            verticalVar[XCord] = tempVar;


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
                Clone[i][YCord] = (-System[i][YCord]/RE);
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
                if (F_string_clone[i]<0) result_temp = false;
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
            ALL_MATRIX.add(it_str, "X C(j) " + buff + "X"); it_str++;

            //2 строка матрицы
            buff = new StringBuilder("");
            for (int i =0; i<count; i++){
                buff.append("x").append(horizontalVar[i]).append(" ");
            } buff.append("A(0)");
            ALL_MATRIX.add(it_str, "C(в) X " + buff); it_str++;

            //3+ строки матрицы
            for (int i =0; i<constr; i++){
                buff = new StringBuilder("");
                buff.append(ChangesVertical[i]).append(" x").append(verticalVar[i]);
                for (int j =0; j<=count; j++){
                    buff.append(" ").append(((int) (Clone[i][j]*1000))/1000.0);
                }
                ALL_MATRIX.add(it_str, String.valueOf(buff)); it_str++;
            }

            //f строка
            buff = new StringBuilder("X F");
            for (int i=0; i<=count; i++){
                F_string_clone[i] = 0.0;
                for (int j =0; j <constr; j++){
                    F_string_clone[i] += Clone[j][i]*ChangesVertical[j];
                }
                if (i!=count) F_string_clone[i] -= ChangesHorizontal[i];
                buff.append(" ").append(((int) (F_string_clone[i]*1000))/1000.0);
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
                ALL_MATRIX.add(it_str, "Result: "+((int) (F_string[count]*1000))/1000.0); it_str++;
            }
        }
        if (iterator_matrix == count) iterator_matrix--;
        return ALL_MATRIX;
    }

    int searchIndexTable(int count, Double[] F_string){
        double temp =-1.0; int j=0;
        for (int i =0; i<count; i++){
            if (Math.abs(F_string[i])>temp && F_string[i]<0){
                temp = Math.abs(F_string[i]);
                j=i;
            }
        }
        return j;
    }
}
