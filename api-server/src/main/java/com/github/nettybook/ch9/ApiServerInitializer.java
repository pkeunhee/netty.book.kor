package com.github.nettybook.ch9;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

import com.github.nettybook.ch9.core.ApiRequestParser;

public class ApiServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public ApiServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpRequestDecoder()); // HTTP 프로토콜 내용을 netty 가 인식할 수 있는 ByteBuf 로 변환
        // Uncomment the following line if you don't want to handle HttpChunks.
        p.addLast(new HttpObjectAggregator(65536)); // 메세지 파편화 이슈를 해결. 설정된 65K 를 넘는 데이터를 받는 경우 TooLongFrameException 발생
        p.addLast(new HttpResponseEncoder()); // 응답 메세지를 HTTP 프로토콜로 만들어 줌
        // Remove the following line if you don't want automatic content
        // compression.
        p.addLast(new HttpContentCompressor()); // 송수신되는 HTTP 본문 데이터를 압축, 해제 함. ChannelDuplexHandler 의 구현체이므로 인바운드/아웃바운드 시 모두 수행됨.
        p.addLast(new ApiRequestParser()); // 비지니스 로직 처리
    }
}