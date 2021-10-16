package com.taskapplication.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public final class ArrayUtil {

    private ArrayUtil() { }

    public static <T> List<T> convertArrayToList(T[] array) {
        return Arrays.stream(array).collect(Collectors.toList());
    }

    public static <T> boolean isEmpty(T[] arr) {
        return isNull(arr) || arr.length == 0;
    }
}
