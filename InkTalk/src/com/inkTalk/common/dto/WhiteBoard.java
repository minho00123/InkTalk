 package com.inkTalk.common.dto;

import java.io.Serializable;

/**
 * 서버와 클라이언트가 공유할 그리기 데이터
 */
public class WhiteBoard implements Serializable{
	private Long roomId;
	private Long sender;
	//그림 관련 필드
	
	
	public WhiteBoard() {}
}
