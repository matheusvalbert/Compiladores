package code;

public class Instrucoes {

    private int[] M = new int[1000];
    private int s = 0, i = 0;
    
    public Instrucoes () {}
	
    public void executaAssembly (String comando, int arg1, int arg2) {
        
        switch (comando) {
        case "LDC":
            LDC(arg1);
            break;
        case "LDV":
            LDV(arg1);
            break;
        case "ADD":
            ADD();
            break;
        case "SUB":
            SUB();
            break;
        case "MULT":
            MULTI();
            break;
        case "DIVI":
            DIVI();
            break;
        case "INV":
            INV();
            break;
        case "AND":
            AND();
            break;
        case "OR":
            OR();
            break;
        case "NEG":
            NEG();
            break;
        case "CME":
            CME();
            break;
        case "CMA":
            CMA();
            break;
        case "CEQ":
            CEQ();
            break;
        case "CDIF":
            CDIF();
            break;
        case "CMEQ":
            CMEQ();
            break;
        case "CMAQ":
            CMAQ();
            break;
        case "START":
            START();
            break;
        case "HLT":
            HLT();
            break;
        case "STR":
            STR(arg1);
            break;
        case "JMP":
            i = i - 1;
            JMP(arg1 - 1);
            break;
        case "JMPF":
            i = i - 1;
            JMPF(arg1 - 1);
            break;
        case "NULL":
            NULL();
            break;
        case "RD":
            RD();
            break;
        case "PRN":
            PRN();
            break;
        case "ALLOC":
            ALLOC(arg1, arg2);
            break;
        case "DALLOC":
            DALLOC(arg1, arg2);
            break;
        case "CALL":
            i = i - 1;
            CALL(arg1 - 1);
            M[s] = M[s] + 1;
            break;
        case "RETURN":
            M[s] = M[s] - 1;
            RETURN();
            break;
        default:
            break;
        }
    }
	
    public void LDC (int arg1) {
        
        s = s + 1;
        M[s] = arg1;
    }

    public void LDV (int arg1) {

        s = s + 1;
        M[s] = M[arg1];
    }

    public void ADD () {

        M[s - 1] = M[s - 1] + M[s];
        s = s - 1;
    }

    public void SUB () {

        M[s - 1] = M[s - 1] - M[s];
        s = s - 1;
    }

    public void MULTI () {

        M[s - 1] = M[s - 1] * M[s];
        s = s - 1;
    }

    public void DIVI () {

        M[s - 1] = M[s - 1] / M[s];
        s = s - 1;
    }

    public void INV () {
        
        M[s] = (-1) * M[s];
    }

    public void AND () {

        if (M[s - 1] == 1 && M[s] == 1) {

            M[s - 1] = 1;
        }

        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void OR () {

        if (M[s - 1] == 1 || M[s] == 1) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;	
        }

        s = s - 1;
    }

    public void NEG () {

        M[s] = 1 - M[s];
    }

    public void CME () {

        if(M[s -1] < M[s]) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void CMA () {

        if(M[s -1] > M[s]) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void CEQ () {

        if(M[s -1] == M[s]) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void CDIF () {

        if(M[s -1] != M[s]) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void CMEQ () {

        if(M[s -1] <= M[s]) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void CMAQ () {

        if(M[s -1] >= M[s]) {

            M[s - 1] = 1;
        }
        else {

            M[s - 1] = 0;
        }

        s = s - 1;
    }

    public void START () {

        s = -1;
    }

    public void HLT () {}

    public void STR (int arg1) {

        M[arg1] = M[s];
        s = s - 1;
    }

    public void JMP (int arg1) {

        i = arg1;
    }

    public void JMPF (int arg1) {

        if(M[s] == 0) {

            i = arg1;
        }
        else {

            i = i + 1;
        }

        s = s - 1;
    }

    public void NULL () {

    }

    public void RD () {
        
        Singleton singleton = Singleton.getInstance();

        s = s + 1;
        M[s] = singleton.entradaGet();
    }

    public void PRN () {
        
        Singleton singleton = Singleton.getInstance();

        singleton.saidaSet(M[s]);
        s = s - 1;
    }

    public void ALLOC (int arg1, int arg2) {

        for (int k = 0; k <= arg2 - 1; k++) {

            s = s + 1;
            M[s] = M[arg1 + k];
        }
    }

    public void DALLOC (int arg1, int arg2) {

        for (int k = arg2 - 1; k >= 0; k--) {

            M[arg1 + k] = M[s];
            s = s - 1;
        }
    }

    public void CALL (int arg1) {

        s = s + 1;
        M[s] = i + 1;
        i = arg1;
    }

    public void RETURN () {

        i = M[s];
        s = s - 1;
    }
    
    public int[] getM() {

        return M;
    }
    
    public void reset() {
        
        M = new int[1000];
        s = 0;
        i = 0;
    }
    
    public int getI() {
        
        return i;
    }
    
    public void proximaInstrucao() {
        
        i = i + 1;
    }
}