package lexico;

public class token {
    
    leituraDoArquivo leituraDoArquivo;
    
    private String simbolo;
    private String lexema;
    private int linha, posicao;
    private boolean erro;
    
    public token(int linha, int posicao) {
    
        this.linha = linha;
        this.posicao = posicao;
    }
    
    public void setErro(boolean erro) {
        
        this.erro = erro;
    }
    
    public void setLexema(String lexema) {
        
        this.lexema = lexema;
    }
    
    public void setSimbolo(String simbolo) {
        
        this.simbolo = simbolo;
    }
    
    public boolean getErro() {
        
        return erro;
    }
    
    public String getLexema() {
        
        return lexema;
    }
    
    public String getSimbolo() {
        
        return simbolo;
    }
    
    public int getLinha() {
        
        return linha;
    }
    
    public int getPosicao() {
        
        return posicao;
    }
}