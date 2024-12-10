package com.sonnguyen.storageservice.utils;

import com.sonnguyen.storageservice.specification.DynamicSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SearchUtils {
    public static List<DynamicSearch> parseOperator(Map<String, String[]> parameterMap) {
        String regex = "(\\w+)\\((.*?)\\)";
        List<DynamicSearch> searchItem = new ArrayList<>();
        for (Map.Entry<String, String[]> set : parameterMap.entrySet()) {
            if (set.getKey().equals("page") || set.getKey().equals("size") || set.getKey().equals("order")) continue;
            for (String value : set.getValue()) {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(value);
                if (matcher.matches()) {
                    String searchValue = matcher.group(2);
                    DynamicSearch.Operator operator = DynamicSearch.Operator.valueOf(matcher.group(1).toUpperCase());
                    searchItem.add(new DynamicSearch(set.getKey(), searchValue, operator));
                    log.info("Search with key: {} value: {} operator: {}", set.getKey(), searchValue, operator);
                } else {
                    searchItem.add(new DynamicSearch(set.getKey(), value, DynamicSearch.Operator.DEFAULT_LIKE));
                    log.info("Search with key: {} value: {}", set.getKey(), value);
                }
            }
        }
        return searchItem;
    }

    public static List<Sort.Order> parseSort(Map<String, String[]> parameterMap) {
        List<Sort.Order> sortItem = new ArrayList<>();
        String[] ordersParam = parameterMap.get("order");
        if (ordersParam == null || ordersParam.length < 1) return List.of();
        for (String param : ordersParam) {
            String[] extractOrder = param.split("[()]");
            if (extractOrder.length == 2) {
                String field = extractOrder[1];
                log.info("Sort by {} direction: {}", field, extractOrder[0]);
                Sort.Direction direction = Sort.Direction.valueOf(extractOrder[0]);
                sortItem.add(new Sort.Order(direction, field));
            } else throw new InvalidParameterException(String.format("Invalid sort parameter near %s", param));
        }

        return sortItem;
    }
}
