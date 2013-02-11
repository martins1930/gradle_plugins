/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.martins1930.gradle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
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
        }

        GlassFishProperties gfProps = new GlassFishProperties();
        gfProps.setPort("http-listener", 8096);

        GlassFish newGlassFish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
        newGlassFish.start();
        
        Deployer deployer = newGlassFish.getDeployer();

        File directory_war = new File(webappDirName);        
        
        createClassDir(classDirName, webappDirName);
        
        deployer.deploy(directory_war, "--name="+contextAppName, "--contextroot="+contextAppName, "--force=true");
        
//        log.info("listen chages in {}",classDirName);
//        Path dir = Paths.get(classDirName);
//        new WatchDir(dir).processEvents() ;
        
//        try {
//            for (int i = 0; i < 10; i++) {
//                Thread.sleep(1000);
//                log.info(".{}",i);
//            }
//            
//            log.info("classDir Deleted");
//        } catch (InterruptedException ex) {
//        }
        
        //idea 1 para reload 
        //    en classes.doLast: copiar classDir en WEB-INF,  crear archivo .reload (probar en windows)
        
        //idea 2 para reload 
        //    en java 7 nio (ver listen files en java 6): copiar classDir en WEB-INF,  crear archivo .reload (probar en windows)
        
        // comparar 1 y 2 en eficiencia de deploy 
        
        // TODO capturar interrupcion del hilo, no usar thread sleep
        // ver si se puede configurarle un realm o un domain.xml ... (probar con rootDirectory con los glassfish que ya tengo aca)
        
        
        try {
            
             while (true) { 
                 Thread.sleep(10000);
//                 deployer.deploy(directory_war, "--name="+contextApp, "--contextroot="+contextApp, "--force=true");
             }
             
        } catch (InterruptedException ex) {
        }

        newGlassFish.stop();
        deleteClassDir(webappDirName);
        newGlassFish.dispose();



    }
    
    public static void createClassDir(String classDir, String webappDir) throws IOException {
        File dirClassesWeb = new File(webappDir+"/WEB-INF/classes") ; 
        if (dirClassesWeb.exists()){
            FileUtils.deleteDirectory(dirClassesWeb) ;
        }
        boolean dirCreated = dirClassesWeb.mkdir();
        if (dirCreated) {
            File fclassDir = new File(classDir) ; 
            FileUtils.copyDirectory(fclassDir, dirClassesWeb);        
        }
    }
    
    public static void deleteClassDir(String dirName) throws IOException{
        File dirClassesWeb = new File(dirName+"/WEB-INF/classes") ; 
        if (dirClassesWeb.exists()){
            FileUtils.deleteDirectory(dirClassesWeb) ;
        }
    }    
}
