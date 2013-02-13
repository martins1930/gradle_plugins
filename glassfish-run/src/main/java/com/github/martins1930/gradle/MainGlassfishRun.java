/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.martins1930.gradle;

import java.io.File;
import java.io.IOException;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainGlassfishRun {

    private static final Logger log = LoggerFactory.getLogger(MainGlassfishRun.class);

    public static void main(String[] args) throws GlassFishException, IOException {
        log.info("Starting glassfish...");
        
        
        String contextAppName = "", webappDirName = "", classDirName = "", resourceDirName = "" ;
        Integer port_http = 8080, port_https = null ;
        
        for (int i = 0; i < args.length; i++) {
            String currentArg = args[i] ;
            log.info("actual arg: {}", currentArg);
            if (i==0) {
                contextAppName = currentArg;
            }
            
            if (i==1) {
                webappDirName = currentArg;
            }
            
            if (i==2) {
                classDirName = currentArg;
            }
            
            if (i==3) {
                resourceDirName = currentArg;
            }
            if (i==4 && currentArg!=null && !currentArg.equals("")) {
                port_http = Integer.parseInt(currentArg);
            }
            if (i==5 && currentArg!=null && !currentArg.equals("")) {
                port_https = Integer.parseInt(currentArg);
            }
        }

        GlassFishProperties gfProps = new GlassFishProperties();
        gfProps.setPort("http-listener", port_http);

        final GlassFish newGlassFish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
        newGlassFish.start();
//        
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() { 
//                log.info("Shutdown...");
//                try {
//                    newGlassFish.stop();
//            //        deleteClassDir(webappDirName);
//                    newGlassFish.dispose();
//                } catch (GlassFishException ex) {
//                    log.error("Error shoutdown glassfish:", ex);
//                }
//            }
//         });
        
        
        
        Deployer deployer = newGlassFish.getDeployer();

        File directory_war = new File(webappDirName);        
        
        deployer.deploy(directory_war, "--name="+contextAppName, "--contextroot="+contextAppName, "--force=true");
 
        
        // TODO ver si se puede configurarle un realm o un domain.xml ... (probar con rootDirectory con los glassfish que ya tengo aca)
        
        
        try {
            
             while (true) { 
                 Thread.sleep(100000);
//                 deployer.deploy(directory_war, "--name="+contextApp, "--contextroot="+contextApp, "--force=true");
             }
             
        } catch (InterruptedException ex) {
        }

//        newGlassFish.stop();
//        deleteClassDir(webappDirName);
//        newGlassFish.dispose();



    }
    
//    
//    public static void deleteClassDir(String dirName) throws IOException{
//        File dirClassesWeb = new File(dirName+"/WEB-INF/classes") ; 
//        if (dirClassesWeb.exists()){
//            FileUtils.deleteDirectory(dirClassesWeb) ;
//        }
//    }    
}
