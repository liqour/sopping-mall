package com.shop.websocket;

import java.nio.charset.Charset;

public final class TextMessage extends AbstractWebSocketMessage<String> {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private final byte[] bytes;
	
	public TextMessage(CharSequence payload) {
		super(payload.toString(), true);
		this.bytes = null;
	}
	
	public TextMessage(byte[] payload) {
		super(new String(payload, UTF_8));
		this.bytes = payload;
	}
	
	public TextMessage(CharSequence payload, boolean isLast) {
		super(payload.toString(), isLast);
		this.bytes = null;
	}

	@Override
	public int getPayloadLength() {
		return asBytes().length;
	}

	public byte[] asBytes() {
		return (this.bytes != null ? this.bytes : getPayload().getBytes(UTF_8));
	}

	@Override
	protected String toStringPayload() {
		return (getPayloadLength() > 10) ? getPayload().substring(0, 10) + ".." : getPayload();
	}
}
