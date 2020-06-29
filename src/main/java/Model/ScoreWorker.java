package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ScoreWorker {

    private Map<Long, String> scoreTable;
    private String namePlayer;

    public ScoreWorker() {
        scoreTable = new TreeMap<>();
        namePlayer = "anonymous";

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/ScoreTable.txt"))) {

        /*try{
            InputStream scoreFile = this.getClass().getClassLoader().getResourceAsStream("ScoreTable.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(scoreFile));*/

            String line;
            while (null != (line = reader.readLine())) {
                int splitter = line.indexOf(",");
                String name = line.substring(0, splitter);
                String score = line.substring(splitter + 1, line.length());

                scoreTable.put(Long.parseLong(score), name);
            }

        } catch (Exception e) {
            System.out.println("Error. ScoreWorker.GetScoreFromFile(): " + e);
        }

    }

    public void writeScoreTableToFile() {
        try (FileWriter writer = new FileWriter("src/main/resources/ScoreTable.txt", false)) {
            for(Map.Entry t: scoreTable.entrySet()){
                writer.write(t.getValue() + "," + t.getKey() + "\n");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public List<String> getScoreTable() {
        List<String> scoreTableList = new ArrayList<>();
        for(Map.Entry elem: scoreTable.entrySet()){
            String str = elem.getValue() + " = " + elem.getKey() + " sec.";
            scoreTableList.add(str);
        }
        return scoreTableList;
    }

    public void addNewScore(Long score) {
        scoreTable.put(score, namePlayer);
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String newName) {
        namePlayer = newName;
    }
}
