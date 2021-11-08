package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
public class FileUploadService {

    public void combineFile(File folder, String location) throws IOException {
        File output = new File(location);
        FileWriter fileWriter = null;
        BufferedWriter bw = null;
        try
        {
            fileWriter = new FileWriter(output, true);
            bw = new BufferedWriter(fileWriter);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

       for (File f : folder.listFiles() ){
           FileInputStream fis;
           try
           {
               fis = new FileInputStream(f);
               BufferedReader br = new BufferedReader(new InputStreamReader(fis));

               String aLine;
               while((aLine = br.readLine()) != null)
               {
                   bw.write(aLine);
                   bw.newLine();
               }

               br.close();
           }
           catch(IOException e1)
           {
               e1.printStackTrace();
           }
       }
        try
        {
            bw.close();
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }
    }

}
