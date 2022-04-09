package net.pumbas.deadlinebot;

import java.util.ArrayList;
import java.util.List;

public class Utils
{
    public static <T> List<T> listOf(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }
}
