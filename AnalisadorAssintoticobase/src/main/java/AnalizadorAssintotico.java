import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorAssintotico {

    // Função para analisar a complexidade assintótica
    public static String analisarComplexidade(String codigoFonte) {
        // Conta loops aninhados e recursão no código fonte
        int profundidadeLoop = contarLoops(codigoFonte);
        boolean contemRecursao = contemRecursao(codigoFonte);
        
        if (contemRecursao) {
            return "O comportamento do algoritmo parece recursivo, pode ser O(T(n)) dependendo da profundidade e das condições de parada.";
        } else if (profundidadeLoop == 1) {
            return "O algoritmo parece ser O(n), pois contém um loop simples.";
        } else if (profundidadeLoop == 2) {
            return "O algoritmo parece ser O(n^2), pois contém dois loops aninhados.";
        } else if (profundidadeLoop > 2) {
            return "O algoritmo parece ser O(n^" + profundidadeLoop + "), pois contém múltiplos loops aninhados.";
        } else {
            return "Não foi possível determinar a complexidade. O algoritmo pode ser constante O(1) ou baseado em operações simples.";
        }
    }

    // Função para contar loops no código-fonte
    public static int contarLoops(String codigoFonte) {
        // Contando "for" e "while" no código
        Pattern patternFor = Pattern.compile("for\\s*\\(");
        Pattern patternWhile = Pattern.compile("while\\s*\\(");

        Matcher matcherFor = patternFor.matcher(codigoFonte);
        Matcher matcherWhile = patternWhile.matcher(codigoFonte);

        int contadorFor = 0;
        int contadorWhile = 0;
        
        // Contagem de loops
        while (matcherFor.find()) {
            contadorFor++;
        }
        while (matcherWhile.find()) {
            contadorWhile++;
        }

        // Supondo que cada loop conta como um nível de profundidade
        return contadorFor + contadorWhile;
    }

    // Função para verificar se há recursão no código
    public static boolean contemRecursao(String codigoFonte) {
        // Verificando se o método chama a si mesmo
        Pattern patternRecursao = Pattern.compile("executar\\s*\\(");
        Matcher matcher = patternRecursao.matcher(codigoFonte);
        return matcher.find();
    }

    // Função para ler o conteúdo de um arquivo de texto
    public static String lerArquivo(String caminhoArquivo) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));
        String linha;
        while ((linha = reader.readLine()) != null) {
            conteudo.append(linha).append("\n");
        }
        reader.close();
        return conteudo.toString();
    }

    // Método principal para testar
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, forneça o caminho para o arquivo de código.");
            return;
        }

        String caminhoArquivo = args[0];

        try {
            // Ler o código-fonte de um arquivo txt
            String codigoFonte = lerArquivo(caminhoArquivo);

            // Analisar a complexidade do código fornecido
            String complexidade = analisarComplexidade(codigoFonte);
            System.out.println("Complexidade estimada: " + complexidade);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
