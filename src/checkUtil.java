import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class checkUtil {
    public static void main(String[] args) {
        try {
            // Read pom.xml
            File fXmlFile = new File("/home/turja/git/devices/lib/parser/pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Parse the parserName
            NodeList nList = doc.getElementsByTagName("modules");
            Node node = nList.item(0);
            NodeList children = node.getChildNodes();

            ArrayList<String> parserName = new ArrayList<String>();
            for (int temp = 0; temp < children.getLength(); temp++) {
                Node nNode = children.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    parserName.add(nNode.getTextContent());
                }
            }

            //Read parser files
            for (int i = 0; i< parserName.size(); i++) {
                String fileName = "/home/turja/git/devices/lib/parser/" + parserName.get(i) + "/src/main/java/veriflow/parser";
                String parserFile = new File(fileName).listFiles()[0].getName();
                fileName = fileName + "/" + parserFile;

                if(fileName.matches(".*common.*")) {
                    continue;
                }

                parserFile = new File(fileName).listFiles()[0].getName();
                fileName = fileName + "/" + parserFile;
                System.out.println("Opening file: " + fileName);
                String line = null;
                String[] keys = {".*PROPERTY_INTERFACE_STATS_OUTPUT_UTILIZATION.*", ".*PROPERTY_INTERFACE_STATS_INPUT_UTILIZATION.*", ".*fillTempIntfProperties().*"};

                FileReader fw = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(fw);
                int lineNumber = 1;
                int matchCount = 0;
                while((line = bufferedReader.readLine()) != null) {
                    for (final String key : keys) {
                        if (line.matches(key)) {
                            System.out.println(parserName.get(i) + ":" + lineNumber + " --> (" +key+ ")" + line);
                            matchCount++;
                        }
                    }

                    lineNumber++;
                }

                if(matchCount > 0) {
                    System.out.println("Found " + matchCount + " matches in here");
                } else {
                    System.out.println("No match found");
                }
                // Always close files.
                bufferedReader.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
