package com.leikooo.mallchat.common.FlatBuffer;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;

/**
 * @author <a href="https://github.com/lieeew">leikooo</a>
 * @date 2024/3/7
 * @description
 */
public class FlatBuffers {

//    public static void main(String[] args) {
//        // 创建 FlatBufferBuilder
//        FlatBufferBuilder builder = new FlatBufferBuilder();
//
//        // 构建 User 对象
//        int userNameOffset = builder.createString("John Doe");
//        User.startUser(builder);
//        User.addId(builder, 123);
//        User.addName(builder, userNameOffset);
//        int userOffset = User.endUser(builder);
//
//        // 完成构建
//        builder.finish(userOffset);
//
//        // 获取序列化后的字节数组
//        byte[] serializedData = builder.sizedByteArray();
//
//        // 假设现在您想要反序列化
//        // 创建一个 ByteBuffer
//        ByteBuffer byteBuffer = ByteBuffer.wrap(serializedData);
//
//        // 反序列化 User 对象
//        User user = User.getRootAsUser(byteBuffer);
//
//        // 获取反序列化后的数据
//        int id = user.id();
//        String name = user.name();
//
//        // 打印结果
//        System.out.println("ID: " + id);
//        System.out.println("Name: " + name);
//    }

}
