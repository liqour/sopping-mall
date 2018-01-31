package com.shop.websocket;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketMessage;

/**
 * 
 * @Description: 可以在WebSocket连接上处理或发送的消息
 * @ClassName: AbstractWebSocketMessage重写
 * @author liquor
 * @date 2017年9月16日 下午4:26:38 
 * 
 * @param <T>
 */
public abstract class AbstractWebSocketMessage<T> implements WebSocketMessage<T>{

	private final T payload;

	private final boolean last;
	
	/**
	 * 使用给定的类型创建一个新的WebSocket消息
	 */
	public AbstractWebSocketMessage(T payload) {
		this(payload, true);
	}
	
	public AbstractWebSocketMessage(T payload, boolean isLast) {
		Assert.notNull(payload, "payload must not be null");
		this.payload = payload;
		this.last = isLast;
	}
	
	@Override
	public T getPayload() {
		return this.payload;
	}

	@Override
	public boolean isLast() {
		return this.last;
	}
	
	@Override
	public int hashCode() {
		return AbstractWebSocketMessage.class.hashCode() * 13 + ObjectUtils.nullSafeHashCode(this.payload);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AbstractWebSocketMessage)) {
			return false;
		}
		AbstractWebSocketMessage<?> otherMessage = (AbstractWebSocketMessage<?>) other;
		return ObjectUtils.nullSafeEquals(this.payload, otherMessage.payload);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " payload= " + toStringPayload()
				+ ", byteCount=" + getPayloadLength() + ", last=" + isLast() + "]";
	}

	protected abstract String toStringPayload();
}
