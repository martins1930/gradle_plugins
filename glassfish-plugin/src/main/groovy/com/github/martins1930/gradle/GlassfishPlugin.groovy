
package com.github.martins1930.gradle;


import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.WarPlugin;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.Sync;
import org.gradle.api.Task

public class GlassfishPlugin implements Plugin<Project>  {
	
    @Override
    public void apply(Project project) {
    
        project.getPlugins().apply(WarPlugin.class);
        project.extensions.create("glassfish", GlassfishExtension);

        project.configurations {
            glassfish
        }
        
        project.dependencies {
            //            glassfish "org.glassfish.main.extras:glassfish-embedded-all:3.1.2.2"
            glassfish "com.github.martins1930.gradle:glassfish-run:0.0.1-SNAPSHOT"
            providedCompile "javax:javaee-api:6.0"
        }

        // properties
        String proyName = project.name;
        String webappDirName = project.webAppDir;
        String classDirName = project.sourceSets.main.output.classesDir.absolutePath
        String resourceDirName = project.sourceSets.main.output.resourcesDir.absolutePath

        // tasks
        Task glassSync = project.task('glassfishSyncClasses', type: Sync) {
            dependsOn = ['classes']
            from classDirName
            into webappDirName+"/WEB-INF/classes"
            description = "Task to copy classes into WEB-INF directory"
            group = "glassfish"          
            doLast{
                new File(webappDirName+"/.reload").delete();
                new File(webappDirName+"/.reload").createNewFile();
            }
        }
        
        TaskContainer tc =  project.getTasks();
        Task tbuild = tc.getByName("build");
        tbuild.dependsOn(glassSync);
        
        project.task('glassfishRun', type: JavaExec){   
            main = "com.github.martins1930.gradle.MainGlassfishRun"
            classpath project.configurations.glassfish;
            classpath project.configurations.runtime;
//            classpath project.sourceSets.main.runtimeClasspath;
            args = [proyName ,
                webappDirName,
                classDirName,
                resourceDirName]
            dependsOn = ['glassfishSyncClasses']
            description = "Task to run exploded war with Glassfish"
            group = "glassfish"

        }

        

        //        project.task('glassfishRun', type: GlassfishRun){   
        //            doFirst {
        //                classPathApp = project.configurations.runtime.collect { it.absolutePath }.join(',')  ;
        //                scanInterval = project.glassfish.gfScanInterval ;
        //                port         = project.glassfish.gfPort;        
        //                portSecure   = project.glassfish.gfPortSecure;        
        //                keyStorePath = project.glassfish.gfKeyStore;        
        //                keyStorePassword = project.glassfish.gfKeyStorePassword;        
        //                deployDeps       = project.glassfish.gfDeployDeps;        
        //                automaticDeps    = project.glassfish.gfAutomaticDeps;    
        //            }
        //            dependsOn = ['build']
        //            description = "Task to run exploded war with Glassfish"
        //            group = "glassfish"
        //            contextApp = proyName;   
        //            webappDir = webappDirName;
        //            classDir = classDirName;
        //            resourceDir = resourceDirName;
        //        }
        
        
            
        
        
    }
    
}
