package com.khd.juanofkingplan;

/**
 * @Author kehd
 * @Date 2022-2-17 18:38
 * @Version 1.0
 * @Description
 */
public class AobingServiceImpl implements AobingService
{
    @Override
    public String hello(String name) {
        return "Yo man hello, i am" + name;
    }
}
