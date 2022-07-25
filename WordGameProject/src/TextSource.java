import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();

	public TextSource(int language) { // ���Ͽ��� �б�
		Scanner fScanner;
		//���Ͽ��� �о� ��� �ܾ���� vector�� ����
		try {
			if (language == 0) //����
				fScanner = new Scanner(new FileInputStream("engWords.txt"));
			else //�ѱ�
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

	//�ܾ� �˻�
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

	//�ƹ� �ܾ� ��ȯ(makeWord()���� ���)
	public String get() {
		int index = (int) (Math.random() * v.size());
		return v.get(index);
	}

	//�ش� �ε����� �������� ��, �� �ε����� �ܾ� ��ȯ(EditPanel���� ���)
	public String getIndexWord(int index) {
		return v.get(index);
	}

	//���Ϳ� �ܾ� �߰�
	public void add(String w) {
		v.add(w);
	}
}
