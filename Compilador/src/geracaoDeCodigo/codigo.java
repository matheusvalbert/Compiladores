package geracaoDeCodigo;

import java.util.ArrayList;
import java.util.Stack;

public class codigo {

    private ArrayList<String> comandosGerados = new ArrayList<>();
    
    private int totalVar = 0, rotulo = 0;
    private Stack<Integer> pilhaVar = new Stack<>();
    private boolean printControl = false; // Variável de controle de print da IHC
    private String codigo;

    public void setTotalVar(int totalVar) { // Escreve o total de varáveis no TOTAL
        
        this.totalVar = totalVar;
    }
    
    public int getTotalVar() { // Pega o total de varáveis no TOTAL
        
        return totalVar;
    }
    
    public void pushPilhaVar(int nvariaveis) { // Empilha a quantidade correta para ALLOC e DALLOC
        
        pilhaVar.push(nvariaveis);
    }
    
    public int popPilhaVar() { // Desempilha a quantidade correta para ALLOC e DALLOC
        
        return pilhaVar.pop();
    }
    
    public void setRotulo(int rotulo) { // Próximo Label guardado
        
        this.rotulo = rotulo;
    }
    
    public int getRotulo() { // Pega o Label
        
        return rotulo;
    }
    
    public void geraLDC(int k) {
        String comando = "\t" + "LDC" + "\t" + String.valueOf(k) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraLDV(int n) {
        String comando = "\t" + "LDV" + "\t" + String.valueOf(n) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraADD() {
        String comando = "\t" + "ADD" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraSUB() {
        String comando = "\t" + "SUB" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraMULT() {
        String comando = "\t" + "MULT" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraDIVI() {
        String comando = "\t" + "DIVI" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraINV() {
        String comando = "\t" + "INV" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraAND() {
        String comando = "\t" + "AND" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraOR() {
        String comando = "\t" + "OR" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraNEG() {
        String comando = "\t" + "NEG" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraCME() {
        String comando = "\t" + "CME" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraCMA() {
        String comando = "\t" + "CMA" + "\n";
        comandosGerados.add(comando);
    }
    public void geraCEQ() {
        String comando = "\t" + "CEQ" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraCDIF() {
        String comando = "\t" + "CDIF" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraCMEQ() {
        String comando = "\t" + "CMEQ" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraCMAQ() {
        String comando = "\t" + "CMAQ" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraSTART() {
        String comando = "\t" + "START" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraHLT() {
        String comando = "\t" + "HLT" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraSTR(int n) {
        String comando = "\t" + "STR" + "\t" + String.valueOf(n) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraJMP(int t) {
        String comando = "\t" + "JMP" + "\t" + "L" + String.valueOf(t) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraJMPF(int t) {
        String comando = "\t" + "JMPF" + "\t" + "L" + String.valueOf(t) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraNULL(int label) {
        String comando = "L" + String.valueOf(label) + "\t" + "NULL" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraRD() {
        String comando = "\t" + "RD" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraPRN() {
        String comando = "\t" + "PRN" + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraALLOC(int m, int n) {
        String comando = "\t" + "ALLOC" + "\t" + String.valueOf(m) + "," + String.valueOf(n) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraDALLOC(int m, int n) {
        String comando = "\t" + "DALLOC" + "\t" + String.valueOf(m) + "," + String.valueOf(n) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraCALL(int t) {
        String comando = "\t" + "CALL" + "\t" + "L" + String.valueOf(t) + "\n";
        comandosGerados.add(comando);
    }
    
    public void geraRETURN() {
        String comando = "\t" + "RETURN" + "\n";
        comandosGerados.add(comando);
    }
    
    public void gerarCodigo() {
        codigo = "";
        
        for(String linha : comandosGerados) {
            codigo += linha;
        }
        
        printControl = true;
    }
    
    public String getCodigo() {
        
        printControl = false;
        return codigo;
    }
    
    public boolean getPrintControl() {
        
        return printControl;
    }
}
