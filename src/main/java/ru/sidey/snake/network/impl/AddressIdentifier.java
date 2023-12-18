package ru.sidey.snake.network.impl;

import java.net.InetAddress;

public record AddressIdentifier(
        InetAddress address,
        int port
) {
}
