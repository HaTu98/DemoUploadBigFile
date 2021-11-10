package com.example.demo.config;

import java.io.File;

public class Constant {
    public static final int MAX_BUF_SIZE = 5 * 1024 * 1024;
    public static final int MAX_CHUNK_FILE = 20;
    public static final String TEMP_FILE = new File("src\\main\\java\\com\\example\\demo\\data").getAbsolutePath() + "\\temp";
    public static final String FINAL_FILE = new File("src\\main\\java\\com\\example\\demo\\data").getAbsolutePath() + "\\final";
}
