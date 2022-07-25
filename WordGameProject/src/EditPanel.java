import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditPanel extends JPanel{
	private JTextField edit = new JTextField(15); //����ڰ� �߰��ϰų� �˻��� �ܾ� �Է�â
	private JButton addButton = new JButton("add");
	private JButton searchButton = new JButton("search");
	private JLabel result = new JLabel("��ư�� ���� �ܾ �߰��ϰų� �˻��ϼ���.");

	private JTextArea findWords = new JTextArea();
	private JScrollPane findWordsPane = new JScrollPane(findWords); //�˻������ ����� â

	public EditPanel(int language) {
		TextSource textSource= new TextSource(language);

		this.setBackground(new Color(102,204,102));
		this.setLayout(null);
		edit.setLocation(0,0);
		edit.setSize(180,30);
		edit.setFont(new Font("��������", Font.BOLD,14));
		add(edit);
		
		addButton.setLocation(180,0);
		addButton.setSize(70,30);
		addButton.setFont(new Font("��������", Font.BOLD,14));
		add(addButton);
		
		searchButton.setLocation(250,0);
		searchButton.setSize(80,30);
		searchButton.setFont(new Font("��������", Font.BOLD,12));
		add(searchButton);
		
		result.setLocation(0,50);
		result.setSize(300,30);
		result.setHorizontalAlignment(JLabel.CENTER);
		result.setFont(new Font("��������", Font.BOLD,14));
		add(result);
		
		findWordsPane.setLocation(0,85);
		findWordsPane.setSize(330,130);
		findWordsPane.getViewport().setBackground(new Color(204,255,204));
		this.add(findWordsPane);
		
		//����ڰ� �ܾ� �Է� ��
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String addWord = edit.getText();
				if(!addWord.equals("")) {
					//�߰��� �ܾ� �Է� �� �߰� ��ư �������� �̹� ����Ǿ��ִ� ���
					if(textSource.search(addWord)) {
						result.setText(addWord+"�� �̹� ����Ǿ��ֽ��ϴ�.");
						return ;
					}
					// ���� �ܾ ���� �߰��� ���
					result.setText(addWord+"�� ���Ͽ� ����Ǿ����ϴ�.");
					edit.setText("");
					textSource.add(addWord);
				}
				//�̺�Ʈ�� ������ ��
				else result.setText("�ܾ �Է��ϼ���.");
			}
		});

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text="";
				String searchWord = edit.getText();
				//�Է��� �ܾ ������ �ƴ� ��, �� ���� ���Ե� ��� �ܾ� ���
				if(!searchWord.equals("")) {
					for(int i=0; i<textSource.getSize(); i++) {
						String word = textSource.getIndexWord(i);
						if(word.length() < searchWord.length()) 
							continue;
						String frontString = word.substring(0,searchWord.length());
						if(frontString.equals(searchWord)) {
							findWords.append(word + "\n");
						}
					}
					return;
				}
				result.setText("�˻������ �����ϴ�.");
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		repaint();
	}
}
