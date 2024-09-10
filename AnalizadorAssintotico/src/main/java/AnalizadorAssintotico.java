import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorAssintotico {

    
    public static String analisarComplexidade(String codigoFonte) {
        
        int profundidadeLoop = contarLoops(codigoFonte);
        boolean contemRecursao = contemRecursao(codigoFonte);
        boolean contemLogaritmo = contemLogaritmo(codigoFonte);
        boolean contemExponencial = contemExponencial(codigoFonte);
        boolean contemFatorial = contemFatorial(codigoFonte);
        
        if (contemFatorial) {
            return "O algoritmo parece ser O(n!)";
        } else if (contemExponencial) {
            return "O algoritmo parece ser O(2^n)";
        } else if (contemLogaritmo) {
            return "O algoritmo parece ser O(log n)";
        } else if (contemRecursao) {
            return "O comportamento do algoritmo parece recursivo, pode ser O(T(n))";
        } else if (profundidadeLoop == 1) {
            return "O algoritmo parece ser O(n), pois contém um loop simples";
        } else if (profundidadeLoop == 2) {
            return "O algoritmo parece ser O(n^2), pois contém dois loops aninhados";
        } else if (profundidadeLoop > 2) {
            return "O algoritmo parece ser O(n^" + profundidadeLoop + "), pois contém múltiplos loops aninhados";
        } else {
            return "O algoritmo parece ser constante O(1)";
        }
    }

    
    public static int contarLoops(String codigoFonte) {
        // Contando "for" e "while" no código
        Pattern patternFor = Pattern.compile("for\\s*\\(");
        Pattern patternWhile = Pattern.compile("while\\s*\\(");

        Matcher matcherFor = patternFor.matcher(codigoFonte);
        Matcher matcherWhile = patternWhile.matcher(codigoFonte);

        int contadorFor = 0;
        int contadorWhile = 0;
        
   
        while (matcherFor.find()) {
            contadorFor++;
        }
        while (matcherWhile.find()) {
            contadorWhile++;
        }

        // Supondo que cada loop conta como um nível de profundidade
        return contadorFor + contadorWhile;
    }

    //verificar se ah recursão no código
    public static boolean contemRecursao(String codigoFonte) {
        // Verificando se o método chama a si mesmo
        Pattern patternRecursao = Pattern.compile("executar\\s*\\(");
        Matcher matcher = patternRecursao.matcher(codigoFonte);
        return matcher.find();
    }

    //verificar se ah operações logarítmicas
    public static boolean contemLogaritmo(String codigoFonte) {
        // Verificando a presença de "log" ou padrões comuns como divisão por 2
        Pattern patternLog = Pattern.compile("log|n\\s*/\\s*2");
        Matcher matcher = patternLog.matcher(codigoFonte);
        return matcher.find();
    }

    // verificar se há operações exponenciais
    public static boolean contemExponencial(String codigoFonte) {
        // Verificando a presença de potências de 2, 3, etc.
        Pattern patternExp = Pattern.compile("2\\s*\\^|Math\\.pow\\s*\\(");
        Matcher matcher = patternExp.matcher(codigoFonte);
        return matcher.find();
    }

    // verificar se há operações fatoriais
    public static boolean contemFatorial(String codigoFonte) {
        // Verificando a presença de fatorial "n!"
        Pattern patternFat = Pattern.compile("n\\s*!|fatorial");
        Matcher matcher = patternFat.matcher(codigoFonte);
        return matcher.find();
    }


    public static void main(String[] args) {
        // codigo fonte para teste
        String codigoFonte = 
                "// Exemplo O(1)\n" +
"int constante(int n) {\n" +
"    return n + 10;  // Operação constante\n" +
"}}";


        String complexidade = analisarComplexidade(codigoFonte);
        System.out.println("Complexidade estimada: " + complexidade);
    }
}
