/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.lcrcbank.meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class RoomManager {

	private final Logger log = LoggerFactory.getLogger(RoomManager.class);

	@Autowired
	private KurentoClient kurento;

	private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<>();

	/**
	 * Looks for a room in the active room list.
	 *
	 * @param roomName
	 *            the name of the room
	 * @return the room if it was already created, or null if it is the first
	 *         time this room is accessed
	 */
	public Room getRoom(String roomName) {
		log.debug("Searching for room {}", roomName);
		Room room = rooms.get(roomName);

		if (room == null) {
			log.debug("Room {} not existent.", roomName);
//			room = new Room(roomName, kurento.createMediaPipeline());
//			rooms.put(roomName, room); 
			return null;
		}
		log.debug("Room {} found!", roomName);
		return room;
	}

	public List getAll() {
		log.debug("Searching for All room");
		List roomList = new ArrayList(rooms.keySet());
		return roomList;
	}

	/**
	 * Create a new room in the active room list.
	 *
	 * @param roomName
	 *            the name of the room
	 * @return false if it was already created, or true if it is the first time
	 *         this room is accessed
	 */
	public boolean createRoom(String roomName) {
		if (getRoom(roomName) == null) {
			log.debug("Room {} not existent. Will create now!", roomName);
			Room room = new Room(roomName, kurento.createMediaPipeline());
			rooms.put(roomName, room);
			return true;
		}
		return false;
	}

	/**
	 * Create a new room with random name in the active room list. The name of
	 * room is a unique string of 6 bit integer.
	 * 
	 * @return the name of the new room
	 */
	public String createRoom() {
		if(rooms.size() >= 1000) return null;
		Random random = new Random();
		String roomName;
		Integer x;
		do {
			x = random.nextInt(899999) + 100000;
			roomName = x.toString();
		} while (!createRoom(roomName));
		return roomName;
	}

	/**
	 * Removes a room from the list of available rooms.
	 *
	 * @param room
	 *            the room to be removed
	 */
	public void removeRoom(Room room) {
		this.rooms.remove(room.getName());
		room.close();
		log.info("Room {} removed and closed", room.getName());
	}

}
