package isuret.polos.aether.domains;

public class Rate {

    private Integer energeticValue = 0;
    private Integer gv = 0;
    private String name;
    private String url;

    public Integer getGv() {
        return gv;
    }

    public void setGv(Integer gv) {
        this.gv = gv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getEnergeticValue() {
        return energeticValue;
    }

    public void setEnergeticValue(Integer energeticValue) {
        this.energeticValue = energeticValue;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "energeticValue=" + energeticValue +
                ", gv=" + gv +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
