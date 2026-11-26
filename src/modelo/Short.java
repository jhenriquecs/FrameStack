package modelo;

public class Short extends Video {

    private String tag; // ex: "#bastidores"

    public Short() {
        super();
    }

    public Short(String titulo, String data, String linkSite, double nota, String tag) {
        super(titulo, data, linkSite, nota);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Short{tag='" + tag + "'} " + super.toString();
    }
}
