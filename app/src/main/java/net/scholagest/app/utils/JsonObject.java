package net.scholagest.app.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.scholagest.services.kdom.KSet;

public class JsonObject {
    private Map<String, Object> json = null;

    public JsonObject(Object... jsonEntriesNameValue) {
        this.json = new HashMap<>();

        for (int i = 0; i < jsonEntriesNameValue.length; i += 2) {
            Object key = jsonEntriesNameValue[i];
            Object value = jsonEntriesNameValue[i + 1];

            if (value instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) value;
                Object[] objs = new Object[map.size() * 2];

                int j = 0;
                for (Map.Entry<?, ?> e : map.entrySet()) {
                    objs[j] = e.getKey();
                    objs[j + 1] = e.getValue();
                    j += 2;
                }

                value = new JsonObject(objs);
            } else if (value instanceof KSet) {
                Map<String, Object> set = new HashMap<>();
                KSet kSet = (KSet) value;

                set.put("key", kSet.getKey());
                set.put("values", setToList(kSet.getValues()));
                set.put("isHtmlList", true);

                value = new JsonObject(set);
            }

            if (key instanceof String) {
                this.json.put((String) key, value);
            }
        }
    }

    private List<Object> setToList(Set<Object> values) {
        List<Object> result = new ArrayList<>();

        for (Object v : values) {
            result.add(v);
        }

        return result;
    }

    public void put(String key, Object value) {
        this.json.put(key, value);
    }

    public Object get(String key) {
        return this.json.get(key);
    }

    @Override
    public String toString() {
        String jsonString = "{";

        int i = 0;
        for (Map.Entry<String, Object> pair : this.json.entrySet()) {
            try {
                jsonString += pair.getKey() + " : ";
                if (pair.getValue() == null) {
                    jsonString += "null";
                } else {
                    String valueString = null;

                    if (pair.getValue() instanceof List<?>) {
                        valueString = "[";
                        List<?> values = (List<?>) pair.getValue();
                        for (int j = 0; j < values.size(); j++) {
                            if (j != 0) {
                                valueString += ", ";
                            }
                            valueString += values.get(i).toString();
                        }
                        valueString += "]";

                        // valueString = URLEncoder.encode(valueString,
                        // "UTF-8");
                    } else if (pair.getValue() instanceof JsonObject) {
                        valueString = pair.getValue().toString();
                    } else {
                        valueString = "\"" + URLEncoder.encode(pair.getValue().toString(), "UTF-8") + "\"";
                    }

                    jsonString += valueString;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (i < this.json.size() - 1) {
                jsonString += ", ";
            }
            i++;
        }

        jsonString += "}";

        return jsonString;
    }
}
