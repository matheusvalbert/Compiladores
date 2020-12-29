package code;

import java.io.File;
import java.util.ArrayList;

public final class Singleton {
	
    Arquivo arquivo = new Arquivo();
    Instrucoes instrucoes = new Instrucoes();

    private ArrayList<String> instrucoesTexto = new ArrayList<>();
    private ArrayList<String[]> arrayDeLinhas = new ArrayList<>();
    private ArrayList<String> saida = new ArrayList<>();
    private int entrada; 
    
    public boolean leitura = false;
    public int flag;

    private static Singleton uniqueInstance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (uniqueInstance == null)
            uniqueInstance = new Singleton();

        return uniqueInstance;
    }

    public void lerArquivo (File lerArq) {

        instrucoesTexto = arquivo.leitura_arquivos(lerArq);
    }

    public ArrayList<String[]> criaArrayDeLinhas () {

        arrayDeLinhas = arquivo.colocaMatriz();

        return arrayDeLinhas;
    }

    public ArrayList<String[]> getArrayDeLinhas () {

        return arrayDeLinhas;
    }

    public int[] retornaI() {

        return instrucoes.getM();
    }

    public boolean executa() {

        if(arrayDeLinhas.size() == instrucoes.getI()) {

            return true;
        }
        else {

            if(arrayDeLinhas.get(instrucoes.getI())[1] == null)
                arrayDeLinhas.get(instrucoes.getI())[1] = "999";
            if(arrayDeLinhas.get(instrucoes.getI())[2] == null)
                arrayDeLinhas.get(instrucoes.getI())[2] = "999";
            instrucoes.executaAssembly(arrayDeLinhas.get(instrucoes.getI())[0], Integer.parseInt(arrayDeLinhas.get(instrucoes.getI())[1]), Integer.parseInt(arrayDeLinhas.get(instrucoes.getI())[2]));
            instrucoes.proximaInstrucao();
            if (!arrayDeLinhas.get(instrucoes.getI() - 1)[0].equals("HLT"))
                if (arrayDeLinhas.get(instrucoes.getI())[0].equals("RD"))
                    leitura = true;

            return false;
        }
    }

    public boolean leitura() {

        return leitura;
    }

    public void entradaSet(int in) {

        entrada = in;
        leitura = false;
    }

    public int entradaGet() {

        return entrada;
    }

    public void saidaSet(int saida) {

        this.saida.add(String.valueOf(saida));
    }

    public String[] saidaGet() {

        String[] retorno = new String[saida.size()];
        saida.toArray(retorno);

        return retorno;
    }

    public void reset(boolean p) {

        if(p == true) {

            instrucoesTexto.clear();
            arrayDeLinhas.clear();   
        }
 
        leitura = false;
        saida.clear();
        instrucoes.reset();
    }
    
    public int getI() {
        
        return instrucoes.getI();
    }
}
