package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configurador {

    ArrayList<String> archivos;
    String rutaLog;
    ArrayList<Integer> poblacion;
    Integer porcIndiAl;
    Integer greedyAlea;
    ArrayList<Integer> esquemaEvolucionElite;
    Integer kBest;
    Integer kWorst;

    Integer porcentajeOX2;

    Integer porcentajeCruce;
    ArrayList<String> algoritmo;
    Integer kDiferencial;
    Integer porcentajeMutacion;

    ArrayList<Long> semillas;

    ArrayList<Integer> condicionParada;

    public Configurador(String ruta) {
        archivos = new ArrayList<>();
        poblacion = new ArrayList<>();
        esquemaEvolucionElite = new ArrayList<>();
        condicionParada = new ArrayList<>();
        algoritmo = new ArrayList<>();
        semillas = new ArrayList<>();
        rutaLog = new String();

        String linea;
        FileReader f = null;

        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            while ( (linea = b.readLine()) != null ){
                String[] split = linea.split("=");
                switch (split[0]){
                    case "Archivos":

                        String[] v = split[1].split(" ");

                        for (int i = 0; i < v.length; i++) {
                            archivos.add(v[i]);}
                    break;

                    case "Poblacion":

                        String[] s = split[1].split(" ");

                        for (int i = 0; i < s.length; i++) {
                            poblacion.add(Integer.parseInt(s[i]));
                        }
                    break;
                    case "Semillas":

                        String[] se = split[1].split(" ");

                        for (int i = 0; i < se.length; i++) {
                            semillas.add(Long.parseLong(se[i]));
                        }
                        break;

                    case "PorcentajeIndividuosAleatoria":

                        porcIndiAl = Integer.parseInt(split[1]);
                        
                    break;
                    case "PorcentajeOx2":

                        porcentajeOX2 = Integer.parseInt(split[1]);

                        break;

                    case "kDiferencial":

                        kDiferencial = Integer.parseInt(split[1]);

                        break;

                    case "GreedyAleatorizado":

                        greedyAlea = Integer.parseInt(split[1]);

                        break;
                    case "RutaLogs":

                            try{
                                rutaLog = split[1];
                            }catch (ArrayIndexOutOfBoundsException ex){
                                rutaLog = "."+ File.separator;
                            }
                        break;

                    case "EsquemaEvolucionElite":

                        String[] eli = split[1].split(" ");

                        for (int i = 0; i < eli.length; i++) {
                            esquemaEvolucionElite.add(Integer.parseInt(eli[i]));
                        }

                        break;

                    case "KBest":

                        kBest = Integer.parseInt(split[1]);

                        break;

                    case "KWorst":

                        kWorst = Integer.parseInt(split[1]);

                        break;

                    case "Algoritmos":
                        String[] alg = split[1].split(" ");

                        for (int i = 0; i < alg.length; i++) {
                            algoritmo.add(alg[i]);
                        }

                        break;

                    case "PorcentajeOperadorCruce":

                        porcentajeCruce = Integer.parseInt(split[1]);

                        break;

                    case "PorcentajeMutacion":

                        porcentajeMutacion = Integer.parseInt(split[1]);

                        break;

                    case "CondicionParada":

                        String[] parada = split[1].split(" ");

                        for (int i = 0; i < parada.length; i++) {
                            condicionParada.add(Integer.parseInt(parada[i]));
                        }

                        break;

                }

            }


        }catch (IOException ex){
            System.err.println("Error a leer el archivo configurador: "+ex);
        }


    }

    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public String getRutaLog() {
        return rutaLog;
    }

    public ArrayList<Integer> getPoblacion() {
        return poblacion;
    }

    public Integer getPorcIndiAl() {
        return porcIndiAl;
    }

    public Integer getGreedyAlea() {
        return greedyAlea;
    }

    public ArrayList<Integer> getEsquemaEvolucionElite() {
        return esquemaEvolucionElite;
    }

    public Integer getkBest() {
        return kBest;
    }

    public Integer getkWorst() {
        return kWorst;
    }

    public Integer getPorcentajeCruce() {
        return porcentajeCruce;
    }

    public Integer getPorcentajeMutacion() {
        return porcentajeMutacion;
    }

    public ArrayList<Integer> getCondicionParada() {
        return condicionParada;
    }

    public ArrayList<Long> getSemillas() { return semillas; }

    public Integer getPorcentajeOX2() {
        return porcentajeOX2;
    }

    public Integer getkDiferencial() {
        return kDiferencial;
    }

    public ArrayList<String> getAlgoritmo() {
        return algoritmo;
    }
}

