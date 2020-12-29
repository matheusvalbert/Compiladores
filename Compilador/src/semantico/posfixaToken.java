package semantico;

import lexico.token;

public class posfixaToken {
    
    private token token;
    private int prioridade;
    
    public void setToken(token token) {
        
        this.token = token;
    }
    
    public void setPrioridade(int prioridade) {
        
        this.prioridade = prioridade;
    }
    
    public token getToken() {
        
        return token;
    }
    
    public int getPrioridade() {
        
        return prioridade;
    }
}
