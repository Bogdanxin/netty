/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import static java.util.Objects.requireNonNull;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;

import java.net.SocketAddress;

/**
 *  Combines the inbound handling of one {@link ChannelHandler} with the outbound handling of
 *  another {@link ChannelHandler}.
 */
public class CombinedChannelDuplexHandler<I extends ChannelHandler, O extends ChannelHandler>
        extends ChannelHandlerAdapter {

    private DelegatingChannelHandlerContext inboundCtx;
    private DelegatingChannelHandlerContext outboundCtx;
    private volatile boolean handlerAdded;

    private I inboundHandler;
    private O outboundHandler;

    /**
     * Creates a new uninitialized instance. A class that extends this handler must invoke
     * {@link #init(ChannelHandler, ChannelHandler)} before adding this handler into a
     * {@link ChannelPipeline}.
     */
    protected CombinedChannelDuplexHandler() {
        ensureNotSharable();
    }

    /**
     * Creates a new instance that combines the specified two handlers into one.
     */
    public CombinedChannelDuplexHandler(I inboundHandler, O outboundHandler) {
        ensureNotSharable();
        init(inboundHandler, outboundHandler);
    }

    /**
     * Initialized this handler with the specified handlers.
     *
     * @throws IllegalStateException if this handler was not constructed via the default constructor or
     *                               if this handler does not implement all required handler interfaces
     * @throws IllegalArgumentException if the specified handlers cannot be combined into one due to a conflict
     *                                  in the type hierarchy
     */
    protected final void init(I inboundHandler, O outboundHandler) {
        validate(inboundHandler, outboundHandler);
        this.inboundHandler = inboundHandler;
        this.outboundHandler = outboundHandler;
    }

    private void validate(I inboundHandler, O outboundHandler) {
        if (this.inboundHandler != null) {
            throw new IllegalStateException(
                    "init() can not be invoked if " + CombinedChannelDuplexHandler.class.getSimpleName() +
                            " was constructed with non-default constructor.");
        }

        requireNonNull(inboundHandler, "inboundHandler");
        requireNonNull(outboundHandler, "outboundHandler");
        if (ChannelHandlerMask.isOutbound(inboundHandler.getClass())) {
            throw new IllegalArgumentException(
                    "inboundHandler must not implement any outbound method to get combined.");
        }
        if (ChannelHandlerMask.isInbound(outboundHandler.getClass())) {
            throw new IllegalArgumentException(
                    "outboundHandler must not implement any inbound method to get combined.");
        }
    }

    protected final I inboundHandler() {
        return inboundHandler;
    }

    protected final O outboundHandler() {
        return outboundHandler;
    }

    private void checkAdded() {
        if (!handlerAdded) {
            throw new IllegalStateException("handler not added to pipeline yet");
        }
    }

    /**
     * Removes the inbound {@link ChannelHandler} that was combined in this {@link CombinedChannelDuplexHandler}.
     */
    public final void removeInboundHandler() {
        checkAdded();
        inboundCtx.remove();
    }

    /**
     * Removes the outbound {@link ChannelHandler} that was combined in this {@link CombinedChannelDuplexHandler}.
     */
    public final void removeOutboundHandler() {
        checkAdded();
        outboundCtx.remove();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (inboundHandler == null) {
            throw new IllegalStateException(
                    "init() must be invoked before being added to a " + ChannelPipeline.class.getSimpleName() +
                            " if " +  CombinedChannelDuplexHandler.class.getSimpleName() +
                            " was constructed with the default constructor.");
        }

        outboundCtx = new DelegatingChannelHandlerContext(ctx, outboundHandler);
        inboundCtx = new DelegatingChannelHandlerContext(ctx, inboundHandler);

        // The inboundCtx and outboundCtx were created and set now it's safe to call removeInboundHandler() and
        // removeOutboundHandler().
        handlerAdded = true;

        try {
            inboundHandler.handlerAdded(inboundCtx);
        } finally {
            outboundHandler.handlerAdded(outboundCtx);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        try {
            inboundCtx.remove();
        } finally {
            outboundCtx.remove();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelRegistered(inboundCtx);
        } else {
            inboundCtx.fireChannelRegistered();
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelUnregistered(inboundCtx);
        } else {
            inboundCtx.fireChannelUnregistered();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelActive(inboundCtx);
        } else {
            inboundCtx.fireChannelActive();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelInactive(inboundCtx);
        } else {
            inboundCtx.fireChannelInactive();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.exceptionCaught(inboundCtx, cause);
        } else {
            inboundCtx.fireExceptionCaught(cause);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.userEventTriggered(inboundCtx, evt);
        } else {
            inboundCtx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelRead(inboundCtx, msg);
        } else {
            inboundCtx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelReadComplete(inboundCtx);
        } else {
            inboundCtx.fireChannelReadComplete();
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        assert ctx == inboundCtx.ctx;
        if (!inboundCtx.removed) {
            inboundHandler.channelWritabilityChanged(inboundCtx);
        } else {
            inboundCtx.fireChannelWritabilityChanged();
        }
    }

    @Override
    public void bind(
            ChannelHandlerContext ctx,
            SocketAddress localAddress, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.bind(outboundCtx, localAddress, callback);
        } else {
            outboundCtx.bind(localAddress, callback);
        }
    }

    @Override
    public void connect(
            ChannelHandlerContext ctx,
            SocketAddress remoteAddress, SocketAddress localAddress,
            ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.connect(outboundCtx, remoteAddress, localAddress, callback);
        } else {
            outboundCtx.connect(localAddress, callback);
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.disconnect(outboundCtx, callback);
        } else {
            outboundCtx.disconnect(callback);
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.close(outboundCtx, callback);
        } else {
            outboundCtx.close(callback);
        }
    }

    @Override
    public void register(ChannelHandlerContext ctx, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.register(outboundCtx, callback);
        } else {
            outboundCtx.register(callback);
        }
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.deregister(outboundCtx, callback);
        } else {
            outboundCtx.deregister(callback);
        }
    }

    @Override
    public void read(ChannelHandlerContext ctx, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.read(outboundCtx, callback);
        } else {
            outboundCtx.read(callback);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelOutboundInvokerCallback callback)
            throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.write(outboundCtx, msg, callback);
        } else {
            outboundCtx.write(msg, callback);
        }
    }

    @Override
    public void flush(ChannelHandlerContext ctx, ChannelOutboundInvokerCallback callback) throws Exception {
        assert ctx == outboundCtx.ctx;
        if (!outboundCtx.removed) {
            outboundHandler.flush(outboundCtx, callback);
        } else {
            outboundCtx.flush(callback);
        }
    }

    private static final class DelegatingChannelHandlerContext implements ChannelHandlerContext {

        private final ChannelHandlerContext ctx;
        private final ChannelHandler handler;
        boolean removed;

        DelegatingChannelHandlerContext(ChannelHandlerContext ctx, ChannelHandler handler) {
            this.ctx = ctx;
            this.handler = handler;
        }

        @Override
        public Channel channel() {
            return ctx.channel();
        }

        @Override
        public EventExecutor executor() {
            return ctx.executor();
        }

        @Override
        public String name() {
            return ctx.name();
        }

        @Override
        public ChannelHandler handler() {
            return ctx.handler();
        }

        @Override
        public boolean isRemoved() {
            return removed || ctx.isRemoved();
        }

        @Override
        public ChannelHandlerContext fireChannelRegistered() {
            ctx.fireChannelRegistered();
            return this;
        }

        @Override
        public ChannelHandlerContext fireChannelUnregistered() {
            ctx.fireChannelUnregistered();
            return this;
        }

        @Override
        public ChannelHandlerContext fireChannelActive() {
            ctx.fireChannelActive();
            return this;
        }

        @Override
        public ChannelHandlerContext fireChannelInactive() {
            ctx.fireChannelInactive();
            return this;
        }

        @Override
        public ChannelHandlerContext fireExceptionCaught(Throwable cause) {
            ctx.fireExceptionCaught(cause);
            return this;
        }

        @Override
        public ChannelHandlerContext fireUserEventTriggered(Object event) {
            ctx.fireUserEventTriggered(event);
            return this;
        }

        @Override
        public ChannelHandlerContext fireChannelRead(Object msg) {
            ctx.fireChannelRead(msg);
            return this;
        }

        @Override
        public ChannelHandlerContext fireChannelReadComplete() {
            ctx.fireChannelReadComplete();
            return this;
        }

        @Override
        public ChannelHandlerContext fireChannelWritabilityChanged() {
            ctx.fireChannelWritabilityChanged();
            return this;
        }

        @Override
        public ChannelHandlerContext bind(SocketAddress localAddress, ChannelOutboundInvokerCallback callback) {
            ctx.bind(localAddress, callback);
            return this;
        }

        @Override
        public ChannelHandlerContext connect(
                SocketAddress remoteAddress, SocketAddress localAddress, ChannelOutboundInvokerCallback callback) {
            ctx.connect(remoteAddress, localAddress, callback);
            return this;
        }

        @Override
        public ChannelHandlerContext disconnect(ChannelOutboundInvokerCallback callback) {
            ctx.disconnect(callback);
            return this;
        }

        @Override
        public ChannelHandlerContext close(ChannelOutboundInvokerCallback callback) {
            ctx.close(callback);
            return this;
        }

        @Override
        public ChannelHandlerContext register(ChannelOutboundInvokerCallback callback) {
            ctx.register(callback);
            return this;
        }

        @Override
        public ChannelHandlerContext deregister(ChannelOutboundInvokerCallback callback) {
            ctx.deregister(callback);
            return this;
        }

        @Override
        public ChannelHandlerContext write(Object msg, ChannelOutboundInvokerCallback callback) {
            return ctx.write(msg, callback);
        }

        @Override
        public ChannelHandlerContext writeAndFlush(Object msg, ChannelOutboundInvokerCallback callback) {
            ctx.writeAndFlush(msg, callback);
            return this;
        }

        @Override
        public ChannelHandlerContext read(ChannelOutboundInvokerCallback callback) {
            ctx.read(callback);
            return this;
        }

        @Override
        public ChannelHandlerContext flush(ChannelOutboundInvokerCallback callback) {
            ctx.flush(callback);
            return this;
        }

        @Override
        public ChannelPipeline pipeline() {
            return ctx.pipeline();
        }

        @Override
        public ByteBufAllocator alloc() {
            return ctx.alloc();
        }

        @Override
        public ChannelPromise newPromise() {
            return ctx.newPromise();
        }

        @Override
        public ChannelFuture newSucceededFuture() {
            return ctx.newSucceededFuture();
        }

        @Override
        public ChannelFuture newFailedFuture(Throwable cause) {
            return ctx.newFailedFuture(cause);
        }

        @Override
        public <T> Attribute<T> attr(AttributeKey<T> key) {
            return ctx.channel().attr(key);
        }

        @Override
        public <T> boolean hasAttr(AttributeKey<T> key) {
            return ctx.channel().hasAttr(key);
        }

        void remove() {
            EventExecutor executor = executor();
            if (executor.inEventLoop()) {
                remove0();
            } else {
                executor.execute(this::remove0);
            }
        }

        private void remove0() {
            if (!removed) {
                removed = true;
                try {
                    handler.handlerRemoved(this);
                } catch (Throwable cause) {
                    fireExceptionCaught(new ChannelPipelineException(
                            handler.getClass().getName() + ".handlerRemoved() has thrown an exception.", cause));
                }
            }
        }
    }
}
