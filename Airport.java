import java.util.ArrayList;

//Creates airport objects here so we can store airports and cities
public class Airport {
    String city_name;
    String alias;

    public Airport(String city_name, String alias) {
        this.city_name = city_name;
        this.alias = alias;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
