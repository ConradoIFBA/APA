import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorAssintotico {

    public static String analisarComplexidade(String codigoFonte) {
        
        int profundidadeLoop = contarLoops(codigoFonte);
        boolean contemRecursao = contemRecursao(codigoFonte);
        boolean contemLogaritmo = contemLogaritmo(codigoFonte);
        boolean contemExponencial = contemExponencial(codigoFonte);
        boolean contemFatorial = contemFatorial(codigoFonte);

        String melhorCaso = "";
        String piorCaso = "";

        if (contemFatorial) {
            piorCaso = "O(n!)";
            melhorCaso = "O(n)";
        } else if (contemExponencial) {
            piorCaso = "O(2^n)";
            melhorCaso = "O(n)";
        } else if (contemLogaritmo) {
            piorCaso = "O(n log n)";
            melhorCaso = "O(log n)";
        } else if (contemRecursao) {
            piorCaso = "O(T(n))";
            melhorCaso = "O(1)";
        } else if (profundidadeLoop == 1) {
            piorCaso = "O(n)";
            melhorCaso = "O(1)";
        } else if (profundidadeLoop == 2) {
            piorCaso = "O(n^2)";
            melhorCaso = "O(n)";
        } else if (profundidadeLoop > 2) {
            piorCaso = "O(n^" + profundidadeLoop + ")";
            melhorCaso = "O(n^" + (profundidadeLoop - 1) + ")";
        } else {
            piorCaso = "O(1)";
            melhorCaso = "O(1)";
        }

        return "Melhor caso: " + melhorCaso + ", Pior caso: " + piorCaso;
    }

    public static int contarLoops(String codigoFonte) {
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

        return contadorFor + contadorWhile;
    }

    public static boolean contemRecursao(String codigoFonte) {
        Pattern patternRecursao = Pattern.compile("executar\\s*\\(");
        Matcher matcher = patternRecursao.matcher(codigoFonte);
        return matcher.find();
    }

    public static boolean contemLogaritmo(String codigoFonte) {
        Pattern patternLog = Pattern.compile("log|n\\s*/\\s*2");
        Matcher matcher = patternLog.matcher(codigoFonte);
        return matcher.find();
    }

    public static boolean contemExponencial(String codigoFonte) {
        Pattern patternExp = Pattern.compile("2\\s*\\^|Math\\.pow\\s*\\(");
        Matcher matcher = patternExp.matcher(codigoFonte);
        return matcher.find();
    }

    public static boolean contemFatorial(String codigoFonte) {
        Pattern patternFat = Pattern.compile("n\\s*!|fatorial");
        Matcher matcher = patternFat.matcher(codigoFonte);
        return matcher.find();
    }

    public static void main(String[] args) {
        String codigoFonte = 
                "// Exemplo O(1)\n" +
                "int constante(int n) {\n" +
                "    return n + 10;  // Operação constante\n" +
                "}";

        String complexidade = analisarComplexidade(codigoFonte);
        System.out.println("Complexidade estimada: " + complexidade);
    }
}
