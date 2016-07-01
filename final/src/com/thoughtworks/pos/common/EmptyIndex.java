package com.thoughtworks.pos.common;

/**
 * Created by ZXR on 2016/6/26.
 */
public class EmptyIndex extends Exception {
    public EmptyIndex (){
        super("索引文件为空");
    }
}
