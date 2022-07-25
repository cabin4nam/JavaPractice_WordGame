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
	// �̹���
	private Image heartImage = new ImageIcon("heart.png").getImage();
	private ImageIcon replayButtonImg = new ImageIcon("replayButton.png");
	private ImageIcon pressedReplayButtonImg = new ImageIcon("pressedReplayButton.png");
	private ImageIcon exitButtonImg = new ImageIcon("exitButton.png");
	private ImageIcon pressedExitButtonImg = new ImageIcon("pressedExitButton.png");
	
	private int heart = 3; // ��ó�� ��� 3���� ����
	private boolean isRestart = false; //�ٽ� ���� ����
	private boolean isExit = false; //Ȩ���� ���ư������� ���� 
	
	private int language; //���� ���
	private int level; //���� ����

	// ������ ������ ������ ������ ǥ���� ���̺��
	private int score = 0;
	private JLabel textLabel = new JLabel("����");
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	private JLabel user = new JLabel("����� ID");
	private JTextField userID = new JTextField(100);

	//�ٽý���, Ȩ ��ư
	private JButton replayButton = new JButton(replayButtonImg);
	private JButton exitButton = new JButton(exitButtonImg);

	// ��ŷ �ҷ����ų� �����ϱ� ���ؼ�
	private Ranking ranking = new Ranking();

	public ScorePanel(int language, int level) {
		this.language = language;
		this.level = level;

		this.setBackground(new Color(153, 204, 255));
		setLayout(null);

		textLabel.setSize(50, 20);
		textLabel.setFont(new Font("�������", Font.BOLD, 20));
		textLabel.setLocation(15, 15);
		add(textLabel);

		scoreLabel.setSize(100, 20);
		scoreLabel.setFont(new Font("�������", Font.BOLD, 20));
		scoreLabel.setLocation(70, 15);
		add(scoreLabel);

		user.setSize(100, 20);
		user.setFont(new Font("�������", Font.BOLD, 20));
		user.setLocation(10, 60);
		add(user);

		userID.setSize(160, 20);
		userID.setLocation(115, 60);
		add(userID);

		//����ڰ� ���̵� �Է����� ��
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
					
					//��ŷ ���
					JLabel rankLabel = new JLabel("1");
					JLabel nameLabel = new JLabel(name);
					JLabel scoreLabel = new JLabel(Integer.toString(score));
					setLayout(null);

					rankLabel.setSize(100, 50);
					rankLabel.setLocation(30, (i * 38) + 100);
					rankLabel.setFont(new Font("�������", Font.BOLD, 20));
					rankLabel.setText(Integer.toString((Integer.parseInt(rankLabel.getText())) + i));
					add(rankLabel);

					nameLabel.setSize(100, 50);
					nameLabel.setLocation(130, (i * 38) + 100);
					nameLabel.setFont(new Font("�������", Font.ITALIC, 20));
					add(nameLabel);

					scoreLabel.setSize(100, 50);
					scoreLabel.setLocation(250, (i * 38) + 100);
					scoreLabel.setFont(new Font("�������", Font.ITALIC, 20));
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

	//���� 10�� ����(������ ����)
	public void fullIncrease() {
		score += 10;
		scoreLabel.setText(Integer.toString(score));
	}
	
	//���� 5�� ����(�������� ���� ����)
	public void halfIncrease() {
		score += 5;
		scoreLabel.setText(Integer.toString(score));
	}

	//��� ���� ��ȯ
	public int getHeart() {
		return heart;
	}

	public void setHeart(int n) {
		heart = n;
	}

	//���� ���� ��ȯ
	public int getScore() {
		return score;
	}

	//�ٽ� ����
	public boolean getRestart() {
		return isRestart;
	}

	public void setRestart(boolean isRestart) {
		this.isRestart = isRestart;
	}

	//Ȩ������ �̵�
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
