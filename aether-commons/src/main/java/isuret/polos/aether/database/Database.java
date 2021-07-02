package isuret.polos.aether.database;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import isuret.polos.aether.domains.*;
import isuret.polos.aether.logs.Logger;
import org.apache.commons.io.FileUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteBuilder;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.filters.ObjectFilters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * <h1>Database</h1>
 * <p>A "keep-it-simple" database, using json and textfiles, as also csv, for storing data.</p>
 * <p>The data is stored in a folder hierarchie for categorizing elements and better reading.</p>
 * <p>Typical session related data is stored inside Nitrite, which can be exported to files if needed.</p>
 */
public class Database {

    public static final String SETTINGS_JSON = "settings/settings.json";
    private ObjectMapper mapper;
    private ObjectWriter writer;
    private File rootFolder;
    private Logger logger = new Logger(Database.class);
    private Map<String,Nitrite> databases = new HashMap<>();

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

    public List<String> getRatesFromPath(String path) {

        List<String> filenames = new ArrayList<>();

        File folder = new File(path);

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith("txt")) {
                    filenames.add(file.getName());
                }
            }
        }

        return filenames;
    }

    public List<Rate> getRates(String ratePathName) {

        List<Rate> rateList = new ArrayList<>();

        try {
            List<String> lines = FileUtils.readLines(getFile("rates" + File.separator + ratePathName), Charset.forName("UTF-8"));

            for (String line : lines) {
                if (line.length() > 0) {
                    Rate rate = new Rate();

                    if (line.contains("\t")) {
                        String parts[] = line.split("\t");
                        rate.setName(parts[0].trim());
                        rate.setUrl(parts[1].trim());
                    } else {
                        rate.setName(line.trim());
                    }

                    rateList.add(rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rateList;
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

    public List<Case> getAllCases(User user) {
        Nitrite db = getDatabase(user);
        List<Case> simpleCases = new ArrayList<>();
        for (Case caseObject : db.getRepository(Case.class).find().toList()) {
            simpleCases.add(caseObject.makeShallowCopy());
        }

        Collections.sort(simpleCases, new Comparator<Case>() {
            @Override
            public int compare(Case o1, Case o2) {
                Calendar a1 = o1.getCreated();
                Calendar a2 = o2.getCreated();

                if (o1.getLastChange() != null) {
                    a1 = o1.getLastChange();
                }

                if (o2.getLastChange() != null) {
                    a2 = o2.getLastChange();
                }

                return a2.compareTo(a1);
            }
        });

        return simpleCases;
    }

    public List<String> getAllCasesNames(User user) {
        Nitrite db = getDatabase(user);
        Cursor<Case> caseCursor = db.getRepository(Case.class).find();
        List<String> caseList = new ArrayList<>();

        for (Case caseObject : caseCursor) {
            caseList.add(caseObject.getName());
        }

        Collections.sort(caseList);

        return caseList;
    }

    public Case readCase(User user, String caseName) {
        Nitrite db = getDatabase(user);
        return db.getRepository(Case.class).find(ObjectFilters.eq("name",caseName)).firstOrDefault();
    }

    public void saveOrUpdateCase(User user, Case caseObject) {
        Nitrite db = getDatabase(user);

        if (caseObject.getUuid() == null) {
            caseObject.setUuid(UUID.randomUUID());
        }

        if (readCase(user, caseObject.getName()) != null) {
            db.getRepository(Case.class).update(caseObject);
        } else {
            db.getRepository(Case.class).insert(caseObject);
        }
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
    }

    private Nitrite getDatabase(User user) {

        // UserName is unique as you can see here
        if (databases.get(user.getUsername()) == null) {

            // Create a db
            Nitrite db = getNitriteBuilder(user)
                    .openOrCreate(user.getUsername(), user.getPassword());
            databases.put(user.getUsername(), db);
        }

        return databases.get(user.getUsername());
    }

    private NitriteBuilder getNitriteBuilder(User user) {
        return Nitrite.builder()
                .compressed()
                .filePath(rootFolder.getPath() + "/" + user.getUsername().replaceAll(" ","").trim() + "_aetherOneDatabase.db");
    }

    public void shutDown() {
        for (Nitrite db : databases.values()) {
            db.close();
        }
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
