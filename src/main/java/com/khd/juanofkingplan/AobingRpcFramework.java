package com.khd.juanofkingplan;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author kehd
 * @Date 2022-2-17 18:38
 * @Version 1.0
 * @Description
 */
public class AobingRpcFramework
{
   // private static Logger logger = LoggerFactory.getLogger(AobingRpcFramework.class);

    public static void export(Object service, int port) throws Exception {
        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket socket = server.accept();
            new Thread(() -> {
                try{
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    //读取到方法名
                    String methodName = (String) input.readObject();
                    //读取到方法类型数组
                    Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                    //读取到的参数数组
                    Object[] arguments = (Object[]) input.readObject();
                    //获取方法
                    Method method = service.getClass().getMethod(methodName, parameterTypes);
                    //反射调用获取结果
                    Object result = method.invoke(service,arguments);
                    //返回结果
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    output.writeObject(result);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
    }
    @SuppressWarnings("all")
    public static <T> T refer(Class<T> interfaceClass, String host, int port) throws Exception {
        //代理
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass},
                (proxy, method, arguments) -> {
                    //获取socket
                    Socket socket = new Socket(host,port );
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    //写入方法名
                    output.writeObject(method.getName());
                    //写入参数类型
                    output.writeObject(method.getParameterTypes());
                    //写入参数
                    output.writeObject(arguments);
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    Object result = input.readObject();
                    return result;
                });
    }
}
