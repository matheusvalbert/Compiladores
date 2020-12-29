package lexico;

import ihc.erroCompilacao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class lexico {
    
    private int linha = 1, posicao = 1, retornaTokenIndex = 0;
    private ArrayList<token> tokens = new ArrayList<>();
    private leituraDoArquivo leituraDoArquivo;
    private String lexicoErro;
    
    public lexico(leituraDoArquivo leituraDoArquivo) throws FileNotFoundException {
    
        this.leituraDoArquivo = leituraDoArquivo;
    }
    
    public void analizadorLexico() throws FileNotFoundException, IOException {
        
        int caracterAux; // Caracter na tabela ASCII
        String caracter;
        boolean erro = false;
        
        caracterAux = leituraDoArquivo.lerCaracter();
        caracter = "" + (char)caracterAux;
        
        while(caracterAux != -1) {
        
            while((caracter.equals("{") || caracter.equals(" ") || caracterAux == 10 || caracterAux == 13 || caracterAux == 9 || caracter.equals("/")) && caracterAux != -1) { // Enquanto for espaço, tab, pula linha ou comentario, e, não é final de arquivos, faz o tratamento aqui dentro - line feed = 10 - carriage return = 13

                if(caracter.equals("/")) { // Se o caracter lido for barra (de comentário)

                    caracterAux = leituraDoArquivo.lerCaracter();
                    caracter = "" + (char)caracterAux;

                    if(caracter.equals("*")) { // Verifica se é seguido de *

                        while(caracterAux != -1) { // Le os caracteres até o final do arquivo

                            caracterAux = leituraDoArquivo.lerCaracter();
                            caracter = "" + (char)caracterAux;
                            if(caracterAux == 10) { // Se pula linha, incrementa linha e zera posição

                                linha++;
                                posicao = 1;
                            }

                            if(caracter.equals("*")) { 

                                caracterAux = leituraDoArquivo.lerCaracter();
                                caracter = "" + (char)caracterAux;

                                if(caracterAux == 10) { // Se for * mas não seguido de barra, e for pula linha, ele incrementa a contadora de linhas

                                    linha++;
                                    posicao = 1;
                                }

                                if(caracter.equals("/")) { // Se for * seguido de barra, finaliza o comentário

                                    break;
                                }
                            }
                        }

                        if(caracterAux == -1 && !caracter.equals("/")) { // Se encontrou o * mas não seguindo de barra e leu final de arquivo, apresenta erro

                            lexicoErro = "Erro ausencia de */ para finalizar comentario - " + " Linha: " + linha + " Posição: " + posicao;
                        }
                    } else { // Se encontrou barra, mas não seguida de *, apresenta erro

                        lexicoErro = "Erro / sem * não reconhecida - " + " Linha: " + linha + " Posição: " + posicao;
                    }

                    caracterAux = leituraDoArquivo.lerCaracter();
                    caracter = "" + (char)caracterAux;
                }

                if(caracter.equals("{")) { // Se o caracter lido for { (de comentário)

                    while(caracterAux != -1) { // Le os caracteres até o final do arquivo

                        caracterAux = leituraDoArquivo.lerCaracter();
                        caracter = "" + (char)caracterAux;

                        if(caracterAux == 10) { // Se pula linha, incrementa linha e zera posição

                            linha++;
                            posicao = 1;
                        }

                        if(caracter.equals("}")) { // Se for }, finaliza o comentário

                            break;
                        }
                    }

                    if(caracterAux == -1 && !caracter.equals("}")) { // Se não encontrou o } e leu final de arquivo, apresenta erro

                        lexicoErro = "Erro comentario nao finalizado com } - " + " Linha: " + linha + " Posição: " + posicao;
                    }

                    caracterAux = leituraDoArquivo.lerCaracter();
                    caracter = "" + (char)caracterAux;
                }

                while((caracter.equals(" ") || caracterAux == 10 || caracterAux == 13 || caracterAux == 9) && caracterAux != -1) { // Enquanto for espaço, tab ou pula linha, le e ignora os caracteres

                    if(caracterAux == 10) { // Se pula linha, incrementa linha e zera posição

                        linha++;
                        posicao = 1;
                    }

                    caracterAux = leituraDoArquivo.lerCaracter();
                    caracter = "" + (char)caracterAux;
                }
            } // fim do tratamento de espaço, tab, pula linha e comentários (while)

            if(caracterAux != -1) { // Se não for final de arquivo, trata o token

                token token = new token(linha, posicao);
                caracterAux = pegaToken(caracterAux, token);

                if (caracterAux == -2) { // Se retornou algum erro no pegaToken, para a execução e indica erro

                    erro = true;
                    token.setErro(erro);
                    tokens.add(token);
                    break;

                } else { // Se leu corretamente, adiciona o token no arrayList

                    token.setErro(false);
                    tokens.add(token);
                    caracter = "" + (char)caracterAux;
                    posicao++;
                }
            }
        }
        if (erro == true) { // Se teve erro, retorna o erro
            
            lexicoErro = "Problema encontrado, caracter invalido, caracter: " + caracter + " Linha: " + linha + " Posição: " + posicao;
        }
        else {
            
            lexicoErro = null;
        }
    }
    
    public int pegaToken(int caracterAux, token token) throws IOException {
        
        String caracter;
        
        caracter = "" + (char)caracterAux;
        
        if(caracter.matches("\\d")) { // Se o caracter for dígito
            
            return trataDigito(caracterAux, token);
        }
        else if(caracter.matches("\\p{IsAlphabetic}")) { // Se o caracter for alfabetico
            
            return trataIdentificarEPalavraReservada(caracterAux, token);
        }
        else if(caracter.matches("\\:")) { // Se o caracter for :
            
            return trataAtribuicao(caracterAux, token);
        }
        else if(caracter.matches("\\+") || caracter.matches("\\-") || caracter.matches("\\*")) { // Se o caracter for +, - , *
            
            return trataOperadorAritmetico(caracterAux, token);
        }
        else if(caracter.matches("\\<") || caracter.matches("\\>") || caracter.matches("\\=") || caracter.matches("\\!")) { // Se o caracter for <, >, =
            
            return trataOperadorRelacional(caracterAux, token);
        }
        else if(caracter.matches("\\;") || caracter.matches("\\,") || caracter.matches("\\(") || caracter.matches("\\)") || caracter.matches("\\.")) { // Se o caracter for ;, ,, (, ), .
            
            return trataPontuacao(caracterAux, token);
        }
        else { // Senão erro
            
            return -2;
        }
    }
    
    public int trataDigito(int caracterAux, token token) throws IOException {
        
        String caracter, numero;
        
        caracter = "" + (char)caracterAux;
        
        numero = caracter;
        caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
        caracter = "" + (char)caracterAux;
        
        while(caracter.matches("\\d")) { // Vai lendo enquanto for um dígito numérico
            
            numero = numero + caracter;
            caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
            caracter = "" + (char)caracterAux;
        }
        
        token.setLexema(numero);
        token.setSimbolo("snumero");
        
        return caracterAux;
    }
    
    public int trataIdentificarEPalavraReservada(int caracterAux, token token) throws IOException {
        
        String caracter, id;
        
        caracter = "" + (char)caracterAux;
        
        id = caracter;
        caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
        caracter = "" + (char)caracterAux;
        
        while(caracter.matches("\\p{IsAlphabetic}") || caracter.matches("\\d") || caracter.matches("\\_")) { // Vai lendo enquanto for um caracter alafabetico ou um dígito numérico ou _
            
            id = id + caracter;
            caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
            caracter = "" + (char)caracterAux;
        }
        
        token.setLexema(id);
        
        switch(id) { // Salva o token simbolo

            case "programa":
                token.setSimbolo("sprograma");
                break;
            case "se":
                token.setSimbolo("sse");
                break;
            case "entao":
                token.setSimbolo("sentao");
                break;
            case "senao":
                token.setSimbolo("ssenao");
                break;
            case "enquanto":
                token.setSimbolo("senquanto");
                break;
            case "faca":
                token.setSimbolo("sfaca");
                break;
            case "inicio":
                token.setSimbolo("sinicio");
                break;
            case "fim":
                token.setSimbolo("sfim");
                break;
            case "escreva":
                token.setSimbolo("sescreva");
                break;
            case "leia":
                token.setSimbolo("sleia");
                break;
            case "var":
                token.setSimbolo("svar");
                break;
            case "inteiro":
                token.setSimbolo("sinteiro");
                break;
            case "booleano":
                token.setSimbolo("sbooleano");
                break;
            case "verdadeiro":
                token.setSimbolo("sverdadeiro");
                break;
            case "falso":
                token.setSimbolo("sfalso");
                break;
            case "procedimento":
                token.setSimbolo("sprocedimento");
                break;
            case "funcao":
                token.setSimbolo("sfuncao");
                break;
            case "div":
                token.setSimbolo("sdiv");
                break;
            case "e":
                token.setSimbolo("se");
                break;
            case "ou":
                token.setSimbolo("sou");
                break;
            case "nao":
                token.setSimbolo("snao");
                break;
            default:
                token.setSimbolo("sidentificador");
                break;
        }
        
        return caracterAux;
    }
    
    public int trataAtribuicao(int caracterAux, token token) throws IOException {
        
        String caracter, atribuicao;
        
        caracter = "" + (char)caracterAux;
        
        atribuicao = caracter;
        caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
        caracter = "" + (char)caracterAux;
        
        if(caracter.matches("\\=")) { // Se o caracter for = (obs: já leu o : anteriormente)
            
            atribuicao = atribuicao + caracter;
            caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
            caracter = "" + (char)caracterAux;
        }
        
        token.setLexema(atribuicao);
        
        switch(atribuicao) { // Salva o token simbolo
            
            case ":":
                token.setSimbolo("sdoispontos");
                break;
            case ":=":
                token.setSimbolo("satribuicao");
                break;
            default:
                token.setSimbolo(null);
                break;
        }
        
        return caracterAux;
    }
    
    public int trataOperadorAritmetico(int caracterAux, token token) throws IOException {
        
        String caracter, aritmetico;
        
        caracter = "" + (char)caracterAux;
        
        aritmetico = caracter;
        
        caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
        caracter = "" + (char)caracterAux;
        
        token.setLexema(aritmetico);
        
        switch(aritmetico) { // Salva o token simbolo
            
            case "+":
                token.setSimbolo("smais");
                break;
            case "-":
                token.setSimbolo("smenos");
                break;
            case "*":
                token.setSimbolo("smult");
                break;
            default:
                token.setSimbolo(null);
                break;
        }
        
        return caracterAux; // Retorna o próximo caracter que foi lido nessa função
    }
    
    public int trataOperadorRelacional(int caracterAux, token token) throws IOException {
        
        String caracter, relacional;
        
        caracter = "" + (char)caracterAux;
        
        relacional = caracter;
        
        caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
        caracter = "" + (char)caracterAux;
        
        switch(relacional) {
            
            case "=":
                token.setLexema(relacional);
                token.setSimbolo("sig");
                return caracterAux; // Retorna o próximo caracter que foi lido nessa função
            case "!":
                if(caracter.matches("\\=")) {
                    
                    relacional = relacional + caracter;
                    caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
                    caracter = "" + (char)caracterAux;
                    token.setLexema(relacional);
                    token.setSimbolo("sdif");
                    return caracterAux; // Retorna o próximo caracter que foi lido nessa função
                }
                else {
                    
                    return -2;
                }
            case ">":
                if(caracter.matches("\\=")) {
                    
                    relacional = relacional + caracter;
                    caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
                    caracter = "" + (char)caracterAux;
                }
                token.setLexema(relacional);
                if(relacional.equals(">"))
                    token.setSimbolo("smaior");
                else if(relacional.equals(">="))
                    token.setSimbolo("smaiorig");
                else
                    token.setSimbolo(null);
                return caracterAux; // Retorna o próximo caracter que foi lido nessa função
            case "<":
                if(caracter.matches("\\=")) {
                    
                    relacional = relacional + caracter;
                    caracterAux = leituraDoArquivo.lerCaracter(); // Le o proximo caracter
                    caracter = "" + (char)caracterAux;
                }
                token.setLexema(relacional);
                if(relacional.equals("<"))
                    token.setSimbolo("smenor");
                else if(relacional.equals("<="))
                    token.setSimbolo("smenorig");
                else
                    token.setSimbolo(null);
                return caracterAux; // Retorna o próximo caracter que foi lido nessa função
            default:
                return caracterAux; // Retorna o próximo caracter que foi lido nessa função
        }
    }
    
    public int trataPontuacao(int caracterAux, token token) throws IOException {
        
        String caracter, pontuacao;
        
        caracter = "" + (char)caracterAux;
        
        pontuacao = caracter;
        
        caracterAux = leituraDoArquivo.lerCaracter();  // Le o proximo caracter
        caracter = "" + (char)caracterAux;
        
        token.setLexema(pontuacao);
        
        switch(pontuacao) {
            
            case ".":
                token.setSimbolo("sponto");
                break;
            case ";":
                token.setSimbolo("sponto_virgula");
                break;
            case ",":
                token.setSimbolo("svirgula");
                break;
            case "(":
                token.setSimbolo("sabre_parenteses");
                break;
            case ")":
                token.setSimbolo("sfecha_parenteses");
                break;
            default:
                token.setSimbolo(null);
                break;
        }
        
        return caracterAux; // Retorna o próximo caracter que foi lido nessa função
    }
    
    public token getToken() { // Retorna um token para o sintatico ou erro
        
        if(tokens.isEmpty() || tokens.size() == retornaTokenIndex) {
            
            throw new erroCompilacao("Problema encontrado no termino do codigo problema encontrado" + " Linha: " + linha + " Posicao: " + posicao);
        }
        else if(tokens.get(retornaTokenIndex).getErro() == true) {
            
            ihc.Singleton.getInstance().erroLinha = linha;
            throw new erroCompilacao(lexicoErro);
        }
        
        return tokens.get(retornaTokenIndex++);
    }
    
    public boolean terminoTokens() { // Verfica se ainda possui tokens na lista (retorna True ou False)
        
        if(tokens.size() == retornaTokenIndex)
            return true;
        else
            return false;
    }
}