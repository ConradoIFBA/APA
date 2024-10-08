//Author Conrado

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Funcao para contar a ocorrencia de uma substring em uma string
// Essa funcao percorre a string 'str' e conta quantas vezes a substring 'word' aparece nela
int countOccurrences(const char *str, const char *word) {
    int count = 0;
    const char *temp = str;
    while ((temp = strstr(temp, word)) != NULL) {
        count++;
        temp += strlen(word); // Avança a posição para evitar contagens repetidas no mesmo local
    }
    return count;
}

// Funcao para verificar se uma linha contem o inicio de uma funcao
// Verifica se a linha contem parenteses '(' e ')' e uma chave '{' que indicam uma declaracao de funcao
int isFunctionDeclaration(const char *line) {
    return (strstr(line, "(") != NULL && strstr(line, ")") != NULL && strstr(line, "{") != NULL);
}

// Funcao para obter o nome da funcao de uma linha que contem uma declaracao de funcao
// Extrai a primeira palavra da linha como nome da funcao (considerando um formato de declaracao tipico)
void getFunctionName(const char *line, char *functionName) {
    sscanf(line, "%s", functionName);  // Extrai a primeira palavra da linha como o nome da funcao
}

// Funcao para detectar padroes que podem levar a uma complexidade fatorial
// Verifica se a linha contem padroes que sugerem a presenca de operacoes fatoriais ou recursao
int isFactorialPattern(const char *line) {
    // Detecta padroes que indicam operacoes fatoriais (decrementos em loops) ou chamadas recursivas especificas
    return (strstr(line, "for") != NULL && strstr(line, "decrement") != NULL) || 
           (strstr(line, "n * (n - 1)") != NULL) || 
           (strstr(line, "*=") != NULL) || 
           (strstr(line, "recursive call") != NULL);
}

int main() {
    FILE *arquivo;
    char linha[1000]; // Buffer para armazenar cada linha do arquivo

    // Abre o arquivo contendo o codigo que sera analisado
    arquivo = fopen("algoritmo.cpp", "r");
    if (arquivo == NULL) {
        printf("Erro ao abrir o arquivo.\n");
        return 1; // Retorna erro se nao for possivel abrir o arquivo
    }

    // Inicializacao de variaveis para armazenar contagens e estados durante a analise
    int totalSteps = 0; // Contador de passos (linhas validas)
    int loopCount = 0; // Numero total de loops encontrados
    int nestedLoopDepth = 0; // Profundidade de loops aninhados
    int currentLoopDepth = 0; // Profundidade atual de aninhamento de loops
    int maxNestedLoopDepth = 0; // Profundidade maxima de aninhamento de loops
    int printfCount = 0; // Contador de chamadas printf e scanf
    int variableCount = 0; // Contador de declaracoes de variaveis
    int recursiveFunctionCount = 0; // Contador de funcoes recursivas
    int factorialPatternDetected = 0; // Indicador de deteccao de padrao fatorial
    char currentFunction[50] = ""; // Nome da funcao atualmente sendo analisada
    char functionName[50]; // Buffer para armazenar o nome da funcao

    // Loop para ler cada linha do arquivo e analisar o conteudo
    while (fgets(linha, sizeof(linha), arquivo)) {
        // Ignora linhas em branco e comentarios para contagem de passos
        if (strlen(linha) > 1 && linha[0] != '/') {
            totalSteps++; // Cada linha valida e considerada um passo
        }

        // Verifica se a linha contem uma declaracao de funcao
        if (isFunctionDeclaration(linha)) {
            getFunctionName(linha, currentFunction); // Extrai e armazena o nome da funcao
        }

        // Conta loops e calcula a profundidade de aninhamento de loops
        int foundLoop = countOccurrences(linha, "for") + countOccurrences(linha, "while");
        if (foundLoop > 0) {
            loopCount += foundLoop; // Adiciona ao total de loops encontrados
            currentLoopDepth++; // Aumenta a profundidade de aninhamento
            if (currentLoopDepth > maxNestedLoopDepth) {
                maxNestedLoopDepth = currentLoopDepth; // Atualiza a profundidade maxima se necessario
            }
        }

        // Verifica se saiu de um bloco de loop para ajustar a profundidade de aninhamento
        if (strstr(linha, "}") != NULL && currentLoopDepth > 0) {
            currentLoopDepth--; // Reduz a profundidade ao sair de um bloco de codigo
        }

        // Verifica se a funcao atual esta chamando a si mesma (recursao)
        if (strlen(currentFunction) > 0 && strstr(linha, currentFunction) != NULL) {
            recursiveFunctionCount++; // Incrementa o contador de funcoes recursivas
        }

        // Detecta padroes que sugerem uma complexidade fatorial
        if (isFactorialPattern(linha)) {
            factorialPatternDetected = 1; // Marca a deteccao de um padrao fatorial
        }

        // Conta ocorrencias de printf e scanf (para entrada e saida de dados)
        printfCount += countOccurrences(linha, "printf");
        printfCount += countOccurrences(linha, "scanf");

        // Conta declaracoes de variaveis simples (tipos basicos)
        variableCount += countOccurrences(linha, "int ");
        variableCount += countOccurrences(linha, "float ");
        variableCount += countOccurrences(linha, "char ");
        variableCount += countOccurrences(linha, "double ");
    }

    // Fecha o arquivo apos a leitura completa
    fclose(arquivo);

    // Estima a complexidade assintotica do codigo analisado
    char bigO[50];
    if (factorialPatternDetected) {
        strcpy(bigO, "O(n!) (potencial fatorial)"); // Caso seja detectada complexidade fatorial
    } else if (maxNestedLoopDepth > 1) {
        sprintf(bigO, "O(n^%d)", maxNestedLoopDepth); // Complexidade polinomial com base na profundidade de aninhamento
    } else if (loopCount > 0) {
        strcpy(bigO, "O(n) ou pior"); // Presenca de pelo menos um loop indica complexidade linear
    } else if (recursiveFunctionCount > 0) {
        strcpy(bigO, "O(n) (possivel recursao)"); // Complexidade potencialmente linear ou exponencial se houver recursao
    } else {
        strcpy(bigO, "O(1)"); // Se nao houver loops ou recursao, assume complexidade constante
    }

    // Exibe os resultados da analise na tela
    printf("Total de passos: %d\n", totalSteps);
    printf("Quantidade de loops: %d\n", loopCount);
    printf("Profundidade maxima de aninhamento de loops: %d\n", maxNestedLoopDepth);
    printf("Quantidade de funcoes recursivas: %d\n", recursiveFunctionCount);
    printf("Quantidade de printf/scanf: %d\n", printfCount);
    printf("Quantidade de variaveis declaradas: %d\n", variableCount);
    printf("Complexidade estimada (Big O): %s\n", bigO);

    return 0; // Fim 
}

