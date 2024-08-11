package clasesAuxiliares;



import IO.Configurador;
import clases.Poblacion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestionaLog {
    private StringBuilder log;

    private StringBuilder logCostes;
    private File carpeta;

    public GestionaLog(Configurador config) {
        log = new StringBuilder();
        carpeta = new File(config.getRutaLog());
        logCostes = new StringBuilder();
    }



    public void registraLog(String texto){
        log.append(texto);
    }

    public void escribeFichero(String ruta){
        FileWriter fichero = null;
        PrintWriter pw = null;

        try {

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            fichero = new FileWriter(ruta);
            pw = new PrintWriter(fichero);

            pw.print(log.toString());


        }catch (IOException ex){
        }
        finally {
            try{
                if (null != fichero){
                    fichero.close();
                }
            }catch (IOException e2){
            }
        }
    }

    public void escribeIndividuos(Poblacion pT){
        this.registraLog("\n"+"---------------POBLACION TOTAL---------------"+"\n");
        for (int j = 0; j < pT.getPoblacion().length; j++) {
            this.registraLog("Individuo "+j+": "+ pT.getPoblacion()[j].getFitness()+"\n");
        }
        this.registraLog("---------------FIN POBLACION TOTAL---------------"+"\n\n");
    }


    public void escribeIteraciones(Poblacion pT,Integer i){
        if (i == Integer.MAX_VALUE){
            this.registraLog("Iteracion Final: ");
            for (int j = 0; j < pT.getElites().length; j++) {
                this.registraLog("Elite "+j+": "+ pT.getElites()[j].getFitness()+"  ");
            }
            this.registraLog("\n");
        }else {
            this.registraLog("Iteracion "+i+": ");
            for (int j = 0; j < pT.getElites().length; j++) {
                this.registraLog("Elite "+j+": "+ pT.getElites()[j].getFitness()+"  ");
            }
            this.registraLog("\n");
        }
    }
}
