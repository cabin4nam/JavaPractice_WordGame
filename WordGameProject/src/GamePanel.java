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
	// 이미지
	private Image background = new ImageIcon("backGround.jpg").getImage();
	private ImageIcon wormIcon = new ImageIcon("worm.png");
	private ImageIcon fullApple = new ImageIcon("apple.png");
	private ImageIcon halfApple = new ImageIcon("apple2.png");
	private ImageIcon fullLemon = new ImageIcon("lemon.png");
	private ImageIcon halfLemon = new ImageIcon("lemon2.png");
	private ImageIcon fullGrape = new ImageIcon("grape.png");
	private ImageIcon halfGrape = new ImageIcon("grape2.png");
	private ImageIcon[][] wordFruit = { { fullApple, halfApple }, { fullLemon, halfLemon }, { fullGrape, halfGrape } };

	private JTextField input = new JTextField(50); // 사용자 단어 입력 창
	private int wordCount = 0; // 현재까지 만들어낸 단어의 수
	private int level; // 현재 게임의 스테이지
	private int language; // 현재 게임의 언어
	private int fallingSpeed = 10;
	private int slowTime; // 애벌레에 적힌 단어를 맞췄을 때 느려지는 시간을 조절하기 위한 변수
	private Vector<JLabel> wordList = new Vector<JLabel>(); // 떨어지는 단어들 저장할 리스트

	// gamePanel 옆에 붙을 점수판과 수정판
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
		private TextSource textSource = new TextSource(language); // words.txt에서 단어 가져오는 기능을 위함
		FallingThread ft = null;
		private boolean gameOn = true; // 게임이 현재 진행중인지 아닌지

		public GameGroundPanel() {
			// 사용자가 단어 입력한 후의 이벤트
			input.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField) (e.getSource());
					String inputWord = t.getText();
					t.setText(""); // 창에 입력된 텍스트 지우기
					GameGroundPanel.this.matchWord(inputWord);
				}
			});

			// 게임 시작하기
			if (gameOn) {
				reset();
				ft = new FallingThread();
				ft.start();
				repaint();
			}
		}

		// 초기화함수
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
					// 홈버튼 눌렀을 때
					if (scorePanel.getExit()) {
						GamePanel.this.setVisible(false);
						scorePanel.setVisible(false);
						editPanel.setVisible(false);
						mainFrame.exitGame();
						reset();
						break;
					}

					// 리플레이 버튼 눌렀을 때
					if (scorePanel.getRestart()) {
						reset();
						GameGroundPanel.this.removeAll();
						scorePanel.setRestart(false);
						scorePanel.setExit(false);
						continue;
					}

					// 정상적으로 게임 진행
					while (gameOn && !scorePanel.getExit() && !scorePanel.getRestart()) {
						try {
							Thread.sleep(200);
							// 7초에 한번씩 과일 모양 단어 생성
							if (time % 7 == 0) {
								makeWord();
								wordCount++;
							}
							// 20초에 한번씩 애벌레 모양 단어 생성
							if (time % 20 == 0 && time != 0) {
								makeWorm();
								wordCount++;
							}
							// 애벌레에 적힌 단어입렵후 10초 지나면 다시 속도 증가
							if (slowTime % 10 == 0 && slowTime != 0) {
								fallingSpeed = 10;
							}
							fallingWord(); // 단어들이 아래로 떨어지는 움직임
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

		//과일 모양 단어 만드는 함수
		private void makeWord() {
			int whichFruit = (int) (Math.random() * 2);
			JLabel word = new JLabel(textSource.get(), wordFruit[level][whichFruit], SwingConstants.CENTER);
			setLayout(null);
			word.setSize(500, 80);
			word.setFont(new Font("나눔고딕", Font.BOLD, 20)); // 레이블 글자의 폰트를 설정한다.
			word.setLocation((int) (Math.random() * 280), 0);

			wordList.add(word);
			this.add(word);

		}
		
		//애벌레 모양 단어 만드는 함수
		private void makeWorm() {
			JLabel worm = new JLabel(textSource.get(), wormIcon, SwingConstants.CENTER);
			setLayout(null);
			worm.setSize(500, 80);
			worm.setFont(new Font("나눔고딕", Font.BOLD, 20));
			worm.setLocation((int) (Math.random() * 280), 0);

			wordList.add(worm);
			this.add(worm);
		}

		//단어가 아래로 떨어지는 움직임
		private void fallingWord() {
			for (int i = 0; i < wordList.size(); i++) {
				JLabel w = wordList.get(i);
				w.setLocation(w.getX(), w.getY() + (level * 5) + fallingSpeed);

				// 사과가 바닥에 닿으면 목숨 줄어듦.
				if (w.getY() >= GameGroundPanel.this.getHeight() - 80 && w.getIcon() != wormIcon) {
					scorePanel.setHeart((scorePanel.getHeart()) - 1);
					if (scorePanel.getHeart() <= 0) {
						gameOn = false; //목숨 3개 모두 소진 시 게임 종료
					}
					wordList.remove(i);
					this.remove(w);
				}
			}
		}

		//사용자가 올바르게 입력했는지 판단
		public boolean matchWord(String inputWord) {
			for (int i = 0; i < wordList.size(); i++) {
				if (wordList.get(i).getText().equals(inputWord)) {
					wordList.get(i).setVisible(false);
					//온전한 과일이면 10점 증가
					if (wordList.get(i).getIcon() == fullApple || wordList.get(i).getIcon() == fullLemon
							|| wordList.get(i).getIcon() == fullGrape)
						scorePanel.fullIncrease();
					//온전하지 않은 과일이면 5점 증가
					else if (wordList.get(i).getIcon() == halfApple || wordList.get(i).getIcon() == halfLemon
							|| wordList.get(i).getIcon() == halfGrape)
						scorePanel.halfIncrease();
					//애벌레 모양 단어면 속도 감소, slowTime 타이머 시작
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

			//게임 오버 시
			if (!gameOn && !scorePanel.getRestart()) {
				for (int i = 0; i < this.getComponentCount(); i++) {
					this.getComponents()[i].setVisible(false);
				}
				g.setFont(new Font("Arial", Font.BOLD, 80));
				g.setColor(Color.red);
				g.drawString("Game Over", 100, 340);

				g.setFont(new Font("나눔고딕", Font.BOLD, 45));
				g.setColor(Color.black);
				g.drawString("우측에서 랭킹을 확인하세요", 43, 440);
			}

			repaint();
		}
	}

	//사용자 입력 패널
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
