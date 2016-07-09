package com.boluozhai.snow.discovery.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.boluozhai.snow.discovery.scheme.PrivateScheme;
import com.boluozhai.snow.discovery.scheme.PublicScheme;

public class OnRxContext {

	public DatagramSocket socket;
	public DatagramPacket packet;
	public PrivateScheme privateScheme;
	public PublicScheme publicScheme;

}
