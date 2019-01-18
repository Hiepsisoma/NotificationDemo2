package vn.framgia.phamhung.notificaitondemo2;

public class Song {
    private int mId;
    private String mName;
    private int mFile;

    public Song(int id, String name, int file) {
        mId = id;
        mName = name;
        mFile = file;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getFile() {
        return mFile;
    }

    public void setFile(int file) {
        mFile = file;
    }
}
