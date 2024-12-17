package IOHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
  sealed class InputFileData permits PublicFileManager {
    final Map<String, List<String>> subclassMap = new HashMap<>();
    private  String currentSubClass = null;

     void dataHandler() throws IOException {
        Path pathToFile =  Path.of("categories_instances.txt");
        System.out.println(pathToFile);
        List<String> lines = Files.readAllLines(pathToFile);



        for (String line : lines){
            line = line.trim();
            if (line.startsWith("<")&&line.endsWith(">")&&!line.startsWith("</")){
                currentSubClass = line.substring(1,line.length()-1);
                subclassMap.putIfAbsent(currentSubClass, new ArrayList<>());

            }
            else if(currentSubClass!=null&& line.startsWith("*")&&!line.startsWith("</")){

                line= line.replaceFirst("\\*", "");
                String[] result =  line.split(",");

                subclassMap.get(currentSubClass).add(line);

            }
        }

       /* subclassMap.forEach((subclass,instances)-> {
            System.out.println("Subclass: " + subclass);
            System.out.println("Instances: " + instances);
        });*/

    }


}
