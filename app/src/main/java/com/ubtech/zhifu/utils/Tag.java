package com.ubtech.zhifu.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class Tag {
    private ArrayList<Tag> mChildren = new ArrayList<>();
    private String mContent;
    private String mName;
    private String mPath;

    Tag(String path, String name) {
        this.mPath = path;
        this.mName = name;
    }

    /* access modifiers changed from: package-private */
    public void addChild(Tag tag) {
        this.mChildren.add(tag);
    }

    /* access modifiers changed from: package-private */
    public String getName() {
        return this.mName;
    }

    /* access modifiers changed from: package-private */
    public String getContent() {
        return this.mContent;
    }

    /* access modifiers changed from: package-private */
    public void setContent(String content) {
        boolean hasContent = false;
        if (content != null) {
            int i = 0;
            while (true) {
                if (i < content.length()) {
                    char c = content.charAt(i);
                    if (c != ' ' && c != 10) {
                        hasContent = true;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
        }
        if (hasContent) {
            this.mContent = content;
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Tag> getChildren() {
        return this.mChildren;
    }

    /* access modifiers changed from: package-private */
    public boolean hasChildren() {
        return this.mChildren.size() > 0;
    }

    /* access modifiers changed from: package-private */
    public int getChildrenCount() {
        return this.mChildren.size();
    }

    /* access modifiers changed from: package-private */
    public Tag getChild(int index) {
        if (index < 0 || index >= this.mChildren.size()) {
            return null;
        }
        return this.mChildren.get(index);
    }

    /* access modifiers changed from: package-private */
    public HashMap<String, ArrayList<Tag>> getGroupedElements() {
        HashMap<String, ArrayList<Tag>> groups = new HashMap<>();
        Iterator<Tag> it = this.mChildren.iterator();
        while (it.hasNext()) {
            Tag child = it.next();
            String key = child.getName();
            ArrayList<Tag> group = groups.get(key);
            if (group == null) {
                group = new ArrayList<>();
                groups.put(key, group);
            }
            group.add(child);
        }
        return groups;
    }

    /* access modifiers changed from: package-private */
    public String getPath() {
        return this.mPath;
    }

    public String toString() {
        return "Tag: " + this.mName + ", " + this.mChildren.size() + " children, Content: " + this.mContent;
    }

}
