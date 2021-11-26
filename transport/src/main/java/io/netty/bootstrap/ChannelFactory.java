/*
 * Copyright 2013 The Netty Project
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
package io.netty.bootstrap;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ReflectiveChannelFactory;
import sun.nio.ch.SelectorProviderImpl;

import java.nio.channels.SelectableChannel;

/**
 * @deprecated Use {@link io.netty.channel.ChannelFactory} instead.
 */
@Deprecated
public interface ChannelFactory<T extends Channel> {
    /**
     * Creates a new channel.
     * 创建新 channel 的步骤：
     * 1. 在 ReflectiveChannelFactory 通过反射创建 NioServerSocketChannel 实例 {@link ReflectiveChannelFactory#newChannel()}
     * 2. 在 NioServerSocketChannel 构造器中创建 JDK 底层的 ServerSocketChannel {@link SelectorProviderImpl#openServerSocketChannel()}
     * 3. 为 Channel 创建 id、unsafe、pipeline 成员变量 {@link AbstractChannel#AbstractChannel(io.netty.channel.Channel)}
     * 4. 设置 Channel 为非阻塞模式 {@link SelectableChannel#configureBlocking(boolean)}
     */
    T newChannel();
}
