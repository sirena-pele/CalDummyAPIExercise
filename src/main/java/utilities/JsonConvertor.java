package utilities;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class JsonConvertor {

    public static JSONObject getJSONObject(String resourceName) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceStream = loader.getResourceAsStream(resourceName);
        if (resourceStream == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokenizer = new JSONTokener(resourceStream);
        return new JSONObject(tokenizer);
    }

}
