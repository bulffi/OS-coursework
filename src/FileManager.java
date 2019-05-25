import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FileManager {
    public static LinkedList<myFile> getFileLinkedList() {
        return fileLinkedList;
    }

    public static void setFileLinkedList(LinkedList<myFile> fileLinkedList) {
        FileManager.fileLinkedList = fileLinkedList;
    }

    private static LinkedList<myFile> fileLinkedList;
    private static LinkedList<Integer> availableSpaces;

    private final int totalSize = 500;



    public static void load(){
        fileLinkedList = new LinkedList<>();
        availableSpaces = new LinkedList<>();
        LinkedList<String[]> dirs = new LinkedList<>();
        LinkedList<String> content = new LinkedList<>();
        try {
            FileReader fr=new FileReader("dir.txt");
            BufferedReader br=new BufferedReader(fr);
            String line="";
            String[] dir=null;
            while ((line=br.readLine())!=null) {
                dir=line.split(" ");
                dirs.add(dir);
            }
            br.close();
            fr.close();

            fr = new FileReader("content.txt");
            br = new BufferedReader(fr);
            line="";
            while ((line=br.readLine())!=null) {
                content.add(line);
            }
            br.close();
            fr.close();

            fr = new FileReader("space.txt");
            br = new BufferedReader(fr);
            line="";
            while ((line=br.readLine())!=null) {
                availableSpaces.add(Integer.parseInt(line));
            }
            br.close();
            fr.close();

        }catch (Exception e){
            System.out.println("in load "+e);

        }

       /*
       for(String[] file:dirs){
            for(String part:file){
                System.out.print(part+" ");
            }
            System.out.println();
        }
        System.out.print(content);
        */
       for(String[] file:dirs){
           LinkedList<Integer> locations = new LinkedList<>();
           String[] location_helper = file[2].split(",");
           StringBuilder myContent = new StringBuilder();
           for(String location:location_helper){
               int loca = Integer.parseInt(location);
               locations.add(loca);
               myContent.append(content.get(loca));
           }
           fileLinkedList.add(new myFile(file[1],file[0],locations,file[3],file[4],myContent.toString()));
       }


       for(myFile file:fileLinkedList){
           if(file.getType().equals("0")){
               String[] children = file.getContent().split(",");
               LinkedList<myFile> childrenList = new LinkedList<>();
               for(String childID:children){
                   for(myFile maybeChild:fileLinkedList){
                       if(maybeChild.getID().equals(childID)){
                           childrenList.add(maybeChild);
                           maybeChild.setFatherID(file);
                           break;
                       }
                   }
               }
               file.setChildrenIDs(childrenList);
           }
       }

/*
     for(myFile file:fileLinkedList){
           System.out.println(file);
     }

*/


    }

    private static String FileArrayToString(List<myFile> list){
        if(list==null){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<list.size()-1;i++){
            builder.append(list.get(i).getID()+",");
        }
        builder.append(list.get(list.size()-1).getID());

        return builder.toString();
    }

    private static String IntArrayToString(List<Integer> list){
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<list.size()-1;i++){
            builder.append(list.get(i)+",");
        }
        builder.append(list.get(list.size()-1));

        return builder.toString();
    }

    public static boolean store(){
        for(int i =0;i<fileLinkedList.size();i++){
            if(fileLinkedList.get(i).getType().equals("0")){
                myFile file = fileLinkedList.get(i);
                file.setContent(FileArrayToString(file.getChildrenIDs()));
            }
        }

        try {
            String[] targetContent = new String[500];
            for(int i=0;i<fileLinkedList.size();i++){
                myFile modifiedFile = fileLinkedList.get(i);
                if(modifiedFile.getLocations()==null||modifiedFile.getLocations().size()!=(modifiedFile.getContent().length()/100 +1)){
            //        System.out.println("I am "+i+": "+"size "+modifiedFile.getLocations().size()+" length: "+modifiedFile.getContent().length());
                    LinkedList<Integer> currentPosi = modifiedFile.getLocations();
                    if(currentPosi==null||currentPosi.size()<modifiedFile.getContent().length()/100+1){
                        if(currentPosi==null){
                            if(availableSpaces.size()!=0){
                                Integer newPosition = availableSpaces.removeFirst();
                                currentPosi = new LinkedList<>();
                            }
                        }
                        while (currentPosi.size()<modifiedFile.getContent().length()/100+1){
                            if(availableSpaces.size()!=0){
                                Integer newPosition = availableSpaces.removeFirst();
                                currentPosi.add(newPosition);
                                continue;
                            }
                            return false;
                        }

                    }else {
                        while (currentPosi.size()>modifiedFile.getContent().length()/100+1){
                            Integer newPosition = currentPosi.removeLast();
                            availableSpaces.add(newPosition);
                        }
                    }
                }
            }
            for(myFile file:fileLinkedList){
                int current_posi=0;
                for(int place:file.getLocations()){
                    targetContent[place]=file.getContent().substring(current_posi,Math.min(current_posi+100,file.getContent().length()));
                    current_posi+=100;
                }
            }

            StringBuilder result_helper = new StringBuilder();
            for(String s:targetContent){
                result_helper.append(s+"\n");
            }

            FileOutputStream fos=new FileOutputStream(new File("dir_out.txt"));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter  bw=new BufferedWriter(osw);

            //bw.write()

            for(myFile file:fileLinkedList){
                String target = new String(file.getID()+" "
                        +file.getFullName()+" "
                        +IntArrayToString(file.getLocations())+" "
                        +file.getCreateTime()+" "
                        +file.getType());
                bw.write(target+"\n");
            }

            bw.close();
            osw.close();
            fos.close();


            fos=new FileOutputStream(new File("content_out.txt"));
            osw=new OutputStreamWriter(fos, "UTF-8");
            bw=new BufferedWriter(osw);

            //bw.write()

            bw.write(result_helper.toString());

            bw.close();
            osw.close();
            fos.close();

            fos=new FileOutputStream(new File("space_out.txt"));
            osw=new OutputStreamWriter(fos, "UTF-8");
            bw=new BufferedWriter(osw);

            //bw.write()

           for(Integer ok:availableSpaces){
               bw.write(ok+"\n");
           }

            bw.close();
            osw.close();
            fos.close();

        }catch (Exception e){
            System.out.println("in store:"+e);
            e.printStackTrace();
        }

        return true;
    }

    public static boolean update(){
        if(store()){
            load();
            return true;
        }
        else {
            return false;
        }
    }




    public static void main(String[] args){
        load();
        store();
    }



}
