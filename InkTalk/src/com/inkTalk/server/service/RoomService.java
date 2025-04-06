package com.inkTalk.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import com.inkTalk.server.domain.Room;

/**
 * 서버에 연결된 세션과 Room 상태를 메모리 상에서 관리
 */
public class RoomService {
    private static final RoomService instance = new RoomService();
    private final Map<Long, Room> rooms = new ConcurrentHashMap<>();

    private RoomService() {}

    public static RoomService getInstance() {
        return instance;
    }

    public Room getRoom(Long roomId) {
        return rooms.get(roomId);
    }

    public void createRoom(Room room) {
        rooms.putIfAbsent(room.getRoomId(), room);
    }

    public void removeRoom(Long roomId) {
        rooms.remove(roomId);
    }

    public void joinRoom(Long roomId, Long userId, Session session) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.addUser(userId, session);
        }
    }

    public void exitRoom(Long roomId, Long userId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.removeUser(userId);
            if (room.getConnectedUsers().isEmpty()) {
                rooms.remove(roomId); // 자동 제거 (선택적)
            }
        }
    }

    public Map<Long, Room> getAllRooms() {
        return rooms;
    }
}
