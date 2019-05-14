package vip.liteng.net;

import java.util.List;

/**
 * @author LiTeng
 * @date 2019-05-14
 */
public class WxArticle {

    /**
     * children : []
     * courseId : 13
     * id : 408
     * name : 鸿洋
     * order : 190000
     * parentChapterId : 407
     * userControlSetTop : false
     * visible : 1
     */

    @com.google.gson.annotations.SerializedName("courseId") private int mCourseId;
    @com.google.gson.annotations.SerializedName("id") private int mId;
    @com.google.gson.annotations.SerializedName("name") private String mName;
    @com.google.gson.annotations.SerializedName("order") private int mOrder;
    @com.google.gson.annotations.SerializedName("parentChapterId") private int mParentChapterId;
    @com.google.gson.annotations.SerializedName("userControlSetTop") private boolean mUserControlSetTop;
    @com.google.gson.annotations.SerializedName("visible") private int mVisible;
    @com.google.gson.annotations.SerializedName("children") private List<?> mChildren;

    public int getCourseId() { return mCourseId;}

    public void setCourseId(int courseId) { mCourseId = courseId;}

    public int getId() { return mId;}

    public void setId(int id) { mId = id;}

    public String getName() { return mName;}

    public void setName(String name) { this.mName = name;}

    public int getOrder() { return mOrder;}

    public void setOrder(int order) { mOrder = order;}

    public int getParentChapterId() { return mParentChapterId;}

    public void setParentChapterId(int parentChapterId) { mParentChapterId = parentChapterId;}

    public boolean isUserControlSetTop() { return mUserControlSetTop;}

    public void setUserControlSetTop(
            boolean userControlSetTop) { mUserControlSetTop = userControlSetTop;}

    public int getVisible() { return mVisible;}

    public void setVisible(int visible) { mVisible = visible;}

    public List<?> getChildren() { return mChildren;}

    public void setChildren(List<?> children) { mChildren = children;}

    @Override
    public String toString() {
        return "WxArticle{" + "mCourseId=" + mCourseId + ", mId=" + mId + ", mName='" + mName + '\'' + ", mOrder=" + mOrder + ", mParentChapterId=" + mParentChapterId + ", mUserControlSetTop=" + mUserControlSetTop + ", mVisible=" + mVisible + ", mChildren=" + mChildren + '}';
    }
}
