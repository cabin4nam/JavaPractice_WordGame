import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ScorePanel extends JPanel {
	// 이미지
	private Image heartImage = new ImageIcon("heart.png").getImage();
	private ImageIcon replayButtonImg = new ImageIcon("replayButton.png");
	private ImageIcon pressedReplayButtonImg = new ImageIcon("pressedReplayButton.png");
	private ImageIcon exitButtonImg = new ImageIcon("exitButton.png");
	private ImageIcon pressedExitButtonImg = new ImageIcon("pressedExitButton.png");
	
	private int heart = 3; // 맨처음 목숨 3개로 시작
	private boolean isRestart = false; //다시 시작 여부
	private boolean isExit = false; //홈으로 돌아가는지의 여부 
	
	private int language; //현재 언어
	private int level; //현재 레벨

	// 점수를 저장할 변수와 점수를 표현할 레이블들
	private int score = 0;
	private JLabel textLabel = new JLabel("점수");
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel user = new JLabel("사용자 ID");
	private JTextField userID = new JTextField(100);

	//다시시작, 홈 버튼
	private JButton replayButton = new JButton(replayButtonImg);
	private JButton exitButton = new JButton(exitButtonImg);

	// 랭킹 불러오거나 저장하기 위해서
	private Ranking ranking = new Ranking();

	public ScorePanel(int language, int level) {
		this.language = language;
		this.level = level;

		this.setBackground(new Color(153, 204, 255));
		setLayout(null);

		textLabel.setSize(50, 20);
		textLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
		textLabel.setLocation(15, 15);
		add(textLabel);

		scoreLabel.setSize(100, 20);
		scoreLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
		scoreLabel.setLocation(70, 15);
		add(scoreLabel);

		user.setSize(100, 20);
		user.setFont(new Font("나눔고딕", Font.BOLD, 20));
		user.setLocation(10, 60);
		add(user);

		userID.setSize(160, 20);
		userID.setLocation(115, 60);
		add(userID);

		//사용자가 아이디 입력했을 때
		userID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField id = (JTextField) e.getSource();
				ranking.add(id.getText(), score);
				ranking.sort();
				for (int i = 0; i < ranking.getSize(); i++) {
					if (i >= 10)
						break;

					String name = ranking.getId(i);
					int score = ranking.getScore(name);

					if (name == null || score < 0)
						break;
					
					//랭킹 출력
					JLabel rankLabel = new JLabel("1");
					JLabel nameLabel = new JLabel(name);
					JLabel scoreLabel = new JLabel(Integer.toString(score));
					setLayout(null);

					rankLabel.setSize(100, 50);
					rankLabel.setLocation(30, (i * 38) + 100);
					rankLabel.setFont(new Font("나눔고딕", Font.BOLD, 20));
					rankLabel.setText(Integer.toString((Integer.parseInt(rankLabel.getText())) + i));
					add(rankLabel);

					nameLabel.setSize(100, 50);
					nameLabel.setLocation(130, (i * 38) + 100);
					nameLabel.setFont(new Font("나눔고딕", Font.ITALIC, 20));
					add(nameLabel);

					scoreLabel.setSize(100, 50);
					scoreLabel.setLocation(250, (i * 38) + 100);
					scoreLabel.setFont(new Font("나눔고딕", Font.ITALIC, 20));
					add(scoreLabel);
				}
				id.setText("");
				userID.removeActionListener(this);
			}
		});

		replayButton.setSize(50, 50);
		replayButton.setLocation(227, 2);
		replayButton.setBorderPainted(false);
		replayButton.setFocusPainted(false);
		replayButton.setContentAreaFilled(false);
		replayButton.setOpaque(false);
		add(replayButton);
		replayButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isRestart = true;
				score = 0;
				scoreLabel.setText(Integer.toString(score));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JButton bt = (JButton) e.getSource();
				bt.setIcon(pressedReplayButtonImg);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JButton bt = (JButton) e.getSource();
				bt.setIcon(replayButtonImg);
			}
		});

		exitButton.setSize(50, 50);
		exitButton.setLocation(268, 0);
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setOpaque(false);
		add(exitButton);
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isExit = true;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JButton bt = (JButton) e.getSource();
				bt.setIcon(pressedExitButtonImg);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JButton bt = (JButton) e.getSource();
				bt.setIcon(exitButtonImg);
			}
		});
	}

	//점수 10점 증가(온전한 과일)
	public void fullIncrease() {
		score += 10;
		scoreLabel.setText(Integer.toString(score));
	}
	
	//점수 5점 증가(온전하지 못한 과일)
	public void halfIncrease() {
		score += 5;
		scoreLabel.setText(Integer.toString(score));
	}

	//목숨 개수 반환
	public int getHeart() {
		return heart;
	}

	public void setHeart(int n) {
		heart = n;
	}

	//현재 점수 반환
	public int getScore() {
		return score;
	}

	//다시 시작
	public boolean getRestart() {
		return isRestart;
	}

	public void setRestart(boolean isRestart) {
		this.isRestart = isRestart;
	}

	//홈으로의 이동
	public boolean getExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (!isExit) {
			super.paintComponent(g);
			for (int i = 0; i < heart; i++) {
				g.drawImage(heartImage, 110 + (i * 40), 5, 40, 40, null);
			}
		}
		repaint();
	}
}
