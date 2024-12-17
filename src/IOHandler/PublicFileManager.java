package IOHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

final public class PublicFileManager extends InputFileData {
     InputFileData IFD = new InputFileData();
    public Map<String, List<String>> subclassMap = IFD.subclassMap;


    public void dataStarter() throws IOException {
        IFD.dataHandler();
    }
    public void printAllProducts() throws IOException {
        dataStarter();
        subclassMap.forEach((subclass,instances)-> {
            System.out.println("Subclass: " + subclass);
            System.out.println("Instances: " + instances);
        });
    }
    public Map getSubclassMap(){
        return subclassMap;
    }
}
