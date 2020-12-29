package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Arquivo {

    private final ArrayList<String> instrucoes = new ArrayList<>();
    private final ArrayList<String[]> arrayDeLinhas = new ArrayList<>();

    public Arquivo () {}

    public ArrayList<String> leitura_arquivos(File lerArq) {

        try {

            instrucoes.clear();
            FileReader arq = new FileReader(lerArq);
            BufferedReader bufferArq = new BufferedReader(arq);
            String linha = bufferArq.readLine(); // le a primeira linha

            while (linha != null) { // null quando acaba de ler o arquivo

                instrucoes.add(linha); // adiciona a linha no arraylist
                linha = bufferArq.readLine(); // le as demais linhas
            }

                arq.close(); // fecha o arquivo
                return instrucoes;

        } catch (IOException e) {

            //Arrumar retorno ***************************
            System.out.println("Erro leitura Arquivo");
            return null;
        }
    }

    public ArrayList<String[]> colocaMatriz () {

        for (String linha: instrucoes) {

            String[] linhaVetor = new String[5]; // Indica a linha, separando as posi��es entre instru��o, atributo 1, atributo 2 e L
            boolean flagSalvar = false; // Flag para indicar se ir� salvar a instru��o/atributo
            int controleInstrucoes = 0; // Vari�vel de controle da posi��o da linhaVetor
            StringBuilder sb = new StringBuilder(); // String que ser� formada pelos caracteres lidos, utilizando o StringBuilder

            for (int i = 0; i < linha.length(); i++) {

                char caracter = linha.charAt(i);

                if(caracter != ' ' && caracter != '\t' && caracter != ',') { // Ignora os caracteres espa�o, tab e v�rgula

                    if(i+1 != linha.length()) { // Verifica��o para n�o acessar posi��o inv�lida

                        if(linha.charAt(i+1) == ' ' || linha.charAt(i+1) == '\t' || linha.charAt(i+1) == ',') { // Se o pr�ximo caracter for espa�o, tab ou v�rgula
                            flagSalvar = true;
                        }

                        if(i == linha.length() - 1) { // Se for o �ltimo caracter

                            flagSalvar = true;
                        }
                    } 

                    else { // Se for o ultimo caracter da linha

                        flagSalvar = true;	
                    }

                    sb.append(caracter); // Concatena o caracter na string que ser� formada pelo StringBuilder

                    if (flagSalvar == true) {

                        String str = sb.toString(); // Transforma a StringBuilder em String
                        sb = new StringBuilder(); // Reseta a vari�vel sb
                        linhaVetor[controleInstrucoes] = str; // Atribui a String na devida posi��o
                        controleInstrucoes++; // Incrementa o controle dposi��o do linhaVetor
                        flagSalvar = false; // Coloca falso na flag de controle para salvar
                    }
                }

            }

                arrayDeLinhas.add(linhaVetor); // Adiciona as instru��es j� separadas no arrayDeLinhas
        }

        // Rearranja a matriz colocando o L na posi��o correta
        for (String[] linha: arrayDeLinhas) {

            if (linha[0] != null && linha[0].charAt(0) == 'L' && (linha[0].charAt(1) >= 48 && linha[0].charAt(1) <= 57)) { // Se o primeiro caractere n�o for null (linha pulada) e for L e o segundo for um n�mero entre 0 e 9

                linha[3] = linha[0]; // Colocar o L na coluna dele
                linha[0] = linha[1]; // Passa a instru��o para o campo correto
                linha[1] = null; // Coloca null na coluna do Atributo 1, onde estava a instru��o
            }
        }
        
        for (String[] linha: arrayDeLinhas) {
            
            linha[4] = comentario(linha[0]);
        }

        for(int i = 0; i < arrayDeLinhas.size(); i++) {

            if (arrayDeLinhas.get(i)[0].equals("JMP") || arrayDeLinhas.get(i)[0].equals("JMPF") || arrayDeLinhas.get(i)[0].equals("CALL")) {

                for(int j = 0; j < arrayDeLinhas.size(); j++) {

                    if(arrayDeLinhas.get(i)[1].equals(arrayDeLinhas.get(j)[3])) {

                        arrayDeLinhas.get(i)[1] = String.valueOf(j);
                    }
                }
            }
        }

        return arrayDeLinhas;
    }
    
    public String comentario(String comando) {
        
        switch(comando) {
            
            case "LDC":
                    return "Carregar constante";
            case "LDV":
                    return "Carregar valor";
            case "ADD":
                    return "Somar";
            case "SUB":
                    return "Subtrair";
            case "MULT":
                    return "Multiplicar";
            case "DIVI":
                    return "Dividir";
            case "INV":
                    return "Inverter sinal";
            case "AND":
                    return "Conjunção";
            case "OR":
                    return "Disjunção";
            case "NEG":
                    return "Negação";
            case "CME":
                    return "Comparar menor";
            case "CMA":
                    return "Comparar maior";
            case "CEQ":
                    return "Comparar igual";
            case "CDIF":
                    return "Comparar desigual";
            case "CMEQ":
                    return "Comparar menor ou igual";
            case "CMAQ":
                    return "Comparar maior ou igual";
            case "START":
                    return "Iniciar programa principal";
            case "HLT":
                    return "Parar";
            case "STR":
                    return "Armazenar valor";
            case "JMP":
                return "Desviar sempre";
            case "JMPF":
                return "Desviar se falso";
            case "NULL":
                return "Nada";
            case "RD":
                return "Leitura";
            case "PRN":
                return "Impressão";
            case "ALLOC":
                return "Alocar memória";
            case "DALLOC":
                return "Desalocar memória";
            case "CALL":
                return "Chamar procedimento ou função";
            case "RETURN":
                return "Retornar de procedimento";
            default:
                return "?";
        }
    }
}
