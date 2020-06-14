package isuret.polos.aether.domains;

import java.util.HashMap;
import java.util.Map;

public class Settings {

    private Map<String,Object> properties = new HashMap<>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
