import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel {
	// �̹���
	private Image background = new ImageIcon("backGround.jpg").getImage();
	private ImageIcon wormIcon = new ImageIcon("worm.png");
	private ImageIcon fullApple = new ImageIcon("apple.png");
	private ImageIcon halfApple = new ImageIcon("apple2.png");
	private ImageIcon fullLemon = new ImageIcon("lemon.png");
	private ImageIcon halfLemon = new ImageIcon("lemon2.png");
	private ImageIcon fullGrape = new ImageIcon("grape.png");
	private ImageIcon halfGrape = new ImageIcon("grape2.png");
	private ImageIcon[][] wordFruit = { { fullApple, halfApple }, { fullLemon, halfLemon }, { fullGrape, halfGrape } };

	private JTextField input = new JTextField(50); // ����� �ܾ� �Է� â
	private int wordCount = 0; // ������� ���� �ܾ��� ��
	private int level; // ���� ������ ��������
	private int language; // ���� ������ ���
	private int fallingSpeed = 10;
	private int slowTime; // �ֹ����� ���� �ܾ ������ �� �������� �ð��� �����ϱ� ���� ����
	private Vector<JLabel> wordList = new Vector<JLabel>(); // �������� �ܾ�� ������ ����Ʈ

	// gamePanel ���� ���� �����ǰ� ������
	private ScorePanel scorePanel = null;
	private EditPanel editPanel = null;
	private MainFrame mainFrame = null;

	public GamePanel(ScorePanel scorePanel, EditPanel editPanel, MainFrame mainFrame, int language, int level) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;
		this.mainFrame = mainFrame;
		this.language = language;
		this.level = level;

		scorePanel.setVisible(true);
		editPanel.requestFocus();

		setLayout(new BorderLayout());
		add(new GameGroundPanel(), BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);
	}

	class GameGroundPanel extends JPanel {
		private TextSource textSource = new TextSource(language); // words.txt���� �ܾ� �������� ����� ����
		FallingThread ft = null;
		private boolean gameOn = true; // ������ ���� ���������� �ƴ���

		public GameGroundPanel() {
			// ����ڰ� �ܾ� �Է��� ���� �̺�Ʈ
			input.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField) (e.getSource());
					String inputWord = t.getText();
					t.setText(""); // â�� �Էµ� �ؽ�Ʈ �����
					GameGroundPanel.this.matchWord(inputWord);
				}
			});

			// ���� �����ϱ�
			if (gameOn) {
				reset();
				ft = new FallingThread();
				ft.start();
				repaint();
			}
		}

		// �ʱ�ȭ�Լ�
		public void reset() {
			gameOn = true;
			scorePanel.setHeart(3);

			wordList.clear();
			wordCount = 0;
			slowTime = 0;
			repaint();
		}

		class FallingThread extends Thread {
			@Override
			public void run() {
				int time = 0;
				while (true) {
					// Ȩ��ư ������ ��
					if (scorePanel.getExit()) {
						GamePanel.this.setVisible(false);
						scorePanel.setVisible(false);
						editPanel.setVisible(false);
						mainFrame.exitGame();
						reset();
						break;
					}

					// ���÷��� ��ư ������ ��
					if (scorePanel.getRestart()) {
						reset();
						GameGroundPanel.this.removeAll();
						scorePanel.setRestart(false);
						scorePanel.setExit(false);
						continue;
					}

					// ���������� ���� ����
					while (gameOn && !scorePanel.getExit() && !scorePanel.getRestart()) {
						try {
							Thread.sleep(200);
							// 7�ʿ� �ѹ��� ���� ��� �ܾ� ����
							if (time % 7 == 0) {
								makeWord();
								wordCount++;
							}
							// 20�ʿ� �ѹ��� �ֹ��� ��� �ܾ� ����
							if (time % 20 == 0 && time != 0) {
								makeWorm();
								wordCount++;
							}
							// �ֹ����� ���� �ܾ��Է��� 10�� ������ �ٽ� �ӵ� ����
							if (slowTime % 10 == 0 && slowTime != 0) {
								fallingSpeed = 10;
							}
							fallingWord(); // �ܾ���� �Ʒ��� �������� ������
							GameGroundPanel.this.repaint();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						time++;
						slowTime++;
					}
					repaint();
				}
			}
		}

		//���� ��� �ܾ� ����� �Լ�
		private void makeWord() {
			int whichFruit = (int) (Math.random() * 2);
			JLabel word = new JLabel(textSource.get(), wordFruit[level][whichFruit], SwingConstants.CENTER);
			setLayout(null);
			word.setSize(500, 80);
			word.setFont(new Font("�������", Font.BOLD, 20)); // ���̺� ������ ��Ʈ�� �����Ѵ�.
			word.setLocation((int) (Math.random() * 280), 0);

			wordList.add(word);
			this.add(word);

		}
		
		//�ֹ��� ��� �ܾ� ����� �Լ�
		private void makeWorm() {
			JLabel worm = new JLabel(textSource.get(), wormIcon, SwingConstants.CENTER);
			setLayout(null);
			worm.setSize(500, 80);
			worm.setFont(new Font("�������", Font.BOLD, 20));
			worm.setLocation((int) (Math.random() * 280), 0);

			wordList.add(worm);
			this.add(worm);
		}

		//�ܾ �Ʒ��� �������� ������
		private void fallingWord() {
			for (int i = 0; i < wordList.size(); i++) {
				JLabel w = wordList.get(i);
				w.setLocation(w.getX(), w.getY() + (level * 5) + fallingSpeed);

				// ����� �ٴڿ� ������ ��� �پ��.
				if (w.getY() >= GameGroundPanel.this.getHeight() - 80 && w.getIcon() != wormIcon) {
					scorePanel.setHeart((scorePanel.getHeart()) - 1);
					if (scorePanel.getHeart() <= 0) {
						gameOn = false; //��� 3�� ��� ���� �� ���� ����
					}
					wordList.remove(i);
					this.remove(w);
				}
			}
		}

		//����ڰ� �ùٸ��� �Է��ߴ��� �Ǵ�
		public boolean matchWord(String inputWord) {
			for (int i = 0; i < wordList.size(); i++) {
				if (wordList.get(i).getText().equals(inputWord)) {
					wordList.get(i).setVisible(false);
					//������ �����̸� 10�� ����
					if (wordList.get(i).getIcon() == fullApple || wordList.get(i).getIcon() == fullLemon
							|| wordList.get(i).getIcon() == fullGrape)
						scorePanel.fullIncrease();
					//�������� ���� �����̸� 5�� ����
					else if (wordList.get(i).getIcon() == halfApple || wordList.get(i).getIcon() == halfLemon
							|| wordList.get(i).getIcon() == halfGrape)
						scorePanel.halfIncrease();
					//�ֹ��� ��� �ܾ�� �ӵ� ����, slowTime Ÿ�̸� ����
					else if (wordList.get(i).getIcon() == wormIcon) {
						fallingSpeed = 3;
						slowTime = 0;
					}
					wordList.remove(i);
					return true;
				}
			}
			return false;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(background, 0, 0, 820, 730, this);

			//���� ���� ��
			if (!gameOn && !scorePanel.getRestart()) {
				for (int i = 0; i < this.getComponentCount(); i++) {
					this.getComponents()[i].setVisible(false);
				}
				g.setFont(new Font("Arial", Font.BOLD, 80));
				g.setColor(Color.red);
				g.drawString("Game Over", 100, 340);

				g.setFont(new Font("�������", Font.BOLD, 45));
				g.setColor(Color.black);
				g.drawString("�������� ��ŷ�� Ȯ���ϼ���", 43, 440);
			}

			repaint();
		}
	}

	//����� �Է� �г�
	class InputPanel extends JPanel {
		private Image inputBackground = new ImageIcon("inputBackground.png").getImage();

		public InputPanel() {
			setLayout(new FlowLayout());
			add(input);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(inputBackground, 0, 0, this.getWidth(), this.getHeight(), 0, 0, this.getWidth(),
					this.getHeight(), this);
			repaint();
		}
	}
}
