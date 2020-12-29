package ihc;

import geracaoDeCodigo.codigo;
import lexico.leituraDoArquivo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lexico.lexico;
import sintatico.sintatico;

public class Singleton {
    
    private leituraDoArquivo arquivo = new leituraDoArquivo();
    private String code = "";
    private codigo codigo;
    public int erroLinha = 0;
    
    private static Singleton uniqueInstance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new Singleton();

        return uniqueInstance;
    }
    
    public void selecionarArquivo(File path) throws FileNotFoundException {
        
        arquivo.setPath(path);
    }
    
    public boolean arquivoAberto() {
        
        return arquivo.arquivoAberto();
    }
    
    public void leitura() throws IOException {
        
        code = "";
        
        arquivo.abrirArquivo();
        
        int aux = 0;
        
        while(aux != -1) {
                
            aux = arquivo.lerCaracter();
            
            if(aux != -1)
                code += (char)aux;
        }
        
        arquivo.fecharArquivo();
    }
    
    public String printCodigo() {
        
        return code;
    }
    
    public void salvar() throws FileNotFoundException, IOException {
        
        arquivo.salvar(code);
    }
    
    public void setCodigo(String code) {
        
        this.code = code;
    }
    
    public String compilar() throws FileNotFoundException, IOException {
        
        arquivo.abrirArquivo();
        lexico lexico = new lexico(arquivo);
        lexico.analizadorLexico();
        codigo = new codigo();
        arquivo.fecharArquivo();
        
        sintatico sintatico = new sintatico(lexico, codigo);
        
        try {
        
            sintatico.analisadorSintatico();
            //System.out.println(codigo.gerarCodigo());
            return "Compilacao realizada com sucesso";
        
        } catch(erroCompilacao erroCompilacao) {
            
            return erroCompilacao.toString().substring(20);
        }
    }
    
    public String getCodigo() {
        
        codigo.gerarCodigo();
        
        while(true) {
            
            if(codigo.getPrintControl() == true) {
                
                return codigo.getCodigo();
            }
        }
    }
    
    public void salvarAssembly(File path) throws FileNotFoundException, IOException {
        
        leituraDoArquivo saveAssembly = new leituraDoArquivo();
        
        
        saveAssembly.criarArquivo(path.toString());
        //saveAssembly.abrirArquivo();
        saveAssembly.salvar(codigo.getCodigo());
        saveAssembly.fecharArquivo();
    }
    
    public int returnLinha() {
        
        if(erroLinha != 0) {
            
            return erroLinha - 1;
        }
        else {
            
            return erroLinha;
        }
    }
}
