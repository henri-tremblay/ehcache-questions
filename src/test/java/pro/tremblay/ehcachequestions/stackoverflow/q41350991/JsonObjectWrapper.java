package pro.tremblay.ehcachequestions.stackoverflow.q41350991;

import java.io.Serializable;

/**
 * @author Henri Tremblay
 */
public class JsonObjectWrapper implements Serializable {
    private static final long serialVersionUID = 3588889322413409158L;
    private Object jsonObject;

    public JsonObjectWrapper(Object jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Object getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(Object jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        return "JsonObjectWrapper{" +
               "jsonObject=" + jsonObject +
               '}';
    }
}
