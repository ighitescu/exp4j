package io.luan.exp4j.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generic object comparator
 *
 */
public class ObjectComparator {

    private static Map<String, Field[]> REFLECTION_CACHE = new HashMap<>();

    private static Map<String, String> diff;

    /**
     * Main entrance point
     * <p>
     * Each time the diff is cleared
     */
    public static Map<String, String> compare(Object obj1, Object obj2) {
        diff = new HashMap<>();

        compareInternal("root", obj1, obj2);

        return diff;
    }

    /**
     * return true if a problem is found
     */
    private static boolean compareInternal(String name, Object obj1, Object obj2) {
        System.out.println(name + " @ COMPARING: " + obj1 + " -> " + obj2);

        if ((obj1 == null && obj2 != null) || (obj1 != null && obj2 == null)) {
            diff.put(name + ".nullcheck", obj1 + "|" + obj2);
            return true;
        }

        if (obj1 == null || obj1.equals(obj2)) {
            return false;
        }

        Class clazz1 = obj1.getClass();

        if (!clazz1.equals(obj2.getClass())) {
            diff.put(name + ".getClass", clazz1 + "|" + obj2.getClass());
            return true;
        }

        if (clazz1.isArray()) {
            System.out.println("---array");
        }

        if (obj1 instanceof List) {
            return compareList(name, (List) obj1, (List) obj2);
        }

        if (obj1 instanceof Map) {
            return compareMap(name, (Map) obj1, (Map) obj2);
        }

        if (obj1 instanceof Set) {
            return compareSet(name, (Set) obj1, (Set) obj2);
        }

        // Other java primitive types
        if (clazz1.getName().startsWith("java")) {
            diff.put(name, obj1 + "|" + obj2);
            return true;
        }

        // finally field by field comparison
        Field[] fields = REFLECTION_CACHE.get(clazz1.getName());
        if (fields == null) {
            fields = clazz1.getDeclaredFields();
            REFLECTION_CACHE.put(clazz1.getName(), fields);
        }

        for (Field field : fields) {
            if ((field.getModifiers() & Modifier.STATIC) != 0) {
                // System.out.println("STATIC: " + field);
                continue;
            }

            if ((field.getModifiers() & Modifier.FINAL) != 0) {
                //System.out.println("FINAL: " + field);
                continue;
            }

            System.out.println("\tFIELD: " + field);

            Object val1 = null;
            Object val2 = null;
            try {
                field.setAccessible(true);
                val1 = field.get(obj1);
                val2 = field.get(obj2);
                if (compareInternal(name + "." + field.getName(), val1, val2)) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                diff.put(clazz1.getName() + "." + field.getName() + "[IllegalAccess]", val1 + "|" + val2);
                return true;
            }

        }

        return false;
    }

    /**
     * return true if problem is found
     */
    private static boolean compareSet(String name, Set set1, Set set2) {
        System.out.println(name + " @ COMPARING Set: " + set1 + " -> " + set2);

        if (set1.size() != set2.size()) {
            diff.put(name + ".size", set1.size() + "|" + set2.size());
            return true;
        }

        for (Object key : set1) {
            if (!set2.contains(key)) {
                diff.put(name + ".notContains", key + "");
            }
        }

        return false;
    }

    /**
     * return true if problem is found
     */
    private static boolean compareList(String name, List list1, List list2) {
        System.out.println(name + " @ COMPARING List: " + list1 + " -> " + list2);

        if (list1.size() != list2.size()) {
            diff.put(name + ".size", list1.size() + "|" + list2.size());
            return true;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (compareInternal(name + "[" + i + "]", list1.get(i), list2.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * return true if problem is found
     */
    private static boolean compareMap(String name, Map map1, Map map2) {
        System.out.println(name + " @ COMPARING Map: " + map1 + " -> " + map2);

        if (map1.size() != map2.size()) {
            diff.put(name + ".size", map1.size() + "|" + map2.size());
            return true;
        }

        for (Object key : map1.keySet()) {
            Object val1 = map1.get(key);
            Object val2 = map2.get(key);
            if (compareInternal(name + "[" + key + "]", val1, val2)) {
                return true;
            }
        }

        return false;
    }

}
