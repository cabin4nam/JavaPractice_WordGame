import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Ranking {
	private HashMap<String, Integer> h = new HashMap<String, Integer>(); //파일에서 불러온 랭킹 저장
	private Set<String> keys = h.keySet();
	private Iterator<String> it = keys.iterator();
	private List<String> listKeySet;

	private File file = new File("top10.txt");

	public Ranking() { // 파일에서 읽기
		Scanner fScanner;
		//파일에서 읽어 해시맵에 순위 저장
		try {
			fScanner = new Scanner(new FileReader(file));
			while (fScanner.hasNext()) {
				String user = fScanner.next();
				int score = fScanner.nextInt();
				h.put(user, score);
			}
			fScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//해시맵에 데이터 추가
	public void add(String user, int score) {
		h.put(user, score);
	}

	//높은 점수 순으로 정렬하여 다시 파일에 저장
	public void sort() {
		listKeySet = new ArrayList<>(h.keySet());
		listKeySet.sort((value1, value2) -> (h.get(value2) - (h.get(value1))));

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < h.size(); i++) {
				writer.write(listKeySet.get(i) + " ");
				writer.write(Integer.toString(h.get(listKeySet.get(i))) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getSize() {
		return h.size();
	}

	//사용자 ID와 해당 점수 반환
	public String getId(int index) {
			return listKeySet.get(index);
	}

	public int getScore(String name) {
			return h.get(name);
	}

}
