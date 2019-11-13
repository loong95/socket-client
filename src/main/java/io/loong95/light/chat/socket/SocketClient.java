package io.loong95.light.chat.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.nio.charset.StandardCharsets;

/**
 * @author linyunlong
 */
public class SocketClient {
    private JTextPane logPane;
    private ChannelFuture channelFuture;

    public SocketClient(JTextPane logPane) {
        this.logPane = logPane;
    }

    private void appendLog(String msg) {
        Document document = logPane.getDocument();
        try {
            document.insertString(document.getLength(), msg + "\n", new SimpleAttributeSet());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8));
        appendLog("发送：" + msg);
    }

    public void sendBinary(String msg) {
        byte[] bytes = ByteUtil.binaryStringToBytes(msg);
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(bytes));
        appendLog("发送[bin]：" + msg);
    }

    public void sendHex(String msg) {
        byte[] bytes = ByteUtil.hexStringToBytes(msg);
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(bytes));
        appendLog("发送[hex]：" + msg);
    }

    public synchronized void reconnect(String host, int port) {
        if (channelFuture != null) {
            channelFuture.channel().close();
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        Bootstrap bs = new Bootstrap();

        bs.group(bossGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
                                int length = byteBuf.readableBytes();
                                byte[] bytes = new byte[length];
                                byteBuf.readBytes(bytes);
                                appendLog("接收到[HEX]：" + ByteUtil.bytesToHexString(bytes));
                            }
                        });
                    }
                });
        try {
            channelFuture = bs.connect(host, port).sync();
            appendLog("连接成功：" + host + ":" + port);
        } catch (Exception e) {
            appendLog("客户端连接失败：" + e.getMessage());
        }
    }
}
