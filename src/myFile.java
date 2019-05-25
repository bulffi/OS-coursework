import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class myFile {
    private String fullName;
    private String ID;
    private LinkedList<Integer> locations;
    private String createTime;
    private myFile fatherID;
    private LinkedList<myFile> childrenIDs;
    private String type;
    private String content;

    public myFile(String fullName,myFile fatherID,String type){
        this.fullName = fullName;
        this.fatherID = fatherID;
        this.type = type;
        ID = UUID.randomUUID().toString();
        createTime = (new Date()).toString().replaceAll(" ","-");
        content = "";
        locations = new LinkedList<>();
        childrenIDs = new LinkedList<>();
        //children

    }

    public myFile(String fullName, String ID, LinkedList<Integer> locations, String createTime, myFile fatherID, LinkedList<myFile> childrenIDs, String type, String content) {
        this.fullName = fullName;
        this.ID = ID;
        this.locations = locations;
        this.createTime = createTime;
        this.fatherID = fatherID;
        this.childrenIDs = childrenIDs;
        this.type = type;
        this.content = content;
    }

    public myFile(){}

    public myFile(String fullName, String ID, LinkedList<Integer> locations, String createTime, String type,String content) {
        this.fullName = fullName;
        this.ID = ID;
        this.locations = locations;
        this.createTime = createTime;
        this.type = type;
        this.content = content;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public LinkedList<Integer> getLocations() {
        return locations;
    }

    public void setLocations(LinkedList<Integer> locations) {
        this.locations = locations;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public myFile getFatherID() {
        return fatherID;
    }

    public void setFatherID(myFile fatherID) {
        this.fatherID = fatherID;
    }

    public LinkedList<myFile> getChildrenIDs() {
        return childrenIDs;
    }

    public void setChildrenIDs(LinkedList<myFile> childrenIDs) {
        this.childrenIDs = childrenIDs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "myFile{" +
                "fullName='" + fullName + '\'' +
                ", ID='" + ID + '\'' +
                ", locations=" + locations +
                ", createTime='" + createTime + '\'' +
                ", fatherID='" + (fatherID==null? "god":fatherID.getID()) + '\'' +
               // ", childrenIDs=" + childrenIDs +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        myFile file = (myFile) o;
        return fullName.equals(file.fullName) &&
                ID.equals(file.ID) &&
       //         locations.equals(file.locations) &&
                createTime.equals(file.createTime);
       //         Objects.equals(fatherID, file.fatherID) &&
      //          childrenIDs.equals(file.childrenIDs) &&
        //        type.equals(file.type) &&
       //         content.equals(file.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, ID, locations, createTime, fatherID, childrenIDs, type, content);
    }
}
