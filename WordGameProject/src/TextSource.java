import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();

	public TextSource(int language) { // 파일에서 읽기
		Scanner fScanner;
		//파일에서 읽어 모든 단어들을 vector에 저장
		try {
			if (language == 0) //영어
				fScanner = new Scanner(new FileInputStream("engWords.txt"));
			else //한글
				fScanner = new Scanner(new InputStreamReader(new FileInputStream("korWords.txt"), "UTF-8"));

			while (fScanner.hasNext()) {
				String word = fScanner.next();
				v.add(word.trim());
			}
			fScanner.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//단어 검색
	public boolean search(String word) {
		for (int i = 0; i < v.size(); i++) {
			if (v.get(i).equals(word))
				return true;
		}
		return false;
	}

	public int getSize() {
		return v.size();
	}

	//아무 단어 반환(makeWord()에서 사용)
	public String get() {
		int index = (int) (Math.random() * v.size());
		return v.get(index);
	}

	//해당 인덱스를 정해줬을 때, 그 인덱스의 단어 반환(EditPanel에서 사용)
	public String getIndexWord(int index) {
		return v.get(index);
	}

	//벡터에 단어 추가
	public void add(String w) {
		v.add(w);
	}
}
