import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class MainFrame extends JFrame {
	//�̹���
	private Image mainBackground = new ImageIcon("mainBackground.png").getImage();
	private ImageIcon startButtonImage = new ImageIcon("startButton.png");
	private ImageIcon pressedButtonImage = new ImageIcon("pressedStartButton.png");

	private String languageList[] = { "english", "korean" }; //��� ����
	private JComboBox<String> languageBox = new JComboBox<String>(languageList); //��� ���� �ڽ�
	private String levelList[] = { "Level 1", "Level 2", "Level 3" }; //�������� ����
	private JComboBox<String> levelBox = new JComboBox<String>(levelList); //�������� ���� �ڽ�
	private JButton startButton = new JButton(startButtonImage); //���� ���� ��ư

	private int language = 0; // 0�̸� ����, 1�̸� �ѱ�
	private int level = 0; // 1~3����
	private boolean isGameOn = false; //���� ���� ������ �ƴ����� ����

	ScorePanel scorePanel = null;
	EditPanel editPanel = null;
	MainPanel mainPanel = new MainPanel();

	public MainFrame() {
		setTitle("Ÿ���� ���� ����ȭ��");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 800);

		Container container = this.getContentPane();
		container.add(mainPanel); //mainPanel�� Panel����

		setResizable(false);
		setVisible(true);
	}

	public class MainPanel extends JPanel {
		public MainPanel() {
			setLayout(null);

			//���ùڽ���� ���ӽ��� ��ư ����
			languageBox.setSize(250, 50);
			languageBox.setLocation(350, 390);
			languageBox.setFont(new Font("�������", Font.BOLD, 20));
			add(languageBox);

			levelBox.setSize(250, 50);
			levelBox.setLocation(350, 460);
			levelBox.setFont(new Font("�������", Font.BOLD, 20));
			add(levelBox);

			startButton.setSize(400, 100);
			startButton.setLocation(270, 550);
			startButton.setBorderPainted(false);
			startButton.setFocusPainted(false);
			startButton.setContentAreaFilled(false);
			add(startButton);
			startButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					JButton b = (JButton) e.getSource();
					b.setIcon(pressedButtonImage);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					JButton b = (JButton) e.getSource();
					b.setIcon(startButtonImage);
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					isGameOn = true;
					language = languageBox.getSelectedIndex();
					level = levelBox.getSelectedIndex();
					splitPane(new ScorePanel(language, level), new EditPanel(language));
					mainPanel.setVisible(false);
				}
			});
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(mainBackground, 0, 0, this.getWidth(), this.getHeight(), this);
			repaint();
		}
	}

	//���� �߿� Ȩȭ���� ������ �� ȣ��Ǵ� �Լ�
	public void exitGame() {
		mainPanel.setVisible(true);
		repaint();
	}

	//���� ������ ���� ���ο� �г� ����
	private void splitPane(ScorePanel scorePanel, EditPanel editPanel) {
		if (scorePanel.getRestart()) {
			this.removeAll();
		}

		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(650);
		hPane.setEnabled(false);
		hPane.setLeftComponent(new GamePanel(scorePanel, editPanel, MainFrame.this, language, level));

		JSplitPane pPane = new JSplitPane();
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(500);
		pPane.setTopComponent(scorePanel);
		pPane.setBottomComponent(editPanel);
		hPane.setRightComponent(pPane);
	}
}
