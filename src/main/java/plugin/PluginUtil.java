package plugin;

import java.io.File;

/**
 * Created by Declan on 29/04/2016.
 */
public final class PluginUtil {

	public static File getDataFolder(String folder){
		File dataFolder = new File("plugindata/" + folder);
		dataFolder.mkdirs();

		return dataFolder;
	}

}
