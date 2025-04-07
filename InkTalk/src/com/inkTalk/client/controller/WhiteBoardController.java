package com.inkTalk.client.controller;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class WhiteBoardController  extends JFrame implements ActionListener, MouseListener, MouseMotionListener{
    JButton thickness;
    JButton colorpick;
    JButton eraiser;
    JButton clearall;
    JButton drawsave;
    JButton walkout;

    Color currentColor = Color.BLACK;
    int currentThickness = 2;

    DrawCanvas canvas;

 // 각각의 획(Stroke)은 점들의 리스트로 구성되므로 해당 리스트는 곡선들을 arraylist로 담아놓는 것.
    ArrayList<Stroke> strokes = new ArrayList<>();
    Stroke currentStroke = null;

    public WhiteBoardController() {
        //setTitle(DB서 가져올 채팅방 이름);

        canvas = new DrawCanvas();
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
        toolbar.setFloatable(false); // 드래그 비활성화
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS)); // 수직 정렬
        toolbar.setPreferredSize(new Dimension(100, getHeight())); // 툴바 폭 설정

        // 버튼 생성 + 리사이즈된 아이콘 설정
        thickness = new JButton(resizeIcon("pentxt.jpg", 100, 50));
        colorpick = new JButton(resizeIcon("colorpicktxt.jpg", 100, 50));
        eraiser = new JButton(resizeIcon("erase.jpg", 100, 50));
        clearall = new JButton(resizeIcon("eraseall.jpg", 100, 50));
        drawsave = new JButton(resizeIcon("drawsave.jpg", 100, 50));
        walkout = new JButton(resizeIcon("walkouttxt.jpg", 100, 50));

        //아니 로그인쪽 로고는 되는데 왜 여기는 안돼?????? 저 여기는 못했어요 죄송합니다
        JButton[] buttons = { thickness, colorpick, eraiser, clearall, drawsave, walkout };
        for (JButton btn : buttons) {
            btn.setPreferredSize(new Dimension(100, 50));
            btn.setMaximumSize(new Dimension(100, 50));
            btn.setMinimumSize(new Dimension(100, 50));
            btn.setFocusable(false); // 포커스 테두리 제거
            btn.addActionListener(this); // 이벤트 리스너 추가
            toolbar.add(btn); // 툴바에 추가
            if(btn==walkout) {
            	toolbar.addSeparator();
            	toolbar.add(btn);
            }
        }
        toolbar.setBackground(new Color(213,231,242));

        add(toolbar, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation(kit.getScreenSize().width / 2 - 550, kit.getScreenSize().height / 2 - 350);
        Image img = kit.getImage("image/logo.jpg");
        setIconImage(img);
        setVisible(true);
    }
    
        private ImageIcon resizeIcon(String path, int width, int height) {
        	java.net.URL imgURL = getClass().getResource("images/"+path);
            if (imgURL == null) {
                System.err.println("이미지를 찾을 수 없습니다: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
    public static void main(String[] args) {
    	 SwingUtilities.invokeLater(WhiteBoardController::new);
    }//EDT에서 실행

 // 그리기용 커스텀 패널 클래스랑 stroke(획) 등... 변수 쉽게 쓰려고 그냥 내부클래스로 만듦.
    class Stroke {
        ArrayList<Point> points;
        Color color;
        int thickness;

        public Stroke(Color color, int thickness) {
            this.color = color;
            this.thickness = thickness;
            this.points = new ArrayList<>();
        }

        public void addPoint(Point p) {
            points.add(p);
        }
    }

    // 그리기 패널
    class DrawCanvas extends JPanel {
    	 BufferedImage canvasImage;
    	 Graphics2D g2;
    	 public DrawCanvas() {
    	        //BufferedImage 초기화
    	        canvasImage = new BufferedImage(1000, 700, BufferedImage.TYPE_INT_ARGB);
    	        g2 = canvasImage.createGraphics();
    	        g2.setColor(Color.WHITE); // 기본 배경 흰색
    	        g2.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
    	    }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(canvasImage, 0, 0, null);
            Graphics2D g2 = (Graphics2D) g;//Graphics가 parameter긴 하나, 실질적으로 저 메소드에 전달될 parameter는 Graphics2D 유형 객체라 이렇게 형변환.
            //스윙 컴포넌트가 자신의 모양을 그리는 메소드로, swing component 중 하나인 jpanel도 해당 메소드 보유, overriding 가능. 
            //즉 jcomponent를 상속받아(여기선 jpanel)서 사용할 수 있는 것.
            //parameter로 graphics 객체를 가지며, 이건 component 그리기에 필요한 도구 제공함. method 선언을 저 형태로만 하는 듯?


            for (Stroke stroke : strokes) {
                g2.setColor(stroke.color);
                g2.setStroke(new BasicStroke(stroke.thickness));
                ArrayList<Point> pts = stroke.points;
                for (int i = 0; i < pts.size() - 1; i++) {
                    Point p1 = pts.get(i);
                    Point p2 = pts.get(i + 1);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }//그냥 좌표 두개만 저장하면 결국 직선들밖에 그릴 수 없음. 일반 그림판처럼 곡선을 그리려면...이렇게 점들을 저장해야 함. 
            }
        }
        public void redrawToBufferedImage() {
            //BufferedImage 위에도 stroke를 다시 그림 (저장용)
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight()); // 초기화

            for (Stroke s : strokes) {
                g2.setColor(s.color);
                g2.setStroke(new BasicStroke(s.thickness));
                for (int i = 1; i < s.points.size(); i++) {
                    Point p1 = s.points.get(i - 1);
                    Point p2 = s.points.get(i);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        public BufferedImage getImage() {
            return canvasImage;
        }
    }
    

    // 마우스를 누르면 새 Stroke 시작
    @Override
    public void mousePressed(MouseEvent e) {
        currentStroke = new Stroke(currentColor, currentThickness);
        currentStroke.addPoint(e.getPoint());
        strokes.add(currentStroke);
        canvas.repaint();
    }

    // 드래그하면서 점 추가
    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentStroke != null) {
            currentStroke.addPoint(e.getPoint());
            canvas.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentStroke = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == thickness) {
            String input = JOptionPane.showInputDialog(this, "두께를 입력하세요 (1~20)");
            try {
                int t = Integer.parseInt(input);
                currentThickness = Math.max(1, Math.min(20, t));
            } catch (NumberFormatException ignored) {
            }
        } else if (e.getSource() == colorpick) {
            Color pickedColor = JColorChooser.showDialog(this, "색상 선택", currentColor);
            if (pickedColor != null) currentColor = pickedColor;
        } else if (e.getSource() == eraiser) {
            currentColor = Color.WHITE; // 이거 사진 붙이거나 배경색 변경 가능하게한다면 코드 다시 짜야할듯
        } else if (e.getSource() == clearall) {
            int choice = JOptionPane.showConfirmDialog(this, "전체 그림을 지우시겠습니까?", "전체 삭제", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                strokes.clear();
                canvas.repaint();
            }
        }else if(e.getSource()==drawsave) {
        	 canvas.redrawToBufferedImage(); // ✅ 현재까지 그린 것 BufferedImage에 반영

        	    try {
        	        String filename = "my_drawing_" + System.currentTimeMillis() + ".png";
        	        //방 제목+저장한 사람으로 해도 괜찮을 것 같아요
        	        File output = new File(filename);
        	        ImageIO.write(canvas.getImage(), "png", output);
        	        JOptionPane.showMessageDialog(this, "이미지가 저장되었습니다:\n" + filename);
        	    } catch (Exception ex) {
        	        ex.printStackTrace();
        	        JOptionPane.showMessageDialog(this, "저장에 실패했습니다. 다시 시도해주세요.");
        	    }
        	}
        
        else if (e.getSource() == walkout) {
            int exit = JOptionPane.showConfirmDialog(this, "해당 채팅방을 나가시겠습니까?\n현재 그림은 저장되지 않습니다."
            		+"\n그림을 저장하길 원하신다면 저장 버튼을 누른 후 채팅방을 나가길 권장합니다.", "채팅방 나가기", JOptionPane.OK_CANCEL_OPTION);
            //원래는 해당 채팅방 이름을 가져와야 하는데 이걸...DB에서 가져오고 난 후에 해야 할듯 
            //방장/다른 참가자 다르게 띄우는건 소켓 연결하고 나서ㅜ
            if (exit == JOptionPane.OK_OPTION) {
                dispose(); // 일단 dispose 해놨는데 이걸 다른 panel로 옮겨야겟징 이것도 평일에 가서 해야지...
            }
        }
    }

    // 나머지 필수 인터페이스 구현 (사용 안 함)
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
