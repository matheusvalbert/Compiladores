package lexico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class leituraDoArquivo {
    
    private InputStreamReader entradaFormatada;
    private File localArq;
    
    public void setPath(File localArq) {
        
        this.localArq = localArq;
    }
    
    public boolean arquivoAberto() {
        
        if(localArq != null)
            return true;
        else
            return false;
    }
    
    public void abrirArquivo() throws FileNotFoundException {
        
        FileInputStream entrada = new FileInputStream(localArq);
        entradaFormatada = new InputStreamReader(entrada);
    }
    
    public int lerCaracter() throws IOException {
        
        int caracter = entradaFormatada.read();
        
        if(caracter != -1) {
            
            return caracter;
        }
        else {
            
            return -1;
        }
    }
    
    public void fecharArquivo() throws IOException {
        
        entradaFormatada.close();
    }
    
    public void salvar(String codigo) throws FileNotFoundException, IOException {
        
        PrintWriter writer = new PrintWriter(localArq);
        
        writer.print(codigo);
        
        writer.close();
        
        abrirArquivo();
    }
    
    public void criarArquivo(String path) {
        
        localArq = new File(path);
    }
}
