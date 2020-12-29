package semantico;

public class simbolo {
    
    private String nome, tipo;
    private boolean nivel;
    private int rotulo, posMem;
    
    public void setNome(String nome) {
        
        this.nome = nome;
    }
    
    public String getNome() {
        
        return nome;
    }
    
    public void setTipo(String tipo) {
        
        this.tipo = tipo;
    }
    
    public String getTipo() {
        
        return tipo;
    }
    
    public void setRotulo(int rotulo) {
        
        this.rotulo = rotulo;
    }
    
    public int getRotulo() {
        
        return rotulo;
    }
    
    public void setPosMem(int posMem) {
        
        this.posMem = posMem;
    }
    
    public int getPosMem() {
        
        return posMem;
    }
    
    public void setNivel(boolean nivel) {
        
        this.nivel = nivel;
    }
    
    public boolean getNivel() {
        
        return nivel;
    }
}
