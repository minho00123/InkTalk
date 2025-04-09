package com.inkTalk.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.inkTalk.domain.Room;
import com.inkTalk.domain.User;
import com.inkTalk.ui.Chatboard;
import com.inkTalk.ui.CreateRoomUi;
import com.inkTalk.ui.EnterRoomUi;
import com.inkTalk.ui.LoginUi;
import com.inkTalk.ui.RoomListUi;
import com.inkTalk.ui.SignupUi;
import com.inkTalk.ui.Whiteboard;

public class AppController extends JPanel{
    private JPanel viewContainer;
    private CardLayout cardLayout;
    
    private Socket socket;
    private User loggedInUser;
    private Room room=new Room(1,1,"기획회의방2");
    private LoginUi loginUi;
    private SignupUi signupUi;
    private JPanel drawChat;
    private Whiteboard whiteboard;
    private Chatboard chatboard;
    private RoomListUi roomList;
    private EnterRoomUi enterRoom;
    private CreateRoomUi createRoom;
   
	public AppController() {

        setLayout(new BorderLayout());
        // 소켓 연결
        try {
            socket = new Socket("172.30.1.31", 5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewContainer = new JPanel();
        cardLayout = new CardLayout();
        viewContainer.setLayout(cardLayout);

        // 각 화면 초기화 및 등록
        loginUi = new LoginUi(this);
        signupUi = new SignupUi(this); 
        roomList = new RoomListUi(this);
        createRoom = new CreateRoomUi(this);
        enterRoom = new EnterRoomUi(this,room);
        whiteboard = new Whiteboard(this, socket);
        chatboard = new Chatboard(socket, loggedInUser.getNickname());

        drawChat = new JPanel(new BorderLayout());
        drawChat.add(whiteboard, BorderLayout.CENTER);
        drawChat.add(chatboard, BorderLayout.EAST);

        viewContainer.add(loginUi, "LOGIN");
        viewContainer.add(signupUi, "SIGNUP");
        viewContainer.add(roomList, "ROOMLIST");
        viewContainer.add(createRoom, "CREATEROOM");
        viewContainer.add(enterRoom, "ENTERROOM");
       
        viewContainer.add(drawChat, "DRAW_CHAT");

        add(viewContainer, BorderLayout.CENTER);

        // 초기 화면
        show("LOGIN");
	}
    public void show(String name) {
        cardLayout.show(viewContainer, name);
    }
    public void loginSuccess(User user) {
        this.loggedInUser = user;
//        chatboard.setUserNickname(user.getNickname());
        show("ENTERROOM"); //방 입장하기 화면으로 전환
    }
    public User getLoggedInUser() {
        return loggedInUser;
    }
    public Socket getSocket() {
        return socket;
    }

	public void dispose() {
	    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
	    if (topFrame != null) {
	        topFrame.dispose();  // 창 닫기
	    }
		
	}	

}
