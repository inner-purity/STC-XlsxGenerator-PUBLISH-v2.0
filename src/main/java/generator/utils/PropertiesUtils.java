package generator.utils;

import generator.constant.ExceptionConstant;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author inner-purity
 * @version 1.0
 * @description TODO
 * @date 2024/11/22 20:22
 */
public class PropertiesUtils {
    public static Map<String, String> properties = new HashMap<>();

    public static void load(String propertiesName) {
        //      读取配置文件
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Objects.requireNonNull(PropertiesUtils.class
                    .getClassLoader()
                    .getResourceAsStream(propertiesName)),
                    StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(ExceptionConstant.PROPERTIES_LOAD_FAILURE + e);
        }
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            PropertiesUtils.properties.put(entry.getKey().toString(), entry.getValue().toString());
        }
    }
}
