import java.util.LinkedList;
import java.util.List;

public class myConsole {
    private static myFile currentFile;
    private static boolean insertMode;


    public static void initialConsole(){
        FileManager.load();
        currentFile = FileManager.getFileLinkedList().getFirst();
        insertMode = false;
    }

    private static String pwd(){
        assert insertMode==false;
        return currentFile.getFullName();
    }

    private static boolean cd(String fullPath){
        LinkedList<myFile> files = FileManager.getFileLinkedList();
        for(myFile file:files){
            if(file.getFullName().equals(fullPath)&&file.getType().equals("0")){
                currentFile = file;
                return true;
            }
        }
        return false;
    }

    private static String ls(String fullPath){
        LinkedList<myFile> files = FileManager.getFileLinkedList();
        for(myFile file:files){
            if(file.getFullName().equals(fullPath)){
                if(file.getType().equals("0")){
                    List<myFile> children = file.getChildrenIDs();
                    StringBuilder targetHelper = new StringBuilder();
                    for(myFile child:children){
                        targetHelper.append(child.getFullName()+" "+child.getCreateTime()+"\n");
                    }
                    return targetHelper.toString();
                }else {
                    return "We are not talking about a directory";
                }
            }
        }
        return "You point to nowhere";
    }

    public static String interpret(String aCommand){
        String[] commands = aCommand.split("\\s+");
        if(commands.length>2){
            return "Too long";
        }
        if(commands.length==0){
            return "Too short";
        }
        String typo= commands[0];
        if(typo.equals("pwd")){
            return pwd();
        }
        else if(typo.equals("ls")){
            StringBuilder fullPath = new StringBuilder();
            if(commands.length==1){
                fullPath.append(currentFile.getFullName());
            }else {
                if(commands[1].indexOf(".")==0 && commands[1].indexOf("..")!=0){
                    if(commands[1].indexOf("/")==1){
                        commands[1]=commands[1].replaceFirst("/","");
                    }
                    fullPath.append(commands[1].replaceFirst(".",currentFile.getFullName())) ;
                } else if(commands[1].indexOf("..")==0){
                    if(commands[1].indexOf("/")==2){
                        commands[1]=commands[1].replaceFirst("/","");
                    }
                    if(currentFile.getFatherID()!=null) {
                        fullPath.append(commands[1].replaceFirst("..", currentFile.getFatherID().getFullName()));
                    }else {
                        return "Root has no father";
                    }

                }
            }
            if(fullPath.lastIndexOf("/")!=fullPath.length()-1){
                fullPath.append("/");
            }
            System.out.println("ls "+fullPath.toString());
            return ls(fullPath.toString());
        }
        else if(typo.equals("cd")){
            if(commands.length==1||commands[1].equals("")){
                if(cd("/")){
                    return "";
                }else {
                    return "No root?";
                }
            }
            else {
                StringBuilder fullPath = new StringBuilder();
                String where = commands[1];
                if(where.indexOf(".")==0 && where.indexOf("..")!=0){
                    if(where.indexOf("/")==1){
                        where=where.replaceFirst("/","");
                    }
                    fullPath.append(where.replaceFirst(".",currentFile.getFullName()));
                }else if(where.indexOf("..")==0){
                    if(where.indexOf("/")==2){
                        where=where.replaceFirst("/","");
                    }
                    if(currentFile.getFatherID()!=null) {
                        fullPath.append(where.replaceFirst("..", currentFile.getFatherID().getFullName()));
                    }else {
                        return "Root has no father";
                    }
                }else if(where.indexOf("/")==0){
                    fullPath.append(where);
                }else {
                    fullPath.append(currentFile.getFullName()+where);
                }
                //TODO 不允许最后一个是”/“
                if(fullPath.lastIndexOf("/")!=fullPath.length()-1){
                    fullPath.append("/");
                }
                System.out.println("cd "+fullPath.toString());
                if(cd(fullPath.toString())){
                    return "";
                }else {
                    return "No such directory";
                }
            }


        }
        else if(typo.equals("mkdir")){
            LinkedList<myFile> files = FileManager.getFileLinkedList();
            if(commands.length==1){
                return "No name?";
            }
            else{
                System.out.println("mkdir "+currentFile.getFullName()+commands[1]+"/");
                for(myFile existFile:currentFile.getChildrenIDs()){
                    if(existFile.getFullName().equals(currentFile.getFullName()+commands[1]+"/")){
                        return "Already exist";
                    }
                }
                myFile newFile = new myFile(currentFile.getFullName()+commands[1]+"/",currentFile,"0");
                LinkedList<myFile> currentChildren = currentFile.getChildrenIDs();
                currentChildren.add(newFile);
                files.add(newFile);
                //System.out.println("Current files"+files);
                if(FileManager.update()){
                    return "";
                }
                else {
                    return "No more spaces";
                }
            }
        }

        return "No such command";
    }

}
