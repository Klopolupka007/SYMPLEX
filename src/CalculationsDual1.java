import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CalculationsDual1 {
    int count, constr;
    Double[][] SysDual, System; Double[] Function; int [] verticalVar;

    CalculationsDual1 (int count, int constr, ArrayList<Double> Function, Double[][] System, int [] verticalVar){
        this.constr = constr;
        this.count = count;
        this.verticalVar = new int[constr];
        this.System = System;
        this.Function = new Double[count];
        for(int i=0; i<constr; i++) this.verticalVar[i] = verticalVar[i];
        for (int i =0; i<count; i++) {
            this.Function[i] = Function.get(i);
        }
        /*
        Path file = Path.of("src\\log.txt");
        try {
            Files.createFile(Path.of(String.valueOf(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Proccessing();
    }

    //Основной метод вычислений
    void Proccessing (){
        //Этап 1 – обращение системы ограничений и целевой функции
        InvMatrix();
    }

    //Метод транспонирования матрицы
    void InvMatrix(){
        int N = 0;
        if (constr != count) {
            if (constr > count) N = constr;
            else N = count;
        }
        else N = count;
        SysDual = new Double[N][N];
        Double[][] SysDualTemp = new Double[N][N];

        //Заполняем единичной матрицей основную матрицу
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) SysDual[i][j] = 1.0;
                else  SysDual[i][j] = 0.0;
            }
        }

        //Построение матрицы из компонентов векторов, входящих в оптимальный базис verticalVar
        for(int i=0; i<constr; i++) {
            for (int j = 0; j <= count; j++) {
                if (verticalVar[j]-1< count) SysDual[i][j] = System[i][verticalVar[j]-1];
            }
        }

        for (int k = 0; k < N; k++)
        {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                {
                    if (i == k && j == k)
                        SysDualTemp[i][j] = 1 / SysDual[i][j];
                    if (i == k && j != k)
                        SysDualTemp[i][j] = -SysDual[i][j] / SysDual[k][k];
                    if (i != k && j == k)
                        SysDualTemp[i][j] = SysDual[i][k] / SysDual[k][k];
                    if (i != k && j != k)
                        SysDualTemp[i][j] = SysDual[i][j] - SysDual[k][j] * SysDual[i][k] / SysDual[k][k];
                }
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++) SysDual[i][j] = SysDualTemp[i][j];
        }
        Double [] temp = new Double[N];
        Double [] result = new Double[N];
        for (int i =0;i<N; i++){
            if (verticalVar[i]-1<count) temp[i] = Function[verticalVar[i]-1];
            else temp[i] = 0.0;
            result[i] =0.0;
        }
        double res=0.0;
        for (int i =0;i<N; i++){
            for (int j=0;j<N;j++){
                result[i] += temp[j]*SysDual[j][i];
            }
            result[i]*=System[i][count];
            res+=result[i];
        }
        res = (int) (1000*res)/1000.0;
    }

}


