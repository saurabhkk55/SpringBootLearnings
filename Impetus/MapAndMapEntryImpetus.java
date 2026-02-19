package Impetus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapAndMapEntryImpetus {
    static void main() {
        List<String> inputList = Arrays.asList("match");

        List<Map<String, String>> listOfMap = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1","something");
        Map<String, String> map2 = new HashMap<>();
        map2.put("key2","something");
        Map<String, String> map3 = new HashMap<>();
        map3.put("key3","match");
        Map<String, String> map4 = new HashMap<>();
        map4.put("key4","match");

        listOfMap.add(map1);
        listOfMap.add(map2);
        listOfMap.add(map3);
        listOfMap.add(map4);

//        For every map within listOfMap, if its value is present in inputList,
//        then extract the k-v pair to outputMap as below
//        -----------------------------------------
//        outputMap
//        {
//            "key3":"match",
//            "key4":"match"
//        }
//        -----------------------------------------

        Stream<Map<String, String>> streamOfMap = listOfMap.stream(); // return entire Map
        Stream<Map.Entry<String, String>> entryMapStream = streamOfMap.flatMap(map -> map.entrySet().stream()); // return entries of map(s)
        Stream<Map.Entry<String, String>> entryMapStream1 = entryMapStream.filter(mapEntry -> inputList.contains(mapEntry.getValue()));
        Map<String, String> outputMap = entryMapStream1.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(outputMap);

    }
}

// Map contains one or multiple entries where each entry consist a Key & a value.
// On Map we can use method like <map_name>.get(<key>), <map_name>.containsKey(<key>)
// Whereas on Map.Entry we can use method like getKey() & getValue()
