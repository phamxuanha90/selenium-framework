package me.selenium.framework.core.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.selenium.framework.core.helper.AutomationTestException;
import me.selenium.framework.core.utils.ComparationUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Asserts {
    public Asserts() {
    }

    public static void assertEquals(Object first, Object second, String... assertType) throws AutomationTestException {
        if (!Objects.equals(first, second)) {
            throw new AutomationTestException(MessageFormat.format(AssertionMessage.EQUAL_ERROR_MESSAGE.getValue(), assertType.length > 0 ? assertType[0] : "", first, second));
        }
    }

    public static void assertNotEquals(Object first, Object second, String... assertType) throws AutomationTestException {
        if (Objects.equals(first, second)) {
            throw new AutomationTestException(MessageFormat.format(AssertionMessage.NOT_EQUAL_ERROR_MESSAGE.getValue(), assertType.length > 0 ? assertType[0] : "", first, second));
        }
    }

    public static void assertEquals(List<Object> first, List<Object> second, Comparator<Object> comparator, String... assertType) throws Exception {
        first.sort(comparator);
        second.sort(comparator);
        assertEqualsInOrder(first, second, assertType);
    }

    public static void assertEqualsInOrder(List<Object> first, List<Object> second, String... assertType) throws Exception {
        if (!first.equals(second)) {
            List<Object> missing = new ArrayList<>(second);
            missing.removeAll(first);
            List<Object> extra = new ArrayList<>(first);
            extra.removeAll(second);
            throw new Exception(MessageFormat.format(AssertionMessage.LIST_EQUAL_ERROR_MESSAGE.getValue(), assertType.length > 0 ? assertType[0] : "", first, second, missing, extra));
        }
    }

    public static void assertContainsInOrder(List<Object> first, List<Object> second, String... assertType) throws Exception {
        Stream<Object> firstStream = first.stream();
        //second.getClass();
        List<Object> commonList = Arrays.asList(firstStream.filter(second::contains).toArray(Object[]::new));
        assertEqualsInOrder(commonList, second, assertType);
    }

    public static void assertContains(List<Object> first, List<Object> second, String... assertType) throws AutomationTestException {
        if (!first.containsAll(second)) {
            List<Object> missing = new ArrayList<>(second);
            missing.removeAll(first);
            throw new AutomationTestException(MessageFormat.format(AssertionMessage.LIST_CONTAIN_ERROR_MESSAGE.getValue(), assertType.length > 0 ? assertType[0] : "", first, second, missing));
        }
    }

    public static void assertNotContains(List<Object> first, List<Object> second, String... assertType) throws AutomationTestException {
        Stream<Object> firstStream = first.stream();
        //second.getClass();
        List<Object> commonList = firstStream.filter(second::contains).collect(Collectors.toList());
        if (commonList.size() > 0) {
            throw new AutomationTestException(MessageFormat.format(AssertionMessage.LIST_NOT_CONTAIN_ERROR_MESSAGE.getValue(), assertType.length > 0 ? assertType[0] : "", first, second, commonList));
        }
    }

    public static void assertInExpectedOrder(List<Object> listObjects, String expectedOrder, String... assertType) throws Exception {
        boolean ordered = true;
        if (listObjects.size() > 1) {
            List<Map<String, String>> listOrders = extractOrder(expectedOrder);
            Class<?> clazz = listObjects.get(0).getClass();

            for (int i = 0; i < listObjects.size() - 1; ++i) {
                Object current = listObjects.get(i);
                Object next = listObjects.get(i + 1);

                for (Map<String, String> listOrder : listOrders) {
                    String fieldName = listOrder.get("field");
                    String order = listOrder.get("order");
                    Field field = FieldUtils.getField(clazz, fieldName, true);
                    if (field == null) {
                        Field[] allFields = FieldUtils.getAllFields(clazz);
                        int var15 = allFields.length;

                        for (Field tmpField : allFields) {
                            JsonProperty declaredAnnotation = (JsonProperty) tmpField.getDeclaredAnnotation(JsonProperty.class);
                            if (declaredAnnotation != null && declaredAnnotation.value().equals(fieldName)) {
                                field = tmpField;
                                break;
                            }
                        }
                    }

                    if (field == null) {
                        throw new NoSuchFieldException("The class " + clazz.getName() + " have no field " + fieldName);
                    }

                    field.setAccessible(true);
                    if (order.contains(">")) {
                        List<String> listExpectedOrder = Arrays.asList(order.split(">"));
                        listExpectedOrder = listExpectedOrder.stream().map(String::trim).collect(Collectors.toList());
                        if (!listExpectedOrder.contains(field.get(current))) {
                            break;
                        }

                        if (!listExpectedOrder.contains(field.get(next))) {
                            ++i;

                            while (!listExpectedOrder.contains(field.get(next)) && i < listObjects.size() - 1) {
                                next = listObjects.get(i + 1);
                                ++i;
                            }

                            if (i == listObjects.size() - 1) {
                                break;
                            }
                        }
                    }

                    int compareResult = ComparationUtils.compareObjectsByOrder(field.get(current), field.get(next), order);
                    if (compareResult > 0) {
                        break;
                    }

                    if (compareResult < 0) {
                        ordered = false;
                        break;
                    }
                }

                if (!ordered) {
                    break;
                }
            }

            if (!ordered) {
                throw new AutomationTestException(MessageFormat.format(AssertionMessage.LIST_NOT_SORTED.getValue(), assertType.length > 0 ? assertType[0] : "", expectedOrder, listObjects.stream().map(Object::toString).collect(Collectors.joining("\n"))));
            }
        }

    }

    public static void assertStringsInExpectedOrder(List<String> listStrings, String expectedOrder, String... assertType) throws Exception {
        boolean ordered = true;
        if (listStrings.size() > 1) {
            for (int i = 0; i < listStrings.size() - 1; ++i) {
                String current = (String) listStrings.get(i);
                String next = (String) listStrings.get(i + 1);
                if (expectedOrder.contains(">")) {
                    List<String> listExpectedOrder = Arrays.asList(expectedOrder.split(">"));
                    listExpectedOrder = listExpectedOrder.stream().map(String::trim).collect(Collectors.toList());
                    if (!listExpectedOrder.contains(current)) {
                        continue;
                    }

                    if (!listExpectedOrder.contains(next)) {
                        ++i;

                        while (!listExpectedOrder.contains(next) && i < listStrings.size() - 1) {
                            next = (String) listStrings.get(i + 1);
                            ++i;
                        }

                        if (i == listStrings.size() - 1) {
                            break;
                        }
                    }
                }

                int compareResult = ComparationUtils.compareObjectsByOrder(current, next, expectedOrder);
                if (compareResult <= 0) {
                    ordered = false;
                    break;
                }
            }

            if (!ordered) {
                throw new AutomationTestException(MessageFormat.format(AssertionMessage.LIST_NOT_SORTED.getValue(), assertType.length > 0 ? assertType[0] : "", expectedOrder, listStrings.stream().map(Object::toString).collect(Collectors.joining("\n"))));
            }
        }

    }

    private static List<Map<String, String>> extractOrder(String orders) throws Exception {
        List<Map<String, String>> listOrders = new ArrayList<>();
        String[] arrayOrderBys = orders.split(",");
        int len = arrayOrderBys.length;

        for (String arrayOrderBy : arrayOrderBys) {
            String orderBy = arrayOrderBy;
            Map<String, String> mapOrder = new HashMap<>();
            orderBy = orderBy.trim();
            if (orderBy.contains(" in_order ")) {
                mapOrder.put("order", orderBy.split("in_order")[1].trim());
                mapOrder.put("field", orderBy.split("in_order")[0].trim());
            } else {
                String[] listItems = orderBy.split(" ");
                String order = listItems[listItems.length - 1];
                String fieldName = orderBy.replace(order, "").trim();
                mapOrder.put("order", order);
                if (!order.equalsIgnoreCase("desc") && !order.equalsIgnoreCase("asc")) {
                    throw new AutomationTestException("The order by should be: '[Field] asc' or '[Field] desc' or '[Field] in_order [value1 > value2 >..> valueN]'");
                }

                mapOrder.put("field", fieldName);
            }

            listOrders.add(mapOrder);
        }

        return listOrders;
    }
}
