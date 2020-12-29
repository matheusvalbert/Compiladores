package sintatico;

import geracaoDeCodigo.codigo;
import ihc.erroCompilacao;
import java.util.ArrayList;
import lexico.lexico;
import lexico.token;
import semantico.semantico;

public class sintatico {
    
    private lexico lexico;
    private codigo codigo;
    private token token, tokenAnterior;
    private ArrayList<token> expressao = new ArrayList<>();
    private semantico semantico;
    private boolean retornoFuncao = false;

    public sintatico(lexico lexico, codigo codigo) {
    
        this.lexico = lexico;
        this.codigo = codigo;
    }
    
    public void analisadorSintatico() {
        
        semantico = new semantico(codigo);
        
        codigo.setRotulo(1);
        
        token = lexico.getToken();
        
        if(token.getSimbolo().equals("sprograma")) {
            
            codigo.geraSTART();
            codigo.geraALLOC(0, 1); // Cria variável para o retorno da função
            codigo.setTotalVar(1);
            codigo.pushPilhaVar(1);
            
            token = lexico.getToken();
            
            if(token.getSimbolo().equals("sidentificador")) {
                
                semantico.insereTabela(token.getLexema(), "nome_de_programa", false, -1, -1); // Insere o nome do programa na tabela de simbolos (para evitar que tenha variáveis com o mesmo nome do programa)
                
                token = lexico.getToken();
            
                if(token.getSimbolo().equals("sponto_virgula")) {

                    analisaBloco();
                    
                    int nvar = codigo.popPilhaVar(); // Desaloca as variáveis alocadas na "main"
                    
                    if(nvar != 0) {
                        
                        codigo.setTotalVar(codigo.getTotalVar() - nvar);
                        codigo.geraDALLOC(codigo.getTotalVar(), nvar);
                    }

                    if(token.getSimbolo().equals("sponto")) {

                        if(lexico.terminoTokens() == true) {

                            // Desaloca a variável de retorno da função e  finaliza a compilação com o comando HLT
                            //return "Compilacao realizada com sucesso";
                            codigo.popPilhaVar();
                            codigo.setTotalVar(0);
                            codigo.geraDALLOC(0, 1);
                            codigo.geraHLT();
                        }

                        else {
                            
                            token = lexico.getToken();
                            
                            ihc.Singleton.getInstance().erroLinha = token.getLinha();
                            throw new erroCompilacao("Comandos encontrados apos possivel finalizacao do programa, erro encontrado: " + token.getLexema() + token.getLinha() + " Posicao: " + token.getPosicao());
                        }
                    }
                    else {
                        
                        ihc.Singleton.getInstance().erroLinha = token.getLinha();
                        throw new erroCompilacao("Problema ao finalizar o codigo, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                    }
                }
                else {

                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Erro na inicializacao do programa, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            }
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Erro na inicializacao do programa, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Erro na inicializacao do programa, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaBloco() {
        
        token = lexico.getToken();
            
        analisaEtVariaveis(); // Analisa as variáveis
            
        analisaSubrotinas(); // Função e procedimento
        
        analisaComandos(); // Analisa os comandos
    }
    
    public void analisaEtVariaveis() { // Analisa etapa de declaração de variáveis
        
        int nVarDecl = 0; // Total de variáveis no escopo
        
        if (token.getSimbolo().equals("svar")) {
            
            token = lexico.getToken();
            
            if (token.getSimbolo().equals("sidentificador")) {
                
                while(token.getSimbolo().equals("sidentificador")) {
                    
                    nVarDecl += analisaVariaveis(codigo.getTotalVar() + nVarDecl); // Análise de quantidade de variáveis por linha
                    
                    if(token.getSimbolo().equals("sponto_virgula")) {
                        
                        token = lexico.getToken();
                    }
                    else {
                        
                        ihc.Singleton.getInstance().erroLinha = token.getLinha();
                        throw new erroCompilacao("Problema na declaracao de variaveis, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                    }
                }
                
                codigo.geraALLOC(codigo.getTotalVar(), nVarDecl); // Gera os ALLOCs das variáveis
                codigo.setTotalVar(codigo.getTotalVar() + nVarDecl); // Atualiza a quantidade total de variáveis
            }
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema na declaracao de variaveis, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        codigo.pushPilhaVar(nVarDecl); // Coloca na pilha o total de variáveis lida no escopo
    }
    
    public int analisaVariaveis(int decl) { // Analisa as variáveis antes da atribuição de tipo
        
        int nvar = 0; // Contador de variáveis na mesma linha 
        
        do {
            
            if(token.getSimbolo().equals("sidentificador")) {
                
                if(semantico.pesquisaDuplicVarTabela(token.getLexema()) == false) {
            
                    semantico.insereTabela(token.getLexema(), null, false, -1, decl + nvar); // Adiciona a variável na tabela de simbolos e coloca o valor da posição de memória dela
                    
                    nvar++;
                    
                    token = lexico.getToken();

                    if(token.getSimbolo().equals("svirgula") || token.getSimbolo().equals("sdoispontos")) {

                        if(token.getSimbolo().equals("svirgula")) {

                            token = lexico.getToken();

                            if(token.getSimbolo().equals("sdoispontos")) {

                                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                                throw new erroCompilacao("Problema encontrado na atribuicao do tipo de variavel, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                            }
                        }
                    }
                    else {

                        ihc.Singleton.getInstance().erroLinha = token.getLinha();
                        throw new erroCompilacao("Problema encontrado no modo de declaracao das variaveis, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                    }
                }
                
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Problema encontrado variavel com dupla declaracao, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            
            }
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado no modo de declaracao das variaveis, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
            
        } while(!token.getSimbolo().equals("sdoispontos"));
        
        token = lexico.getToken();
        
        analisaTipo(); // Coloca se tipo inteiro ou booleano na variável (coloca em todas da linha)
        
        return nvar; // Retorna a quantidade de variáveis na linha
    }
    
    public void analisaTipo() { // Verifica se é inteiro ou booleano a variável
        
        if(!token.getSimbolo().equals("sinteiro") && !token.getSimbolo().equals("sbooleano")) {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na atribuicao do tipo de variavel, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
        else {
            
            semantico.colocaTipoTabela(token.getLexema()); // Coloca o tipo da variável na tabela de símbolos
        }
        
        token = lexico.getToken();
    }
    
    public void analisaComandos() { // Analisa os comandos existentes dentro de um inicio e fim
        
        if(token.getSimbolo().equals("sinicio")) {
            
            token = lexico.getToken();
            
            analisaComandoSimples();
            
            while(!token.getSimbolo().equals("sfim")) {
                
                if(token.getSimbolo().equals("sponto_virgula")) {
                    
                    token = lexico.getToken();
                    
                    if(!token.getSimbolo().equals("sfim")) {
                        
                        analisaComandoSimples();
                    }
                }
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Problema encontrado na analise dos comandos, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            }
            
            token = lexico.getToken();
        }
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na analise dos comandos, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaComandoSimples() {
        
        if(retornoFuncao == true) {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na analise dos comandos, codigo inalcancavel, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
        
        if(token.getSimbolo().equals("sidentificador")) {
            
            analisaAtribChprocedimento();
        }
        else if(token.getSimbolo().equals("sse")) {
            
            analisaSe();
        }
        else if(token.getSimbolo().equals("senquanto")) {
            
            analisaEnquanto();
        }
        else if(token.getSimbolo().equals("sleia")) {
            
            analisaLeia();
        }
        else if(token.getSimbolo().equals("sescreva")) {
            
            analisaEscreva();
        }
        else {
            
            analisaComandos(); // "Recursividade"
        }
    }
    
    public void analisaAtribChprocedimento() {
        
        tokenAnterior = token;
        
        token = lexico.getToken();
        
        if(token.getSimbolo().equals("satribuicao")) { // :=
            
            analisaAtribuicao();
        }
        else {
            
            analisaChamadaDeProcedimento();
        }
    }
    
    public void analisaLeia() {
        
        token = lexico.getToken();
        
        if(token.getSimbolo().equals("sabre_parenteses")) {
            
            token = lexico.getToken();
            
            if(token.getSimbolo().equals("sidentificador")) {
                
                if(semantico.pesquisaDeclvarTabela(token.getLexema()) == true) {
                    
                    codigo.geraRD();
                    codigo.geraSTR(semantico.retornaPosicaoMem(token.getLexema()));
                    
                    token = lexico.getToken();

                    if(token.getSimbolo().equals("sfecha_parenteses")) {

                        token = lexico.getToken();
                    }

                    else {

                        ihc.Singleton.getInstance().erroLinha = token.getLinha();
                        throw new erroCompilacao("Problema encontrado no leia, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                    }
                }
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Problema encontrado, variavel nao declarada, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            }
            
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado no leia, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado no leia, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaEscreva() {
        
        token = lexico.getToken();
        
        if(token.getSimbolo().equals("sabre_parenteses")) {
            
            token = lexico.getToken();
            
            if(token.getSimbolo().equals("sidentificador")) {
                
                if(semantico.pesquisaDeclvarfuncTabela(token.getLexema()) == true) {
                    
                    if(semantico.retornaPosicaoMem(token.getLexema()) != -1) { // Verifica se é variável ou função
                        
                        codigo.geraLDV(semantico.retornaPosicaoMem(token.getLexema()));
                    }
                    else {
                        // Para função, gera o CALL e carrega o retorno da variável
                        codigo.geraCALL(semantico.retornaNivel(token.getLexema()));
                        codigo.geraLDV(0);
                    }
                    codigo.geraPRN();
                    
                    token = lexico.getToken();

                    if(token.getSimbolo().equals("sfecha_parenteses")) {

                        token = lexico.getToken();
                    }

                    else {

                        ihc.Singleton.getInstance().erroLinha = token.getLinha();
                        throw new erroCompilacao("Problema encontrado no escreva, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                    }       
                }
                
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Problema encontrado, variavel ou funcao nao encontrada, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            }
            
            
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado no escreva, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado no escreva, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaEnquanto() {
        
        int rotulo1 = codigo.getRotulo();
        codigo.setRotulo(codigo.getRotulo() + 1);
        codigo.geraNULL(rotulo1);
        
        token = lexico.getToken();
        
        analisaExpressao(); // Faz a análise da expressão
        
        String tipoExpressao = semantico.trataExpressao(expressao); // Retorna o tipo da expressão
        
        if(!tipoExpressao.equals("booleano")) { // Caso de erro no tipo da expressão
            
            String printExpressao = "";
            
            for(token e : expressao) {
                
                printExpressao += e.getLexema();
            }
            
            //System.err.println(printExpressao);
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na analise da expressao, expressao nao booleana encontrada, Expressao: " + printExpressao + " Linha: " + token.getLinha());
        }
        
        expressao.clear();
        
        if(token.getSimbolo().equals("sfaca")) {
            
            int rotulo2 = codigo.getRotulo();
            codigo.setRotulo(codigo.getRotulo() + 1);
            codigo.geraJMPF(rotulo2); // Jump para fora do loop
            
            token = lexico.getToken();
            
            analisaComandoSimples();
            
            retornoFuncao = false; // Indica que a função não tem retorno
            
            codigo.geraJMP(rotulo1);
            codigo.geraNULL(rotulo2); // Lugar para sair do loop
        }
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na inicializacao do enquanto, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaSe() {
        
        boolean retorno;
        int rotulo1 = codigo.getRotulo(), rotulo2 = rotulo1;
        
        token = lexico.getToken();
        
        analisaExpressao();
            
        String tipoExpressao = semantico.trataExpressao(expressao);
        
        if(!tipoExpressao.equals("booleano")) {
            
            String printExpressao = "";
            
            for(token e : expressao) {
                
                printExpressao += e.getLexema();
            }
            
            System.err.println(printExpressao);
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na analise da expressao, expressao nao booleana encontrada, Expressao: " + printExpressao + " Linha: " + token.getLinha());
        }
        
        expressao.clear();
            
        if(token.getSimbolo().equals("sentao")) { // Se (if)
            
            codigo.setRotulo(codigo.getRotulo() + 1); // Atuliza o último rótulo
            codigo.geraJMPF(rotulo1);
            
            token = lexico.getToken();
        
            analisaComandoSimples();
            
            retorno = retornoFuncao; // Armazena se a função tinha retorno antes
            
            if(token.getSimbolo().equals("ssenao")) { // Senão (else)
                
                rotulo2 = codigo.getRotulo();
                codigo.setRotulo(codigo.getRotulo() + 1);
                codigo.geraJMP(rotulo2);
                codigo.geraNULL(rotulo1);
                
                retornoFuncao = false; // False, mas irá verificar se o senão tem retorno dentro de analisaComandoSimples()
                
                token = lexico.getToken();
        
                analisaComandoSimples();
                
                // Valida se exite retorno de função
                if(retorno == true && retornoFuncao == true) {
                    
                    retornoFuncao = true;
                }
                else {
 
                    retornoFuncao = false;
                }
            }
            else {
                
                retornoFuncao = false;
            }
            
            codigo.geraNULL(rotulo2);
        }
        
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado no condicional se, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaSubrotinas() { // Analisa procedimento ou função
        
        boolean flag = false;
        int rotulo = 0;
        
        if(token.getSimbolo().equals("sprocedimento") || token.getSimbolo().equals("sfuncao")) {
            
            flag = true;
            rotulo = codigo.getRotulo();
            codigo.setRotulo(codigo.getRotulo() + 1);
            
            codigo.geraJMP(rotulo);
        }
        
        while(token.getSimbolo().equals("sprocedimento") || token.getSimbolo().equals("sfuncao")) {
            
            if(token.getSimbolo().equals("sprocedimento")) {
                
                analisaDeclaracaoProcedimento();
            }
            else {
                
                analisaDeclaracaoFuncao();
            }
            
            if(token.getSimbolo().equals("sponto_virgula")) {
                
                token = lexico.getToken();
            }
            
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado na finalizacao da declaracao de subrotinas, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        if(flag == true) { // Se existir subrotina, ele pula para o inicio do "programa", se existir subrotina dentro de subrotina, vai para a mais externa ("recursividade")
            
            codigo.geraNULL(rotulo);
        }
    }
    
    public void analisaDeclaracaoProcedimento() { // Verifica o procedimento inteiro
        
        int nivel = codigo.getRotulo(); // Label
        codigo.setRotulo(codigo.getRotulo() + 1); // Incrementa o contador de Label
        
        token = lexico.getToken();
        
        if(token.getSimbolo().equals("sidentificador")) {
            
            if(semantico.pesquisaDeclprocTabela(token.getLexema()) == false) {
                
                semantico.insereTabela(token.getLexema(), "procedimento", true, nivel, -1); // Insere na tabela de símbolos
                
                codigo.geraNULL(nivel); // Gera o NULL em cima do procedimento
                
                token = lexico.getToken();

                if(token.getSimbolo().equals("sponto_virgula")) {

                    analisaBloco();
                    
                    // Gera o DALLOC das variáveis e o RETURN
                    int nvar = codigo.popPilhaVar();
                    if(nvar != 0) {
                        
                        codigo.setTotalVar(codigo.getTotalVar() - nvar);
                        codigo.geraDALLOC(codigo.getTotalVar(), nvar);
                    }
                    
                    codigo.geraRETURN();
                }

                else {

                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Problema na declaracao do procedimento, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            }

            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado, procedimento ja existente no escopo, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema na declaracao do procedimento, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
        
        semantico.desempilha(); // Desempilha as variáveis do procedimento na tabela de simbolos
    }
    
    public void analisaDeclaracaoFuncao() { // Verifica a função inteira
        
        int nivel = codigo.getRotulo();
        codigo.setRotulo(codigo.getRotulo() + 1);
        
        token = lexico.getToken();
        
        if(token.getSimbolo().equals("sidentificador")) {
            
            if(semantico.pesquisaDeclfuncTabela(token.getLexema()) == false) {
                
                semantico.insereTabela(token.getLexema(), null, true, nivel, -1); // Insere na tabela de símbolos o nome e o nível da fução (nível = Label)
                
                codigo.geraNULL(nivel);
                
                token = lexico.getToken();

                if(token.getSimbolo().equals("sdoispontos")) {

                    token = lexico.getToken();

                    if(token.getSimbolo().equals("sinteiro") || token.getSimbolo().equals("sbooleano")) {

                        semantico.colocaTipofuncTabela(token.getSimbolo()); // Coloca o tipo de retorno da função na tabela de símbolos

                        token = lexico.getToken();

                        if(token.getSimbolo().equals("sponto_virgula")) {

                            analisaBloco();
                            
                            if(retornoFuncao == false) { // Verifica se tem retorno na função, se não tiver retorna erro
                                
                                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                                throw new erroCompilacao("Problema encontrado, funcao sem retorno, erro linha: " + token.getLinha());
                            }
                            retornoFuncao = false;
                            
                            codigo.setTotalVar(codigo.getTotalVar() - codigo.popPilhaVar()); // Tira do total de variáveis alocadas e desempilha o total de variáveis da função
                        }
                    }

                    else {

                        ihc.Singleton.getInstance().erroLinha = token.getLinha();
                        throw new erroCompilacao("Problema na declaracao de funcao, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                    }
                }

                else {

                    ihc.Singleton.getInstance().erroLinha = token.getLinha();
                    throw new erroCompilacao("Problema na declaracao de funcao, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
                }
            }

            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado, funcao ja existente no escopo, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema na declaracao da funcao, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }

        semantico.desempilha(); // Desempilha as variáveis da tabela de símbolos
    }
    
    public void analisaExpressao() {
        
        analisaExpressaoSimples();
        
        if(token.getSimbolo().equals("smaior") || token.getSimbolo().equals("smaiorig") || token.getSimbolo().equals("sig") || token.getSimbolo().equals("smenor") || token.getSimbolo().equals("smenorig") || token.getSimbolo().equals("sdif")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
            
            analisaExpressaoSimples();
        }
    }
    
    public void analisaExpressaoSimples() {
        
        if(token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
        }
            
        analisaTermo();
            
        while(token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos") || token.getSimbolo().equals("sou")) {

            expressao.add(token);
            
            token = lexico.getToken();

            analisaTermo();
        }
    }
    
    public void analisaTermo() {
        
        analisaFator();
        
        while(token.getSimbolo().equals("smult") || token.getSimbolo().equals("sdiv") || token.getSimbolo().equals("se")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
            
            analisaFator();
        }
    }
    
    public void analisaFator() {
        
        if(token.getSimbolo().equals("sidentificador")) {
            
            String tipoFuncao = semantico.pesquisaTabela(token.getLexema());
            if(tipoFuncao != null) {
                
                if(tipoFuncao.equals("funcao_inteiro") || tipoFuncao.equals("funcao_booleano")) {
                    
                    analisaChamadaDeFuncao();
                }
                else {
                    
                    expressao.add(token);
                    
                    token = lexico.getToken();
                }
            }
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado na analise da variavel ou chamada da funcao, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        else if (token.getSimbolo().equals("snumero")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
        }
        
        else if(token.getSimbolo().equals("snao")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
            
            analisaFator();
        }
        
        else if(token.getSimbolo().equals("sabre_parenteses")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
            
            analisaExpressao();
            
            if(token.getSimbolo().equals("sfecha_parenteses")) {
                
                expressao.add(token);
                
                token = lexico.getToken();
            }
            
            else {
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado na analise do fator, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
            }
        }
        
        else if(token.getLexema().equals("verdadeiro") || token.getLexema().equals("falso")) {
            
            expressao.add(token);
            
            token = lexico.getToken();
        }
        
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado na analise do fator, erro encontrado: " + token.getLexema() + " Linha: " + token.getLinha() + " Posicao: " + token.getPosicao());
        }
    }
    
    public void analisaAtribuicao() { // :=
        
        //System.err.println(tokenAnterior.lexema); ver depois
        
        token = lexico.getToken();
            
        analisaExpressao();
        
        String tipoExpressao = semantico.trataExpressao(expressao);
        
        expressao.clear();
        
        String tipo = semantico.pesquisaTabela(tokenAnterior.getLexema()); // Verifica o tipo
        
        if(tipo == null) { // Se a variável não existir retorna erro
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado, variavel nao existente, erro encontrado: " + tokenAnterior.getLexema() + " Linha: " + tokenAnterior.getLinha() + " Posicao: " + tokenAnterior.getPosicao());
        }
        
        if(tipo.equals("inteiro")) { // Verifica se tipo da variável é inteiro
            
            if(!tipoExpressao.equals("inteiro")) { // Verifica se o tipo da expressão é compatível
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado, tipos incompativeis na atribuicao da variavel, linha: " + tokenAnterior.getLinha());
            }
        }
        else if(tipo.equals("funcao_inteiro")) { // Verifica se o tipo da função é inteiro
            
            if(!tipoExpressao.equals("inteiro")) { // Verifica se o tipo da expressão é compatível
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado, tipos incompativeis na atribuicao da variavel, linha: " + tokenAnterior.getLinha());
            }
                
            retornoFuncao = true;
        }
        else if(tipo.equals("booleano")) { // Verifica se tipo da variável é booleano
            
            if(!tipoExpressao.equals("booleano")) { // Verifica se o tipo da expressão é compatível
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado, tipos incompativeis na atribuicao da variavel, linha: " + tokenAnterior.getLinha());
            }
        }
        else if(tipo.equals("funcao_booleano")) {  // Verifica se o tipo da função é booleano
            
            if(!tipoExpressao.equals("booleano")) { // Verifica se o tipo da expressão é compatível
                
                ihc.Singleton.getInstance().erroLinha = token.getLinha();
                throw new erroCompilacao("Problema encontrado, tipos incompativeis na atribuicao da variavel, linha: " + tokenAnterior.getLinha());
            }
                
            retornoFuncao = true; // Indica que existe retorno nessa atribuição
        }
        else {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema encontrado, possivel nao variavel encontrada na atribuicao, linha: " + tokenAnterior.getLinha());
        }
        
        if (retornoFuncao == true) { // Se for retorno de função
            
            int nvar = codigo.popPilhaVar(); // Pega o valor para colocar na variável
            
            codigo.pushPilhaVar(nvar); // Insere novamente o valor retirado na linha acima
            
            codigo.geraSTR(0); // Salva o retorno da função na posição 0

            if(nvar != 0) {

                codigo.geraDALLOC(codigo.getTotalVar() - nvar, nvar); // Desaloca caso exista variáveis
            }

            codigo.geraRETURN();
        }
        else {
            
            codigo.geraSTR(semantico.retornaPosicaoMem(tokenAnterior.getLexema())); // Gera STR de atribução a alguma variável
        }
    }
    
    public void analisaChamadaDeProcedimento() {
        
        // Verifica se o procedimento está na tabela de símbolos e gera o CALL
        if(semantico.pesquisaDeclprocTabela(tokenAnterior.getLexema()) == false) {
            
            ihc.Singleton.getInstance().erroLinha = token.getLinha();
            throw new erroCompilacao("Problema enconrado, procedimento nao existente, erro encontrado: " + tokenAnterior.getLexema() + " Linha: " + tokenAnterior.getLinha() + " Posicao: " + tokenAnterior.getPosicao());
        }
        
        codigo.geraCALL(semantico.retornaNivel(tokenAnterior.getLexema()));
        
        //token = lexico.getToken();
    }
    
    public void analisaChamadaDeFuncao() {
        
        // Realiza a chamada da função dentro da Geração de Código da Posfixa (semantico:codigoExpressao())
        expressao.add(token);
        
        token = lexico.getToken();
    }
}
