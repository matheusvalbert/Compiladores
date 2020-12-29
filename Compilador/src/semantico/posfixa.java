package semantico;

import java.util.ArrayList;
import java.util.Stack;
import lexico.token;

public class posfixa {
    
    private ArrayList<posfixaToken> prioridade = new ArrayList<>();
    private ArrayList<posfixaToken> lista = new ArrayList<>();
    
    public void setPrioridade(ArrayList<token> expressao) { // Gera a prioridade e armazena no posfixaToken
        
        boolean unario = true;
        
        for(token token : expressao) {
            
            if(unario == true && (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos"))) {

                unario = false;
                posfixaToken posfixaToken = new posfixaToken();
                posfixaToken.setToken(token);
                posfixaToken.setPrioridade(7);
                prioridade.add(posfixaToken);
            }
                
            else {

                if(token.getSimbolo().equals("sabre_parenteses")) {

                    unario = true;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(-1);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("sfecha_parenteses")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(-1);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("snao")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(7);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("smult") || token.getSimbolo().equals("sdiv")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(6);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("smenos") || token.getSimbolo().equals("smais")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(5);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("smaior") || token.getSimbolo().equals("smaiorig") || token.getSimbolo().equals("smenor") || token.getSimbolo().equals("smenorig")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(4);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("sig") || token.getSimbolo().equals("sdif")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(3);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("se")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(2);
                    prioridade.add(posfixaToken);
                }

                else if(token.getSimbolo().equals("sou")) {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(1);
                    prioridade.add(posfixaToken);
                }

                else {

                    unario = false;
                    posfixaToken posfixaToken = new posfixaToken();
                    posfixaToken.setToken(token);
                    posfixaToken.setPrioridade(0);
                    prioridade.add(posfixaToken);
                }
            }
        }
    }
    
    public ArrayList<posfixaToken> geraPosfixa() { // Gera a expressão na forma posfixa
        
        Stack<posfixaToken> pilha = new Stack<>();
        
        for(posfixaToken p : prioridade) {
            
            //Thread.sleep(500);
            
            //System.out.println(prioridade.get(i).getToken().getLexema() + prioridade.get(i).getPrioridade());
            
            if(p.getPrioridade() == 0) {
                
                lista.add(p);
            }
            
            else if(p.getToken().getSimbolo().equals("sabre_parenteses")) {
                
                pilha.push(p);
            }
            
            else if(p.getToken().getSimbolo().equals("sfecha_parenteses")) {
                
                while (!pilha.isEmpty() && !pilha.peek().getToken().getSimbolo().equals("sabre_parenteses")) {
                    
                    lista.add(pilha.pop());    
                }
                  
                pilha.pop();
            }
            
            else {
                
                if(!pilha.isEmpty() && pilha.peek().getToken().getSimbolo().equals("snao") && p.getToken().getSimbolo().equals("snao")) {
                    
                    pilha.push(p);
                }
                
                else {
                
                    while (!pilha.isEmpty() && p.getPrioridade() <= pilha.peek().getPrioridade()) { 

                        lista.add(pilha.pop());
                    }

                    pilha.push(p);
                }
            }
        }
        
        while (!pilha.isEmpty()) {
            
            lista.add(pilha.pop());
        }
        
        //for(int i = 0; i < lista.size(); i++) {
            
            //System.err.print(lista.get(i).getToken().getLexema());
        //}
        
        //System.err.println("");
        
        return lista;
    }
}