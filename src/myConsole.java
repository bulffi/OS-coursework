import com.sun.org.apache.bcel.internal.generic.FMUL;

import java.util.LinkedList;
import java.util.List;

public class myConsole {
    private static myFile currentDirectory;
    private static boolean insertMode;
    private static myFile currentFile;



    public static void initialConsole(){
        FileManager.load();
        currentDirectory = FileManager.getFileLinkedList().getFirst();
        insertMode = false;
    }

    private static String pwd(){
        assert insertMode==false;
        return currentDirectory.getFullName();
    }

    private static boolean cd(String fullPath){
        LinkedList<myFile> files = FileManager.getFileLinkedList();
        for(myFile file:files){
            if(file.getFullName().equals(fullPath)&&file.getType().equals("0")){
                currentDirectory = file;
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

    public static String interpret(String aCommand) {
        if (!insertMode) {
            String[] commands = aCommand.split("\\s+");
            if (commands.length > 2) {
                return "Too long";
            }
            if (commands.length == 0) {
                return "Too short";
            }
            String typo = commands[0];
            if (typo.equals("pwd")) {
                return pwd();
            } else if (typo.equals("ls")) {
                StringBuilder fullPath = new StringBuilder();
                if (commands.length == 1) {
                    fullPath.append(currentDirectory.getFullName());
                } else {
                    if (commands[1].indexOf(".") == 0 && commands[1].indexOf("..") != 0) {
                        if (commands[1].indexOf("/") == 1) {
                            commands[1] = commands[1].replaceFirst("/", "");
                        }
                        fullPath.append(commands[1].replaceFirst(".", currentDirectory.getFullName()));
                    } else if (commands[1].indexOf("..") == 0) {
                        if (commands[1].indexOf("/") == 2) {
                            commands[1] = commands[1].replaceFirst("/", "");
                        }
                        if (currentDirectory.getFatherID() != null) {
                            fullPath.append(commands[1].replaceFirst("..", currentDirectory.getFatherID().getFullName()));
                        } else {
                            return "Root has no father";
                        }

                    }
                }
                if (fullPath.lastIndexOf("/") != fullPath.length() - 1) {
                    fullPath.append("/");
                }
                System.out.println("ls " + fullPath.toString());
                return ls(fullPath.toString());
            } else if (typo.equals("cd")) {
                if (commands.length == 1 || commands[1].equals("")) {
                    if (cd("/")) {
                        return "";
                    } else {
                        return "No root?";
                    }
                } else {
                    StringBuilder fullPath = new StringBuilder();
                    String where = commands[1];
                    if (where.indexOf(".") == 0 && where.indexOf("..") != 0) {
                        if (where.indexOf("/") == 1) {
                            where = where.replaceFirst("/", "");
                        }
                        fullPath.append(where.replaceFirst(".", currentDirectory.getFullName()));
                    } else if (where.indexOf("..") == 0) {
                        if (where.indexOf("/") == 2) {
                            where = where.replaceFirst("/", "");
                        }
                        if (currentDirectory.getFatherID() != null) {
                            fullPath.append(where.replaceFirst("..", currentDirectory.getFatherID().getFullName()));
                        } else {
                            return "Root has no father";
                        }
                    } else if (where.indexOf("/") == 0) {
                        fullPath.append(where);
                    } else {
                        fullPath.append(currentDirectory.getFullName() + where);
                    }
                    //TODO 不允许最后一个是”/“
                    if (fullPath.lastIndexOf("/") != fullPath.length() - 1) {
                        fullPath.append("/");
                    }
                    System.out.println("cd " + fullPath.toString());
                    if (cd(fullPath.toString())) {
                        return "";
                    } else {
                        return "No such directory";
                    }
                }


            }
            else if (typo.equals("mkdir")) {
                LinkedList<myFile> files = FileManager.getFileLinkedList();
                if (commands.length == 1) {
                    return "No name?";
                }
                else {
                    System.out.println("mkdir " + currentDirectory.getFullName() + commands[1] + "/");
                    for (myFile existFile : currentDirectory.getChildrenIDs()) {
                        if (existFile.getFullName().equals(currentDirectory.getFullName() + commands[1] + "/")) {
                            return "Already exist";
                        }
                    }
                    int index = FileManager.getFileLinkedList().indexOf(currentDirectory);
                    myFile newDirectory = new myFile(currentDirectory.getFullName() + commands[1] + "/", currentDirectory, "0");
                    LinkedList<myFile> currentChildren = currentDirectory.getChildrenIDs();
                    currentChildren.add(newDirectory);
                    currentDirectory.setChildrenIDs(currentChildren);
                    files.set(index,currentDirectory);
                    files.add(newDirectory);
                    //System.out.println("Current files"+files);
                    if (FileManager.update()) {
                        return "";
                    } else {
                        return "No more spaces";
                    }
                }
            }
            else if (typo.equals("nanoo")) {
                if (commands.length == 1) {
                    return "Invalid open, you have to specify a name";
                } else {
                    for (myFile file:currentDirectory.getChildrenIDs()){
                        if(file.getFullName().equals(currentDirectory.getFullName()+commands[1])){
                            insertMode = true;
                            currentFile = file;
                            return  currentFile.getContent();
                        }
                    }
                    int index = FileManager.getFileLinkedList().indexOf(currentDirectory);
                    assert index!=-1;
                    myFile newFile = new myFile(currentDirectory.getFullName()+commands[1],currentDirectory,"1");
                    LinkedList<myFile> currentChildren = currentDirectory.getChildrenIDs();
                    currentChildren.add(newFile);
                    currentDirectory.setChildrenIDs(currentChildren);
                    FileManager.getFileLinkedList().set(index,currentDirectory);
                    LinkedList<myFile> files = FileManager.getFileLinkedList();
                    files.add(newFile);
                    if(FileManager.update()){
                        insertMode = true;
                        currentFile = newFile;
                        return "";
                    }else {
                        return "Don't write! No space to store";
                    }
                }
            }
            else if (typo.equals("rm")){
                if(commands.length==1){
                    return "Which to delete?";
                }
                else {
                    System.out.println("rm "+currentDirectory.getFullName()+commands[1]);
                    for(myFile file:currentDirectory.getChildrenIDs()){
                        if(file.getFullName().equals(currentDirectory.getFullName()+commands[1])){
                            if(file.getType().equals("0")){
                                if(file.getChildrenIDs().size()==0){
                                    if(file.getFatherID()!=null){
                                        myFile father = file.getFatherID();
                                        int fatherIndex = FileManager.getFileLinkedList().indexOf(father);
                                        father.getChildrenIDs().remove(file);
                                        FileManager.getFileLinkedList().set(fatherIndex,father);
                                        FileManager.getFileLinkedList().remove(file);

                                        LinkedList<Integer> space = FileManager.getAvailableSpaces();
                                        for(int s:file.getLocations()){
                                            space.add(s);
                                        }
                                        space.sort((a,b)->{
                                            if(a>b) return 1;
                                            else if(a<b) return -1;
                                            return 0;
                                        });

                                        if(FileManager.update()){
                                            return "";
                                        }else {
                                            return "Delete fail";
                                        }
                                    }
                                }else {
                                    return "Delete the content first";
                                }
                            }else {
                                if(file.getFatherID()!=null){
                                    myFile father = file.getFatherID();
                                    int fatherIndex = FileManager.getFileLinkedList().indexOf(father);
                                    System.out.println("fatherIndex: "+fatherIndex);
                                    father.getChildrenIDs().remove(file);
                                    FileManager.getFileLinkedList().set(fatherIndex,father);
                                    FileManager.getFileLinkedList().remove(file);


                                    LinkedList<Integer> space = FileManager.getAvailableSpaces();
                                    for(int s:file.getLocations()){
                                        space.add(s);
                                    }
                                    space.sort((a,b)->{
                                        if(a>b) return 1;
                                        else if(a<b) return -1;
                                        return 0;
                                    });

                                    if(FileManager.update()){
                                        return "";
                                    }else {
                                        return "Delete fail";
                                    }
                                }
                            }
                        }
                    }
                    return "No such file or directory";
                }
            }
            else if(typo.equals("newLife")){
                FileManager.reStart();
                initialConsole();
                return "Wish you good luck";
            }
            return "No such command";
        }
        else{
            assert currentFile!=null;
            int index = FileManager.getFileLinkedList().indexOf(currentFile);
            currentFile.setContent(aCommand);
            FileManager.getFileLinkedList().set(index,currentFile);
            insertMode = false;
            currentFile = null;
            if(FileManager.update()){
                return "Saved!";
            }
            else {
                return "No more room!";
            }
        }
    }


}
