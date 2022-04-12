package net.halflex.mythic.config;

import io.lumine.mythic.bukkit.utils.config.file.FileConfiguration;
import io.lumine.mythic.bukkit.utils.config.file.YamlConfiguration;
import net.halflex.mythic.Constants;
import net.halflex.mythic.NotMythicScript;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigurationLoader<T extends JavaPlugin> {

    private File file;
    private FileConfiguration configuration;
    private String defaultFile;

    public ConfigurationLoader(String file){
        this(file,null);
    }

    public ConfigurationLoader(String file, String folder){
        this.defaultFile = file;

        if(folder != null)  {
            folder = folder.replace("/", File.separator);

            final String path = Constants.MYTHIC_FOLDER + File.separator + folder;
            final File dir = new File(path);
            if(!dir.exists())	{
                dir.mkdir();
            }
            this.file = new File(Constants.MYTHIC_FOLDER + File.separator + folder, file);
        } else  {
            this.file = new File(Constants.MYTHIC_FOLDER, file);
        }

        loadFile(!this.file.exists());

    }

    public ConfigurationLoader(T plugin, File newfile, String folder) {

        file = newfile;

        loadFile((file == null));
    }


    public void loadFile(boolean defaults){
        if (defaults && this.file != null){

            this.file = new File(this.file.getParent(), defaultFile);
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
            InputStream inputStream = NotMythicScript.inst().getResource(defaultFile);
            if (inputStream != null){
                Reader reader = new InputStreamReader(inputStream);
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
                configuration.setDefaults(defConfig);
            }
            getConfig().options().copyDefaults(true);
            save();
        } else {
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    public File getFile(){
        return this.file;
    }

    public FileConfiguration getConfig(){
        if (this.configuration == null){
            loadFile(file == null);
        }
        return this.configuration;
    }

    public void save(){
        if (configuration == null || file == null) return;
        try{
            getConfig().save(this.file);
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
