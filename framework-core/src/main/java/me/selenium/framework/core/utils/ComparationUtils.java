package me.selenium.framework.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComparationUtils {
    public ComparationUtils() {
    }

    public static boolean compareJsonString(String expectedString, String actualString) {
        ObjectMapper om = new ObjectMapper();

        try {
            Map<String, Object> expectedObject = (Map)om.readValue(expectedString, Map.class);
            Map<String, Object> actualObject = (Map)om.readValue(actualString, Map.class);
            return expectedObject.equals(actualObject);
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static int compareObjectsByOrder(Object obj1, Object obj2, String order) throws Exception {
        int result = 0;
        if (!order.equalsIgnoreCase("desc") && !order.equalsIgnoreCase("asc") && !order.contains(">")) {
            throw new Exception("The order by should be: 'asc' or 'desc' or '[value1 > value2 > ... > valueN]'");
        } else {
            if (order.equalsIgnoreCase("desc")) {
                if (comparePropertiesValue(obj1, obj2) < 0) {
                    result = -1;
                } else if (comparePropertiesValue(obj1, obj2) > 0) {
                    result = 1;
                }
            } else if (order.equalsIgnoreCase("asc")) {
                if (comparePropertiesValue(obj1, obj2) > 0) {
                    result = -1;
                } else if (comparePropertiesValue(obj1, obj2) < 0) {
                    result = 1;
                }
            } else {
                List<String> expectedOrder = Arrays.asList(order.split(">"));
                expectedOrder = (List)expectedOrder.stream().map(String::trim).collect(Collectors.toList());
                if (expectedOrder.indexOf(obj1) > expectedOrder.indexOf(obj2)) {
                    result = -1;
                } else if (expectedOrder.indexOf(obj1) < expectedOrder.indexOf(obj2)) {
                    result = 1;
                }
            }

            return result;
        }
    }

    private static int comparePropertiesValue(Object obj1, Object obj2) {
        int output = 0;
        if (obj1 instanceof Integer) {
            Integer number1 = (Integer)obj1;
            Integer number2 = (Integer)obj2;
            output = number1.compareTo(number2);
        }

        if (obj1 instanceof String) {
            String string1 = (String)obj1;
            String string2 = (String)obj2;
            output = string1.compareToIgnoreCase(string2);
        }

        if (obj1 instanceof Long) {
            Long number1 = (Long)obj1;
            Long number2 = (Long)obj2;
            output = number1.compareTo(number2);
        }

        return output;
    }
}
