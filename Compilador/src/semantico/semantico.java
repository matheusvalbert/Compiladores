package semantico;

import geracaoDeCodigo.codigo;
import ihc.erroCompilacao;
import java.util.ArrayList;
import lexico.token;

public class semantico {
    
    private ArrayList<simbolo> simbolos = new ArrayList<>();
    private codigo codigo;
    
    public semantico(codigo codigo) {
        
        this.codigo = codigo;
    }
    
    public void insereTabela(String lexema, String tipo, boolean nivel, int rotulo, int posMem) { // Insere na tabela de símbolos
        
        simbolo simbolo = new simbolo();
        simbolo.setNome(lexema);
        simbolo.setTipo(tipo);
        simbolo.setNivel(nivel);
        simbolo.setRotulo(rotulo);
        simbolo.setPosMem(posMem);
        simbolos.add(simbolo);
        //System.out.println("Insere tabela " + "Lexema: " + lexema + " tipo: " + tipo + " nivel: " + nivel + " rotulo: " + rotulo);
    }
    
    public boolean pesquisaDuplicVarTabela(String lexema) { // Verifica se existe variável duplicada
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {
            
            if(simbolos.get(i).getNivel() == true) {

                break;
            }
            
            if(simbolos.get(i).getTipo() == null || simbolos.get(i).getTipo().equals("inteiro") || simbolos.get(i).getTipo().equals("booleano")) {

                if(simbolos.get(i).getNome().equals(lexema)) {

                    return true;
                }
            }
        }
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {
                    
            if(simbolos.get(i).getTipo() != null && !simbolos.get(i).getTipo().equals("inteiro") && !simbolos.get(i).getTipo().equals("booleano")) {

                if(simbolos.get(i).getNome().equals(lexema)) {

                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void colocaTipoTabela(String lexema) { // Coloca o tipo da variável na tabela
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {
            
            if(simbolos.get(i).getTipo() == null) {
                
                simbolos.get(i).setTipo(lexema);
                //System.err.println("Lexema: " + simbolos.get(i).getNome() + " tipo: " + simbolos.get(i).getTipo() + " nivel: " + simbolos.get(i).getNivel() + " rotulo: " + simbolos.get(i).getRotulo());
            }
            else {
                
                break;
            }
        }
        
        //System.out.println("Tipos:");
        
        //for(simbolo simbolo : simbolos) {
            
            //System.err.println("Lexema: " + simbolo.getNome() + " tipo: " + simbolo.getTipo() + " nivel: " + simbolo.getNivel() + " rotulo: " + simbolo.getRotulo());
        //}
        
        //Thread.sleep(1000);
    }
    
    public boolean pesquisaDeclvarTabela(String lexema) { // Pesquisa declaração de variável na tabela
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {
            
            if(simbolos.get(i).getTipo().equals("inteiro") || simbolos.get(i).getTipo().equals("booleano")) {
                
                if(simbolos.get(i).getNome().equals(lexema)) {
                    
                    //System.err.println("Lexema: " + simbolos.get(i).getNome() + " tipo: " + simbolos.get(i).getTipo() + " nivel: " + simbolos.get(i).getNivel() + " rotulo: " + simbolos.get(i).getRotulo());
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean pesquisaDeclvarfuncTabela(String lexema) { // Pesquisa declaração de variável ou função na tabela
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {
            
            if(simbolos.get(i).getTipo().equals("inteiro") || simbolos.get(i).getTipo().equals("booleano") || simbolos.get(i).getTipo().equals("funcao_inteiro") || simbolos.get(i).getTipo().equals("funcao_booleano")) {
                
                if(simbolos.get(i).getNome().equals(lexema)) {
                    
                    //System.err.println("Lexema: " + simbolos.get(i).getNome() + " tipo: " + simbolos.get(i).getTipo() + " nivel: " + simbolos.get(i).getNivel() + " rotulo: " + simbolos.get(i).getRotulo());
                    return true;
                }
            }
        }       
        
        return false;
    }
    
    public boolean pesquisaDeclprocTabela(String lexema) { // Pesquisa declaração de procedimento na tabela
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {

            if(simbolos.get(i).getNome().equals(lexema) && simbolos.get(i).getTipo().equals("procedimento")) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean pesquisaDeclfuncTabela(String lexema) { // Pesquisa declaração de função na tabela
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {

            if(simbolos.get(i).getNome().equals(lexema)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public String pesquisaTabela(String lexema) { // Retorna o tipo (inteiro ou booleano) ou null caso não encontre
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {

            if(simbolos.get(i).getNome().equals(lexema)) {
                
                return simbolos.get(i).getTipo();
            }
        }
        
        return null;
    }
    
    public int retornaPosicaoMem(String lexema) { // Retona a posição de memória (STR e LDV) (para manipular a variável)
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {

            if(simbolos.get(i).getNome().equals(lexema)) {
                
                return simbolos.get(i).getPosMem();
            }
        }
        
        return -1;
    }
    
    public int retornaNivel(String lexema) { // Retorna o Label para chamar a função
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {

            if(simbolos.get(i).getNome().equals(lexema)) {
                
                return simbolos.get(i).getRotulo();
            }
        }
        
        return -1;
    }
    
    public void colocaTipofuncTabela(String simbolo) { // Coloca o tipo da função na tabela (inteiro ou booleano)
        
        if (simbolo.equals("sinteiro")) {
            
            simbolos.get(simbolos.size() - 1).setTipo("funcao_inteiro");
        }
        else {
            
            simbolos.get(simbolos.size() - 1).setTipo("funcao_booleano");
        }
    }
    
    public void desempilha() { // Desempilha as variáveis da função ou procedimento
        
        for(int i = simbolos.size() - 1; i >= 0; i--) {
            
            if(simbolos.get(i).getNivel() == true) {
                
                simbolos.get(i).setNivel(false);
                break;
            }
            else {
                
                simbolos.remove(i);
            }
        }
    }
    
    public String trataExpressao(ArrayList<token> expressao) { // Verifica o tipo da expressão (inteiro ou booleana)
        
        ArrayList<posfixaToken> expressaoPosfixa;
        ArrayList<String> tipos = new ArrayList<>();
        posfixa posfixa = new posfixa();
        String tipo;
        
        posfixa.setPrioridade(expressao); // Coloca a prioridade e gera o posfixaToken
        expressaoPosfixa = posfixa.geraPosfixa(); // Gera a expressão na forma posfixa
        
        for(posfixaToken expressaoTokens : expressaoPosfixa) { // Verifica se a expressão é inteira ou booleana e retorna
            
            if(expressaoTokens.getToken().getSimbolo().equals("sidentificador")) {
                
                tipo = pesquisaTabela(expressaoTokens.getToken().getLexema());
                
                if(tipo.equals("inteiro") || tipo.equals("funcao_inteiro")) {
                    
                    tipos.add("inteiro");
                }
                else if(tipo.equals("booleano") || tipo.equals("funcao_booleano")) {
                    
                    tipos.add("booleano");
                }
            }
            
            if(expressaoTokens.getToken().getSimbolo().equals("sverdadeiro") || expressaoTokens.getToken().getSimbolo().equals("sfalso")) {
                
                tipos.add("booleano");
            }

            if(expressaoTokens.getToken().getSimbolo().equals("snumero")) {
                
                tipos.add("inteiro");
            }
            
            if(expressaoTokens.getPrioridade() == 7) {

                if(!expressaoTokens.getToken().getSimbolo().equals("snao")) {

                    if(!tipos.get(tipos.size() - 1).equals("inteiro")) {
                        
                        ihc.Singleton.getInstance().erroLinha = expressaoTokens.getToken().getLinha();
                        throw new erroCompilacao("Erro encontrado, incompatibilidade de tipos, erro encontrado: " + expressaoTokens.getToken().getLexema() + " Linha: " + expressaoTokens.getToken().getLinha() + " Posicao: " + expressaoTokens.getToken().getPosicao());
                    }
                }
            }
            
            if(expressaoTokens.getPrioridade() == 6 || expressaoTokens.getPrioridade() == 5 || expressaoTokens.getPrioridade() == 4) {
                
                if(tipos.get(tipos.size() - 1).equals("inteiro") && tipos.get(tipos.size() - 2).equals("inteiro")) {
                    
                    tipos.remove(tipos.size() - 1);
                    
                    if(expressaoTokens.getPrioridade() == 4) {
                        
                        tipos.remove(tipos.size() - 1);
                        tipos.add("booleano");
                    }
                }
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = expressaoTokens.getToken().getLinha();
                    throw new erroCompilacao("Erro encontrado, incompatibilidade de tipos, erro encontrado: " + expressaoTokens.getToken().getLexema() + " Linha: " + expressaoTokens.getToken().getLinha() + " Posicao: " + expressaoTokens.getToken().getPosicao());
                }
            }
            
            if(expressaoTokens.getToken().getSimbolo().equals("snao")) {
                
                if(!tipos.get(tipos.size() - 1).equals("booleano")) {
                    
                    ihc.Singleton.getInstance().erroLinha = expressaoTokens.getToken().getLinha();
                    throw new erroCompilacao("Erro encontrado, incompatibilidade de tipos, erro encontrado: " + expressaoTokens.getToken().getLexema() + " Linha: " + expressaoTokens.getToken().getLinha() + " Posicao: " + expressaoTokens.getToken().getPosicao());
                }
            }
            
            if(expressaoTokens.getPrioridade() == 3) {
                
                if(tipos.get(tipos.size() - 1).equals(tipos.get(tipos.size() - 2))) {
                    
                    tipos.remove(tipos.size() - 1);
                    tipos.remove(tipos.size() - 1);
                    tipos.add("booleano");
                }
                
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = expressaoTokens.getToken().getLinha();
                    throw new erroCompilacao("Erro encontrado, incompatibilidade de tipos, erro encontrado: " + expressaoTokens.getToken().getLexema() + " Linha: " + expressaoTokens.getToken().getLinha() + " Posicao: " + expressaoTokens.getToken().getPosicao());
                }
            }
            
            if(expressaoTokens.getPrioridade() == 2 || expressaoTokens.getPrioridade() == 1) {
                
                if(tipos.get(tipos.size() - 1).equals("booleano") && tipos.get(tipos.size() - 2).equals("booleano")) {

                    tipos.remove(tipos.size() - 1);
                }
                
                else {
                    
                    ihc.Singleton.getInstance().erroLinha = expressaoTokens.getToken().getLinha();
                    throw new erroCompilacao("Erro encontrado, incompatibilidade de tipos, erro encontrado: " + expressaoTokens.getToken().getLexema() + " Linha: " + expressaoTokens.getToken().getLinha() + " Posicao: " + expressaoTokens.getToken().getPosicao());
                }
            }
        }
        
        codigoExpressao(expressaoPosfixa); // Chama a geração de código
        
        //System.err.println("tamanho: " + tipos.size() + " tipo: " + tipos.get(tipos.size() - 1));
        return tipos.get(tipos.size() - 1);
    }
    
    public void codigoExpressao(ArrayList<posfixaToken> expressaoPosfixa) { // Gera o código Assembly da expressão
        
        for(posfixaToken pf : expressaoPosfixa) {
            
            if(pf.getToken().getSimbolo().equals("sidentificador")) {
                
                String tipo = pesquisaTabela(pf.getToken().getLexema());
                
                if(tipo.equals("inteiro") || tipo.equals("booleano")) {
                    
                    codigo.geraLDV(retornaPosicaoMem(pf.getToken().getLexema()));
                }
                else {
                    
                    codigo.geraCALL(retornaNivel(pf.getToken().getLexema()));
                    codigo.geraLDV(0);
                }
            }
            else if(pf.getToken().getSimbolo().equals("snumero")) {
                
                codigo.geraLDC(Integer.parseInt(pf.getToken().getLexema()));
            }
            else if(pf.getToken().getSimbolo().equals("snao")) {
                
                codigo.geraNEG();
            }
            else if(pf.getToken().getSimbolo().equals("sverdadeiro")) {
                
                //completar
                codigo.geraLDC(1);
            }
            else if(pf.getToken().getSimbolo().equals("sfalso")) {
                
                codigo.geraLDC(0);
            }
            else if(pf.getPrioridade() == 7 && pf.getToken().getSimbolo().equals("smais")) { //verificar
                
                //codigo.geraINV();
            }
            else if(pf.getPrioridade() == 7 && pf.getToken().getSimbolo().equals("smenos")) {
                
                codigo.geraINV();
            }
            else if(pf.getToken().getSimbolo().equals("smult")) {
                
                codigo.geraMULT();
            }
            else if(pf.getToken().getSimbolo().equals("sdiv")) { //verificar
                
                codigo.geraDIVI();
            }
            else if(pf.getToken().getSimbolo().equals("smais")) {
                
                codigo.geraADD();
            }
            else if(pf.getToken().getSimbolo().equals("smenos")) {
                
                codigo.geraSUB();
            }
            else if(pf.getToken().getSimbolo().equals("sig")) {
                
                codigo.geraCEQ();
            }
            else if(pf.getToken().getSimbolo().equals("sdif")) {
                
                codigo.geraCDIF();
            }
            else if(pf.getToken().getSimbolo().equals("smenor")) {
                
                codigo.geraCME();
            }
            else if(pf.getToken().getSimbolo().equals("smenorig")) {
                
                codigo.geraCMEQ();
            }
            else if(pf.getToken().getSimbolo().equals("smaior")) {
                
                codigo.geraCMA();
            }
            else if(pf.getToken().getSimbolo().equals("smaiorig")) {
                
                codigo.geraCMAQ();
            }
            else if(pf.getToken().getSimbolo().equals("se")) {
                
                codigo.geraAND();
            }
            else if(pf.getToken().getSimbolo().equals("sou")) {
                
                codigo.geraOR();
            }
            else {
                
                throw new erroCompilacao("Erro geracao de codigo, expressao");
            }
        }
    }
}
