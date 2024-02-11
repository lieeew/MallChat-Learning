package com.leikooo.mallchat.common.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author leikooo
 */
@Slf4j
@Configuration
public class NettyWebSocketServer {
    public static final int WEB_SOCKET_PORT = 8090;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private final EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    @PostConstruct
    public void start() {
        try {
            run();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e.getCause());
        }
    }

    @PreDestroy
    public void destroy() {
        Future<?> bossFuture = bossGroup.shutdownGracefully();
        Future<?> workFuture = workerGroup.shutdownGracefully();
        bossFuture.syncUninterruptibly();
        workFuture.syncUninterruptibly();
        log.info("关闭 ws server 成功");
    }

    private void run() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 发送心跳包
                        pipeline.addLast(new IdleStateHandler(90, 0, 0));
                        // http 协议使用 http 解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 以快方式写
                        pipeline.addLast(new ChunkedWriteHandler());
                        /*
                           1. http数据在传输过程中是分段的，HttpObjectAggregator 可以把多个段聚合起来；
                           2. 这就是为什么当浏览器发送大量数据时，就会发出多次 http请求的原因
                         */
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        // 保存用户 ip 信息
                        pipeline.addLast(new HttpHeadersHandler());
                        /*
                          说明：
                           1. 对于 WebSocket，它的数据是以帧frame 的形式传递的；
                           2. 可以看到 WebSocketFrame 下面有6个子类
                           3. 浏览器发送请求时： ws://localhost:7000/hello 表示请求的uri
                           4. WebSocketServerProtocolHandler 核心功能是把 http协议升级为 ws 协议，保持长连接. 是通过一个状态码 101 来切换的
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                        // 自定义handler ，处理业务逻辑
                        pipeline.addLast(new NettyWebSocketServerHandler());
                    }
                });
        // 启动服务器，监听端口，阻塞直到启动成功
        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
        log.info("启动成功");
    }
}
