/**/

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] names = {
                "강유미", "김경호", "김동현", "김서영", "김수빈", "김인엽", "김정규", "김종호",
                "김준영", "김지윤", "김진이", "박상민", "서정희", "송인범", "오화랑", "왕승철",
                "이지은", "이찬민", "이형창", "정현석", "조승기", "최병익", "태희쌤", "민주프로님"
        };
        HashMap<String, String> name2mission = new HashMap<>();
        List<String> missions = new ArrayList<>();

        // 히든 미션 입력
        BufferedReader br = Files.newBufferedReader(Paths.get("personalMission.txt"));
        String personalName = "";
        while((personalName = br.readLine()) != null) {
            name2mission.put(personalName, br.readLine());
        }

        br = Files.newBufferedReader(Paths.get("commonMission.txt"));
        String commonMission = "";
        while((commonMission = br.readLine()) != null) {
            missions.add(commonMission);
        }

        List<String> giverList = new ArrayList<>();
        List<String> receiverList = new ArrayList<>();
        int missionIndex = 0;

        for (String name : names) {
            giverList.add(name);
            receiverList.add(name);
        }

        Collections.shuffle(receiverList);
        while (!isAllowed(giverList, receiverList)) {
            Collections.shuffle(receiverList);
        }

        String uuidMapFileName = "name" + "_map.txt";
        try {
            FileWriter mapFileWriter = new FileWriter(uuidMapFileName);
            for (String name : names) {
                FileWriter uuidMapFileWriter = new FileWriter("uuidmap.txt");
                String uuid = generateUUID();
                String line = name + ": " + uuid + "\n";
                uuidMapFileWriter.write(line);

                String folderPath = uuid + "/";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs(); // 폴더 생성
                }

                String fileName = uuid + "/" + name;
                FileWriter fileWriter = new FileWriter(fileName);
                String receiver = receiverList.get(giverList.indexOf(name));
                fileWriter.write(name + "님의 마니또는 " + receiver + "님입니다. \n");
                if (name2mission.containsKey(receiver)) {
                    fileWriter.write("히든 미션은" + name2mission.get(receiver) + " 입니다.");
                } else {
                    fileWriter.write("히든 미션은" + missions.get(missionIndex) + " 입니다.");
                    missionIndex = ((missionIndex + 1) % missions.size());
                }

                fileWriter.close();

                String mapLine = name + ": " + uuid + "\n";
                mapFileWriter.write(mapLine);

            }
            mapFileWriter.close();
            System.out.println("결과가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일을 저장하는 동안 오류가 발생했습니다: " + e.getMessage());
        }

    }

    private static boolean isAllowed(List<String> giverList, List<String> receiverList) {
        for (int i = 0; i < giverList.size(); i++) {
            if (giverList.get(i).equals(receiverList.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
