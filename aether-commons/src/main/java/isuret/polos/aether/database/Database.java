package isuret.polos.aether.database;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import isuret.polos.aether.domains.HotBitIntegers;
import isuret.polos.aether.domains.Settings;
import isuret.polos.aether.logs.Logger;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;

import java.io.File;
import java.io.IOException;

/**
 * <h1>Database</h1>
 * <p>A "keep-it-simple" database, using json and textfiles, as also csv, for storing data.</p>
 * <p>The data is stored in a folder hierarchie for categorizing elements and better reading.</p>
 */
public class Database {

    public static final String SETTINGS_JSON = "settings/settings.json";
    private ObjectMapper mapper;
    private ObjectWriter writer;
    private File rootFolder;
    private Logger logger = new Logger(Database.class);
    private Nitrite db;

    public Database(File rootFolder) {
        this.rootFolder = rootFolder;
        init();
    }

    public Settings getSettings() throws IOException {

        File settingsFile = getFile(SETTINGS_JSON);

        if (settingsFile.exists()) {
            return mapper.readValue(settingsFile, Settings.class);
        }

        return new Settings();
    }

    private File getFile(String relativeFilePath) {
        return new File(rootFolder.getAbsolutePath() + File.separator + "database" + File.separator + relativeFilePath);
    }

    public HotBitIntegers getHotBitPackage() throws IOException {

        File hotbitsDirectory = getFile("hotbits");

        for (File file : hotbitsDirectory.listFiles()) {
            if (file.isFile() && file.getName().startsWith("hotbits") && file.getName().endsWith("json")) {
                HotBitIntegers hotBitIntegers = mapper.readValue(file, HotBitIntegers.class);
                file.delete();
                return hotBitIntegers;
            }
        }

        return null;
    }

    public void saveSettings(Settings settings) throws IOException {
        save("settings/settings.json", settings);
    }

    public void save(String filePath, Object object) throws IOException {
        writer.writeValue(getFile(filePath), object);
        logger.info("Object " + object.getClass().getSimpleName() + " saved!");
    }

    /**
     * Initialize the database, making sure everything necessary is available.
     */
    private void init() {
        initObjectMapper();
        initFolder("settings");
        initFolder("hotbits");
        initFolder("cases");
        initFolder("queue");
        initFolder("rates");
        initFolder("images/layers");

        db = Nitrite.builder()
                .compressed()
                .filePath("aetherOneDatabase.db")
                .openOrCreate("user", "password");

        // Create a Nitrite Collection
        NitriteCollection collection = db.getCollection("test");
    }

    public void shutDown() {
        if (db != null) db.close();
    }

    private void initObjectMapper() {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        writer = mapper.writer(new DefaultPrettyPrinter());
    }

    /**
     * If the folder or path does not exist, it will be created.
     *
     * @param folderPath
     */
    private void initFolder(String folderPath) {

        File folder = new File(rootFolder.getAbsolutePath() + File.separator + "database" + File.separator + folderPath);

        if (folder.exists()) return;

        folder.mkdirs();
    }

}
